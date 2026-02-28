package com.quizapp.sailing.domain.usecase

import com.quizapp.sailing.domain.models.Quiz
import com.quizapp.sailing.domain.repository.QuizRepository

class GetQuizUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(): Quiz {
        return repository.getQuiz()
    }
}
