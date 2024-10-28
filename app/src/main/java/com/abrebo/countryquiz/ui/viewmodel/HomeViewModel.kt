package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
            GameCategory(1,"Ülke Adına Göre Bayrağını Bul",
                "Bayrağı isme göre eşleştirin. Her soru için 3 saniye. Bayraklar arasında 4 seçenek."),
            GameCategory(2,"Bayrağa Göre Ülkeyi Bul",
                "Bayraklardan ülke ismini bulun. Her soru için 3 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(3,"Ülkenin Başkentini Bul",
                "Ülke başkentlerini öğrenin. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(4,"Ülkenin Nüfusunu Bul",
                "Ülkelerin nüfusunu tahmin edin. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(6,"Bir Dakikada Tahmin Et (Ülke Adına Göre Bayrağını Bul)",
                "Bayrağı isme göre eşleştirin. 1 dakikada olabildiğince çok soru bilin."),
            GameCategory(7,"Coğrafi Konuma Göre Ülkeyi Bul",
                "Harita üzerinde ülkeler gösteriliyor, gösterilen ülkeyi bulun."),
            GameCategory(8,"Ülkenin Kıtasını Bul",
                "Ülkelerin bulunduğu kıtayı doğru tahmin edin. Her soru için 3 saniye. Kıtalar arasında 4 seçenek."),
        )
        categoryList.value = categories
        categories.forEach { category ->
            viewModelScope.launch {
                val highestScore = repository.getHighestScore(userName, category.id)
                category.highestScore = highestScore
                categoryList.postValue(categories)
            }
        }
    }
}