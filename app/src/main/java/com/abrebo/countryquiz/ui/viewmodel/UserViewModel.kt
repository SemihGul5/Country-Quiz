package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.countryquiz.data.model.RankUser
import com.abrebo.countryquiz.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor (var repository: Repository,
                                         application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var userRankList = MutableLiveData<List<RankUser>>()
    fun getAllRankUsers(game:Int) {
        viewModelScope.launch {
            val users = repository.getAllRankUsers(game)
            userRankList.value = users
        }
    }
    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }


}