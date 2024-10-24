package com.abrebo.countryquiz.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.abrebo.countryquiz.data.model.PopulationQuestion
import com.abrebo.countryquiz.data.repo.CountriesPopulationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PopulationQuizViewModel @Inject constructor(
    var repository: CountriesPopulationRepository,
    application: Application
) : AndroidViewModel(application) {
    val currentQuestion = MutableLiveData<PopulationQuestion>()
    val score = MutableLiveData<Int>()
    private var questionIndex = 0
    private val questions = mutableListOf<PopulationQuestion>()

    init {
        score.value = 0
        prepareQuestions()
        nextQuestion()
    }

    private fun prepareQuestions() {
        val allCountries = repository.getAllCountriesPopulation()
        allCountries.shuffled().forEach { country ->
            val options = allCountries
                .filter { it != country }
                .shuffled()
                .take(3)
                .map { it.population }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, country.population)

            questions.add(
                PopulationQuestion(
                    countryName = country.countryName,
                    population = country.population,
                    options = options
                )
            )
        }
    }

    fun nextQuestion() {
        if (questionIndex < questions.size) {
            currentQuestion.value = questions[questionIndex]
            questionIndex++
        } else {
            resetGame()
        }
    }

    fun checkAnswer(selectedAnswer: String): Boolean {
        return if (selectedAnswer == currentQuestion.value?.population) {
            score.value = score.value?.plus(1)
            true
        } else {
            false
        }
    }

    private fun resetGame() {
        questionIndex = 0
        score.value = 0
        prepareQuestions()
        nextQuestion()
    }
}
