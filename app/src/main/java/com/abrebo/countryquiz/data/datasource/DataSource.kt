package com.abrebo.countryquiz.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.data.model.RankUser
import com.abrebo.countryquiz.data.model.User
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class DataSource(var collectionReference: CollectionReference,
                 var collectionReferenceUserScores: CollectionReference) {
    var userList=MutableLiveData<List<User>>()

    fun uploadUser():MutableLiveData<List<User>>{
        collectionReference.addSnapshotListener { value, error ->
            if (value != null) {
                val list=ArrayList<User>()

                for (d in value.documents){
                    val user=d.toObject(User::class.java)
                    if (user!=null){
                        user.id=d.id
                        list.add(user)
                    }
                }
                userList.value=list
            }
        }
        return userList
    }
    fun search(word:String): MutableLiveData<List<User>> {
        collectionReference.addSnapshotListener { value, error ->
            if(value != null){
                val list = ArrayList<User>()

                for(d in value.documents){
                    val user = d.toObject(User::class.java)
                    if(user != null){
                        if(user.userName!!.lowercase().contains(word.lowercase())){
                            user.id = d.id
                            list.add(user)
                        }
                    }
                }
                userList.value = list
            }
        }
        return userList
    }

    fun saveUser(user:User){
        collectionReference.document().set(user)
    }
    fun deleteUser(userId:String){
        collectionReference.document(userId).delete()
    }

    fun updateUser(user:User){
        val newUser=HashMap<String,Any>()
        newUser["nameFamily"]=user.nameFamily!!
        newUser["userName"]=user.userName!!
        newUser["email"]=user.email!!
        collectionReference.document(user.id!!).update(newUser)
    }

    suspend fun checkUserNameAvailability(userName: String): Boolean {
        return try {
            val querySnapshot = collectionReference
                .whereEqualTo("userName", userName)
                .get()
                .await()

            querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("hata",e.message.toString())
            false
        }
    }
    suspend fun getHighestScore(userId: String): Int {
        val document = collectionReferenceUserScores.document(userId).get().await()
        return document.getLong("highestScore")?.toInt() ?: 0
    }

    suspend fun saveHighestScore(userId: String, score: Int) {
        val userDoc = collectionReferenceUserScores.document(userId).get().await()
        if (!userDoc.exists()) {
            val rankUser = RankUser(
                rank = 0,
                userName = userId,
                highestScore = score
            )
            collectionReferenceUserScores.document(userId).set(rankUser)
            updateUserRanks()
        } else {
            val currentHighestScore = userDoc.getLong("highestScore")?.toInt() ?: 0
            if (score >= currentHighestScore) {
                val rankUser = RankUser(
                    rank = userDoc.getLong("rank")?.toInt() ?: 0,
                    userName = userId,
                    highestScore = score
                )
                collectionReferenceUserScores.document(userId).set(rankUser)

                updateUserRanks()
            }
        }
    }

    private suspend fun updateUserRanks() {
        val users = collectionReferenceUserScores.get().await().documents
        val sortedUsers = users.map { doc ->
            RankUser(
                rank = 0,
                userName = doc.id,
                highestScore = doc.getLong("highestScore")?.toInt() ?: 0
            )
        }.sortedByDescending { it.highestScore }
        sortedUsers.forEachIndexed { index, rankUser ->
            collectionReferenceUserScores.document(rankUser.userName).update("rank", index + 1)
        }
    }

    suspend fun getUserNameByEmail(userEmail: String): String? {
        return try {
            val querySnapshot = collectionReference
                .whereEqualTo("email", userEmail)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                document.getString("userName")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("hata", e.message.toString())
            null
        }
    }
    suspend fun getAllRankUsers(): List<RankUser> {
        return try {
            val usersSnapshot = collectionReferenceUserScores.get().await()
            usersSnapshot.documents.map { document ->
                RankUser(
                    rank = document.getLong("rank")?.toInt() ?: 0,
                    userName = document.getString("userName") ?: "Unknown",
                    highestScore = document.getLong("highestScore")?.toInt() ?: 0
                )
            }
        } catch (e: Exception) {
            Log.e("hata",e.message.toString())
            emptyList()
        }
    }
}