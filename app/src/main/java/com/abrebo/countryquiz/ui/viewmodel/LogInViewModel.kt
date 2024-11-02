package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.User
import com.abrebo.countryquiz.data.repo.Repository
import com.abrebo.countryquiz.datastore.AppPref
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor (var repository: Repository,
                                          application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val appPref= AppPref.getInstance(context)
    var userName= MutableLiveData<String>()

    fun saveUserName(userName:String){
        viewModelScope.launch {
            appPref.saveUserName(userName)
            repository.saveUser(User("",userName))
        }
    }
    fun getUserName(){
        viewModelScope.launch {
            userName.value = appPref.getUserName()
        }
    }
    fun checkUserNameAvailability(userName: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAvailable = withContext(Dispatchers.IO) {
                repository.checkUserNameAvailability(userName)
            }
            callback(isAvailable)
        }
    }

}