package com.abrebo.countryquiz.data.model

data class PopulationQuestion(val countryName:String,
                              val population:String,
                              val options: List<String>) {
}