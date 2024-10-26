package com.abrebo.countryquiz.data.model

data class FlagQuestion(
    val flagDrawable: Int,
    val countryName: String,
    val capital:String,
    val population:String,
    val continent:String,
    val options: List<Any>
)
