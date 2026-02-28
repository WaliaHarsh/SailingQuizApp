package com.quizapp.sailing.data

import com.quizapp.sailing.domain.models.Quiz
import com.quizapp.sailing.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val dataSource: LocalQuizDataSource,
) : QuizRepository {
    override suspend fun getQuiz(): Quiz {
        return dataSource.getQuiz()
    }
}
