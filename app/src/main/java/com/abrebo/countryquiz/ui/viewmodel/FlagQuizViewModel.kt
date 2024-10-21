package com.abrebo.countryquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.FlagQuestion
import com.abrebo.countryquiz.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FlagQuizViewModel @Inject constructor (var repository: Repository,
                                             application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _currentQuestion = MutableLiveData<FlagQuestion>()
    val currentQuestion: LiveData<FlagQuestion> get() = _currentQuestion
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score
    private val questions = mutableListOf<FlagQuestion>()
    private var questionIndex = 0

    init {
        _score.value = 0
        prepareQuestions()
        nextQuestion()
    }

    private fun prepareQuestions() {
        val allCountries = repository.getAllCountries() ?: return
        if (allCountries.isEmpty()) {
            Log.e("FlagQuizViewModel", "AllCountries list is empty!")
            return
        }

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

    fun nextQuestion() {
        if (questions.isEmpty()) {
            Log.e("FlagQuizViewModel", "Sorular listesi boş!")
            return
        }

        if (questionIndex < questions.size) {
            _currentQuestion.value = questions[questionIndex]
            questionIndex++
        } else {
            resetGame()
        }
    }
    fun getAllCountries():List<FlagQuestion>{
        return repository.getAllCountries()
    }

    fun checkAnswer(selectedDrawable: Int) {
        if (selectedDrawable == _currentQuestion.value?.flagDrawable) {
            _score.value = _score.value?.plus(1)
        }
        nextQuestion()
    }

    private fun resetGame() {
        questionIndex = 0
        _score.value = 0
        prepareQuestions()
        nextQuestion()
    }
}