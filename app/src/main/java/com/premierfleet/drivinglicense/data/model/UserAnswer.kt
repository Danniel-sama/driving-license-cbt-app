package com.premierfleet.drivinglicense.data.model

data class UserAnswer(
    val questionId: Int,
    val selectedAnswer: String,
    val isCorrect: Boolean
)
