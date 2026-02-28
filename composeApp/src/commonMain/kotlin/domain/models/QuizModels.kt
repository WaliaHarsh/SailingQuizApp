package com.quizapp.sailing.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String = "",
    val category: String = "General",
)

@Serializable
data class Quiz(
    val title: String,
    val questions: List<Question>,
)
