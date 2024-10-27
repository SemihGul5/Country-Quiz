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
import com.abrebo.countryquiz.data.model.PopulationQuestion
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
                .filter { it.countryName != correctCountry.countryName }
                .shuffled()
                .take(3)
                .map { it.flagDrawable }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, correctCountry.flagDrawable)
            val question = FlagQuestion(
                flagDrawable = correctCountry.flagDrawable,
                countryName = correctCountry.countryName,
                mapDrawable = correctCountry.mapDrawable,
                population = correctCountry.population,
                capital = correctCountry.capital,
                continent = correctCountry.continent,
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
                .filter { it.countryName != correctCountry.countryName }
                .shuffled()
                .take(3)
                .map { it.countryName }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, correctCountry.countryName)

            val question = FlagQuestion(
                flagDrawable = correctCountry.flagDrawable,
                countryName = correctCountry.countryName,
                population = correctCountry.population,
                mapDrawable = correctCountry.mapDrawable,
                capital = correctCountry.capital,
                continent = correctCountry.continent,
                options = options
            )
            questions.add(question)
        }
    }
    fun prepareQuestionsGame4Population() {
        val allCountries = repository.getAllCountries()
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
                FlagQuestion(
                    countryName = country.countryName,
                    population = country.population,
                    capital = country.capital,
                    mapDrawable = country.mapDrawable,
                    flagDrawable = country.flagDrawable,
                    continent = country.continent,
                    options = options
                )
            )
        }
    }
    fun prepareQuestionsGame3Capital() {
        val allCountries = repository.getAllCountries()
        allCountries.shuffled().forEach { country ->
            val options = allCountries
                .filter { it != country }
                .shuffled()
                .take(3)
                .map { it.capital }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, country.capital)

            questions.add(
                FlagQuestion(
                    countryName = country.countryName,
                    population = country.population,
                    mapDrawable = country.mapDrawable,
                    capital = country.capital,
                    flagDrawable = country.flagDrawable,
                    continent = country.continent,
                    options = options
                )
            )
        }
    }
    fun prepareQuestionsGame7Map() {
        val allCountries = repository.getAllCountries() ?: return
        val shuffledCountries = allCountries.shuffled()

        shuffledCountries.forEach { correctCountry ->
            val options = allCountries
                .filter { it.countryName != correctCountry.countryName && it.continent == correctCountry.continent }
                .shuffled()
                .take(3)
                .map { it.countryName }
                .toMutableList()

            if (options.size < 3) {
                return@forEach
            }
            val correctAnswerPosition = Random.nextInt(options.size + 1)
            options.add(correctAnswerPosition, correctCountry.countryName)

            val question = FlagQuestion(
                flagDrawable = correctCountry.flagDrawable,
                countryName = correctCountry.countryName,
                population = correctCountry.population,
                mapDrawable = correctCountry.mapDrawable,
                capital = correctCountry.capital,
                continent = correctCountry.continent,
                options = options
            )
            questions.add(question)
        }
    }

    fun prepareQuestionsGame8Continent() {
        val allCountries = repository.getAllCountries()
        allCountries.shuffled().forEach { country ->
            val currentContinent = country.continent
            val continentOptions = allCountries
                .filter { it.continent != currentContinent }
                .map { it.continent }
                .distinct()
            val options = continentOptions.shuffled().take(3).toMutableList()
            options.add(Random.nextInt(4), currentContinent)

            questions.add(
                FlagQuestion(
                    countryName = country.countryName,
                    population = country.population,
                    capital = country.capital,
                    mapDrawable = country.mapDrawable,
                    flagDrawable = country.flagDrawable,
                    continent = country.continent,
                    options = options
                )
            )
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
        return if (selectedCountryName == _currentQuestion.value?.countryName) {
            _score.value = _score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun checkAnswerGame4Population(selectedAnswer: String): Boolean {
        return if (selectedAnswer == currentQuestion.value?.population) {
            _score.value = score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun checkAnswerGame3Capital(selectedAnswer: String): Boolean {
        return if (selectedAnswer == currentQuestion.value?.capital) {
            _score.value = score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun checkAnswerGame8Continent(selectedAnswer: String): Boolean {
        return if (selectedAnswer == currentQuestion.value?.continent) {
            _score.value = score.value?.plus(1)
            true
        } else {
            false
        }
    }
    fun updateScore(newScore: Int, userId: String,game:Int) {
        _score.value = newScore
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userId,game)
            _highestScore.value = currentHighestScore

            if (newScore >= currentHighestScore) {
                repository.saveHighestScore(userId, newScore,game)
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
        when (id) {
            1, 6 -> {
                prepareQuestionsGame1()
            }
            2 -> {
                prepareQuestionsGame2()
            }
            3->{
                prepareQuestionsGame3Capital()
            }
            4 -> {
                prepareQuestionsGame4Population()
            }
            7->{
                prepareQuestionsGame7Map()
            }
            8->{
                prepareQuestionsGame8Continent()
            }
        }
        nextQuestion(id)
    }

}