package com.quizapp.sailing.presentation

import com.quizapp.sailing.domain.models.Question

sealed class QuizState {
    object Loading : QuizState()

    data class Setup(
        val categories: List<String>,
        // Category -> (TotalCount, Map<Limit, HighScore>)
        val categoryStats: Map<String, Pair<Int, Map<Int, Int>>>,
    ) : QuizState()

    data class Active(
        val question: Question,
        val currentIndex: Int,
        val totalQuestions: Int,
        val selectedOptionIndex: Int? = null,
        val score: Int,
        val streak: Int = 0,
        val isLifelineAvailable: Boolean = true,
        val hiddenOptionIndices: List<Int> = emptyList(),
    ) : QuizState()

    data class Finished(
        val score: Int,
        val totalQuestions: Int,
    ) : QuizState()
}
