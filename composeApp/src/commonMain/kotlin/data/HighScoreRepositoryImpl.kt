package com.quizapp.sailing.data

import com.quizapp.sailing.domain.repository.HighScoreRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get

class HighScoreRepositoryImpl(
    private val settings: Settings = Settings(),
) : HighScoreRepository {
    override fun getHighScore(
        category: String,
        limit: Int,
    ): Int {
        return settings.getInt(getKey(category, limit), 0)
    }

    override fun saveHighScore(
        category: String,
        limit: Int,
        score: Int,
    ) {
        val currentHighScore = getHighScore(category, limit)
        if (score > currentHighScore) {
            settings.putInt(getKey(category, limit), score)
        }
    }

    private fun getKey(
        category: String,
        limit: Int,
    ): String {
        return "high_score_${category.replace(" ", "_")}_$limit"
    }
}
