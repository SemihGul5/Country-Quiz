package com.abrebo.countryquiz.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.data.model.FlagQuestion
import com.abrebo.countryquiz.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class Game2ViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

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

    fun nextQuestion() {
        if (questionIndex < questions.size) {
            _currentQuestion.value = questions[questionIndex]
            questionIndex++
        } else {
            resetGame()
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

    private fun resetGame() {
        questionIndex = 0
        _score.value = 0
        prepareQuestions()
        nextQuestion()
    }
}
