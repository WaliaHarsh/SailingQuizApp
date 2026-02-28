package com.quizapp.sailing.data

import com.quizapp.sailing.domain.models.Quiz
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import quizapp.composeapp.generated.resources.Res

class LocalQuizDataSource {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun getQuiz(): Quiz {
        val bytes = Res.readBytes("files/sailing_quiz.json")
        val jsonString = bytes.decodeToString()
        return Json.decodeFromString<Quiz>(jsonString)
    }
}
