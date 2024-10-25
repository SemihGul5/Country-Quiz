package com.abrebo.countryquiz.data.model

data class FlagQuestion(
    val flagDrawable: Int,
    val correctAnswer: String,
    val options: List<Any>
)
