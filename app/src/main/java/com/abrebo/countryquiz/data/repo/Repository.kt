package com.abrebo.countryquiz.data.repo

import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.datasource.DataSource
import com.abrebo.countryquiz.data.model.FlagQuestion
import com.abrebo.countryquiz.data.model.RankUser
import com.abrebo.countryquiz.data.model.User

class Repository(private var dataSource: DataSource) {
    fun uploadUser(): MutableLiveData<List<User>> = dataSource.uploadUser()
    fun search(word:String): MutableLiveData<List<User>> = dataSource.search(word)
    fun saveUser(user:User) = dataSource.saveUser(user)
    fun deleteUser(userId:String) = dataSource.deleteUser(userId)
    fun updateUser(user:User) = dataSource.updateUser(user)
    suspend fun checkUserNameAvailability(userName: String): Boolean = dataSource.checkUserNameAvailability(userName)
    suspend fun getHighestScore(userId: String): Int = dataSource.getHighestScore(userId)
    suspend fun saveHighestScore(userId: String, score: Int) = dataSource.saveHighestScore(userId, score)
    suspend fun getUserNameByEmail(userEmail: String): String? =dataSource.getUserNameByEmail(userEmail)
    suspend fun getAllRankUsers(): List<RankUser> = dataSource.getAllRankUsers()


}