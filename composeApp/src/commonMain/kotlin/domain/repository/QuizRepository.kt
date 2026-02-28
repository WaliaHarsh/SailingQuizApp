package com.quizapp.sailing.domain.repository

import com.quizapp.sailing.domain.models.Quiz

interface QuizRepository {
    suspend fun getQuiz(): Quiz
}
