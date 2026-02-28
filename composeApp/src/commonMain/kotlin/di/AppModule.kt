package com.quizapp.sailing.di

import com.quizapp.sailing.data.HighScoreRepositoryImpl
import com.quizapp.sailing.data.LocalQuizDataSource
import com.quizapp.sailing.data.QuizRepositoryImpl
import com.quizapp.sailing.domain.repository.HighScoreRepository
import com.quizapp.sailing.domain.repository.QuizRepository
import com.quizapp.sailing.domain.usecase.GetQuizUseCase
import com.quizapp.sailing.presentation.QuizViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single { LocalQuizDataSource() }
        single<QuizRepository> { QuizRepositoryImpl(get()) }
        single<HighScoreRepository> { HighScoreRepositoryImpl() }
        single { GetQuizUseCase(get()) }
        viewModelOf(::QuizViewModel)
    }
