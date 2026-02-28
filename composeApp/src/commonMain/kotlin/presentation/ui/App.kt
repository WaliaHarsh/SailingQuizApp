package com.quizapp.sailing.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.quizapp.sailing.presentation.QuizViewModel
import com.quizapp.sailing.presentation.ui.theme.SailingQuizTheme
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinContext {
        SailingQuizTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                val viewModel = koinViewModel<QuizViewModel>()
                val state by viewModel.uiState.collectAsState()

                QuizScreen(
                    state = state,
                    onStartQuiz = { count, category -> viewModel.startQuiz(count, category) },
                    onOptionSelected = { viewModel.onOptionSelected(it) },
                    onUseLifeline = { viewModel.useLifeline() },
                    onNext = { viewModel.nextQuestion() },
                    onRestart = { viewModel.restartQuiz() },
                )
            }
        }
    }
}
