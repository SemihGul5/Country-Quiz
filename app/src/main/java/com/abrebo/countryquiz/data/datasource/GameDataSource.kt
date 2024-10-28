package com.abrebo.countryquiz.data.datasource

import android.util.Log
import com.abrebo.countryquiz.data.model.RankUser
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class GameDataSource(
    private val collectionReferenceGame1:CollectionReference,
    private val collectionReferenceGame2:CollectionReference,
    private val collectionReferenceGame3:CollectionReference,
    private val collectionReferenceGame4:CollectionReference,
    private val collectionReferenceGame6:CollectionReference,
    private val collectionReferenceGame7:CollectionReference,
    private val collectionReferenceGame8:CollectionReference) {

    suspend fun getHighestScore(userId: String, game: Int): Int {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1
            2 -> collectionReferenceGame2
            3 -> collectionReferenceGame3
            4 -> collectionReferenceGame4
            6 -> collectionReferenceGame6
            7 -> collectionReferenceGame7
            8 -> collectionReferenceGame8
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val document = collectionReference.document(userId).get().await()
        return document.getLong("highestScore")?.toInt() ?: 0
    }


    suspend fun saveHighestScore(userId: String, score: Int,game:Int) {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1
            2 -> collectionReferenceGame2
            3 -> collectionReferenceGame3
            4 -> collectionReferenceGame4
            6 -> collectionReferenceGame6
            7 -> collectionReferenceGame7
            8 -> collectionReferenceGame8
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val userDoc = collectionReference.document(userId).get().await()
        if (!userDoc.exists()) {
            val rankUser = RankUser(
                rank = 0,
                userName = userId,
                highestScore = score
            )
            collectionReference.document(userId).set(rankUser)
            updateUserRanks(game)
        } else {
            val currentHighestScore = userDoc.getLong("highestScore")?.toInt() ?: 0
            if (score >= currentHighestScore) {
                val rankUser = RankUser(
                    rank = userDoc.getLong("rank")?.toInt() ?: 0,
                    userName = userId,
                    highestScore = score
                )
                collectionReference.document(userId).set(rankUser)

                updateUserRanks(game)
            }
        }
    }

    private suspend fun updateUserRanks(game:Int) {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1
            2 -> collectionReferenceGame2
            3 -> collectionReferenceGame3
            4 -> collectionReferenceGame4
            6 -> collectionReferenceGame6
            7 -> collectionReferenceGame7
            8 -> collectionReferenceGame8
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val users = collectionReference.get().await().documents
        val sortedUsers = users.map { doc ->
            RankUser(
                rank = 0,
                userName = doc.id,
                highestScore = doc.getLong("highestScore")?.toInt() ?: 0
            )
        }.sortedByDescending { it.highestScore }
        sortedUsers.forEachIndexed { index, rankUser ->
            collectionReference.document(rankUser.userName).update("rank", index + 1)
        }
    }


    suspend fun getAllRankUsers(game:Int): List<RankUser> {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1
            2 -> collectionReferenceGame2
            3 -> collectionReferenceGame3
            4 -> collectionReferenceGame4
            6 -> collectionReferenceGame6
            7 -> collectionReferenceGame7
            8 -> collectionReferenceGame8
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        return try {
            val usersSnapshot = collectionReference.get().await()
            usersSnapshot.documents.map { document ->
                RankUser(
                    rank = document.getLong("rank")?.toInt() ?: 0,
                    userName = document.getString("userName") ?: "Unknown",
                    highestScore = document.getLong("highestScore")?.toInt() ?: 0
                )
            }.sortedBy { it.rank }
        } catch (e: Exception) {
            Log.e("hata",e.message.toString())
            emptyList()
        }
    }
}