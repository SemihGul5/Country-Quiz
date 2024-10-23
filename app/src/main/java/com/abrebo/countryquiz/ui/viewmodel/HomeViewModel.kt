package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    private val _highestScore = MutableLiveData<Int>()
    val highestScore: LiveData<Int> get() = _highestScore
    val categoryList = MutableLiveData<List<GameCategory>>()

    fun getHighestScore(userName: String) {
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userName)
            _highestScore.value = currentHighestScore
        }
    }
    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }

    fun loadCategories() {
        val categories = listOf(
            GameCategory(1,"Ülke Adına Göre Bayrağını Bul",
                "Bayrağı isme göre eşleştirin. Her soru için 10 saniye. Bayraklar arasında 4 seçenek."),
            GameCategory(2,"Bayrağa Göre Ülke İsmini Bul",
                "Bayraklardan ülke ismini bulun. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(3,"Ülkenin Başkentini Bul",
                "Ülke başkentlerini öğrenin. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(4,"Ülkenin Nüfusunu Tahmin Et",
                "Ülkelerin nüfusunu tahmin edin. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(5,"Ülke Simgeleri ile Ülkeyi Eşleştir",
                "Ülkeleri simgeleriyle eşleştirin. Her soru için 10 saniye. Ülkeler arasında 4 seçenek."),
            GameCategory(6,"Bir Dakikada Tahmin Et (Ülke Adına Göre Bayrağını Bul)",
                "Bayrağı isme göre eşleştirin. 1 dakikada olabildiğince çok soru bilin."),
            GameCategory(7,"Her Soru İçin 5 Saniye (Bayrağa Göre Ülke İsmini Bul)",
                "Bayraklardan ülke ismini bulun."),
            GameCategory(8,"Coğrafi Konuma Göre Ülke İsmi Bul",
                "Harita üzerinde ülkeler gösteriliyor, gösterilen ülkeyi bulun."),
            GameCategory(9,"Ülkenin Kıtasını Tahmin Et",
                "Ülkelerin bulunduğu kıtayı doğru tahmin edin. Her soru için 10 saniye. Kıtalar arasında 4 seçenek."),
            GameCategory(10,"Para Birimi Tahmini",
                "Ülkenin para birimini tahmin et. Her soru için 10 saniye. Bayraklar arasında 4 seçenek.")
        )
        categoryList.value = categories
    }

}