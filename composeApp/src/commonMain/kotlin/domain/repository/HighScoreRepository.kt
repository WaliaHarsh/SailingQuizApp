package com.quizapp.sailing.domain.repository

interface HighScoreRepository {
    fun getHighScore(
        category: String,
        limit: Int,
    ): Int

    fun saveHighScore(
        category: String,
        limit: Int,
        score: Int,
    )
}
