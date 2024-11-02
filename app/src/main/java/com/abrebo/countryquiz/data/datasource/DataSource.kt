package com.abrebo.countryquiz.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.data.model.User
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class DataSource(var collectionReference: CollectionReference) {
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
        newUser["userName"]=user.userName!!
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
}