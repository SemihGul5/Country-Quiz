package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.GameCategory
import com.abrebo.countryquiz.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (var repository: Repository,
                                             application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val highestScore = MutableLiveData<Int>()
    val categoryList = MutableLiveData<List<GameCategory>>()
    fun getHighestScore(userName: String,game:Int) {
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userName,game)
            highestScore.value = currentHighestScore
        }
    }
    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }
    fun loadCategories(userName: String) {
        val categories = listOf(
            GameCategory(1, context.getString(R.string.FindtheFlagbyCountryName), context.getString(R.string.Game1Text)),
            GameCategory(7, context.getString(R.string.FindtheCountrybyGeographicLocation), context.getString(R.string.Game7Text)),
            GameCategory(2, context.getString(R.string.Findthecountrybytheflag), context.getString(R.string.Game2Text)),
            GameCategory(3, context.getString(R.string.FindtheCapitaloftheCountry), context.getString(R.string.Game3Text)),
            GameCategory(4, context.getString(R.string.FindthePopulationoftheCountry), context.getString(R.string.Game4Text)),
            GameCategory(6, context.getString(R.string.Game6Title), context.getString(R.string.Game6Text)),
            GameCategory(8, context.getString(R.string.Game8Title), context.getString(R.string.Game8Text)),
        )

        categoryList.value = categories

        categories.forEach { category ->
            viewModelScope.launch {
                val highestScore = repository.getHighestScore(userName, category.id)
                category.highestScore = highestScore

                val rankUsers = repository.getAllRankUsers(category.id)
                val userRank = rankUsers.find { it.userName == userName }?.rank ?: 0
                category.rank = userRank

                categoryList.postValue(categories)
            }
        }
    }

}