package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.FlagQuestion
import com.abrebo.countryquiz.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FlagQuizViewModel @Inject constructor (var repository: Repository):ViewModel(){

    private val _currentQuestion = MutableLiveData<FlagQuestion>()
    val currentQuestion: LiveData<FlagQuestion> get() = _currentQuestion
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score
    private val questions = mutableListOf<FlagQuestion>()
    private var questionIndex = 0
    private val _highestScore = MutableLiveData<Int>()
    val highestScore: LiveData<Int> get() = _highestScore
    init {
        _score.value = 0
    }

    fun prepareQuestionsGame1() {
        val allCountries = repository.getAllCountries() ?: return
        val shuffledCountries = allCountries.shuffled()

        shuffledCountries.forEach { correctCountry ->
            val options = allCountries
                .filter { it.correctAnswer != correctCountry.correctAnswer }
                .shuffled()
                .take(3)
                .map { it.flagDrawable }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, correctCountry.flagDrawable)
            val question = FlagQuestion(
                flagDrawable = correctCountry.flagDrawable,
                correctAnswer = correctCountry.correctAnswer,
                options = options
            )
            questions.add(question)
        }
    }
    fun prepareQuestionsGame2() {
        val allCountries = repository.getAllCountries() ?: return
        val shuffledCountries = allCountries.shuffled()

        shuffledCountries.forEach { correctCountry ->
            val options = allCountries
                .filter { it.correctAnswer != correctCountry.correctAnswer }
                .shuffled()
                .take(3)
                .map { it.correctAnswer }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, correctCountry.correctAnswer)

            val question = FlagQuestion(
                flagDrawable = correctCountry.flagDrawable,
                correctAnswer = correctCountry.correctAnswer,
                options = options
            )
            questions.add(question)
        }
    }
    fun nextQuestion(id:Int) {
        if (questionIndex < questions.size) {
            _currentQuestion.value = questions[questionIndex]
            questionIndex++
        } else {
            resetGame(id)
        }
    }

    fun getAllCountries():List<FlagQuestion>{
        return repository.getAllCountries()
    }

    fun checkAnswer(selectedDrawable: Int): Boolean {
        return if (selectedDrawable == _currentQuestion.value?.flagDrawable) {
            _score.value = _score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun checkAnswer(selectedCountryName: String): Boolean {
        return if (selectedCountryName == _currentQuestion.value?.correctAnswer) {
            _score.value = _score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun updateScore(newScore: Int, userId: String) {
        _score.value = newScore
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userId)
            _highestScore.value = currentHighestScore

            if (newScore >= currentHighestScore) {
                repository.saveHighestScore(userId, newScore)
            }
        }
    }
    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }
    private fun resetGame(id:Int) {
        questionIndex = 0
        _score.value = 0
        if (id==1||id==6){
            prepareQuestionsGame1()
        }else if (id==2){
            prepareQuestionsGame2()
        }
        nextQuestion(id)
    }

}