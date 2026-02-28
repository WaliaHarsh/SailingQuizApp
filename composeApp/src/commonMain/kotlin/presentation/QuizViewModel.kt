package com.quizapp.sailing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizapp.sailing.domain.models.Question
import com.quizapp.sailing.domain.repository.HighScoreRepository
import com.quizapp.sailing.domain.usecase.GetQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val highScoreRepository: HighScoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<QuizState>(QuizState.Loading)
    val uiState: StateFlow<QuizState> = _uiState.asStateFlow()

    private var allQuestions: List<Question> = emptyList()
    private var activeQuestions: List<Question> = emptyList()
    private var currentIndex = 0
    private var currentScore = 0
    private var currentStreak = 0
    private var isLifelineUsed = false
    private var selectedCategory = "All"
    private var selectedLimit = 10

    init {
        loadQuiz()
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            try {
                val quiz = getQuizUseCase()
                allQuestions = quiz.questions
                updateSetupState()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun startQuiz(
        limit: Int,
        category: String,
    ) {
        selectedCategory = category
        selectedLimit = limit

        val filtered =
            if (category == "All") {
                allQuestions
            } else {
                allQuestions.filter { it.category == category }
            }

        activeQuestions =
            filtered.shuffled().take(limit).map { question ->
                val options = question.options
                val correctOptionText = options[question.correctOptionIndex]
                val shuffledOptions = options.shuffled()
                val newCorrectIndex = shuffledOptions.indexOf(correctOptionText)
                question.copy(options = shuffledOptions, correctOptionIndex = newCorrectIndex)
            }

        currentIndex = 0
        currentScore = 0
        currentStreak = 0
        isLifelineUsed = false
        updateActiveState()
    }

    fun useLifeline() {
        val currentState = _uiState.value as? QuizState.Active ?: return
        if (!currentState.isLifelineAvailable || isLifelineUsed) return

        val correctIndex = currentState.question.correctOptionIndex
        val incorrectIndices = currentState.question.options.indices.filter { it != correctIndex }
        val hiddenIndices = incorrectIndices.shuffled().take(2)

        isLifelineUsed = true
        _uiState.value =
            currentState.copy(
                hiddenOptionIndices = hiddenIndices,
                isLifelineAvailable = false,
            )
    }

    fun onOptionSelected(index: Int) {
        val currentState = _uiState.value as? QuizState.Active ?: return
        if (currentState.selectedOptionIndex != null) return
        _uiState.value = currentState.copy(selectedOptionIndex = index)
    }

    fun nextQuestion() {
        val currentState = _uiState.value as? QuizState.Active ?: return
        val selectedIndex = currentState.selectedOptionIndex ?: return

        if (selectedIndex == activeQuestions[currentIndex].correctOptionIndex) {
            currentScore++
            currentStreak++
        } else {
            currentStreak = 0
        }

        if (currentIndex < activeQuestions.size - 1) {
            currentIndex++
            updateActiveState()
        } else {
            highScoreRepository.saveHighScore(selectedCategory, activeQuestions.size, currentScore)
            _uiState.value = QuizState.Finished(currentScore, activeQuestions.size)
        }
    }

    fun restartQuiz() {
        updateSetupState()
    }

    private fun updateSetupState() {
        val categories = listOf("All") + allQuestions.map { it.category }.distinct()
        val categoryStats =
            categories.associateWith { cat ->
                val count = if (cat == "All") allQuestions.size else allQuestions.count { it.category == cat }
                val highScores =
                    listOf(5, 10, 20, 25).associateWith { limit ->
                        highScoreRepository.getHighScore(cat, limit)
                    }
                count to highScores
            }
        _uiState.value = QuizState.Setup(categories, categoryStats)
    }

    private fun updateActiveState() {
        if (activeQuestions.isEmpty()) return
        val question = activeQuestions[currentIndex]
        isLifelineUsed = false // Reset for every question
        _uiState.value =
            QuizState.Active(
                question = question,
                currentIndex = currentIndex,
                totalQuestions = activeQuestions.size,
                score = currentScore,
                streak = currentStreak,
                // Always available initially for a new question
                isLifelineAvailable = true,
            )
    }
}
