package com.abrebo.countryquiz.data.model

data class GameCategory(
    val id:Int,
    val title: String,
    val description: String,
    var highestScore: Int = 0
)
