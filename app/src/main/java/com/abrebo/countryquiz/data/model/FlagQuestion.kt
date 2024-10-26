package com.abrebo.countryquiz.data.model

data class FlagQuestion(
    val flagDrawable: Int,
    val countryName: String,
    val population:String,
    val capital:String,
    val options: List<Any>
)
