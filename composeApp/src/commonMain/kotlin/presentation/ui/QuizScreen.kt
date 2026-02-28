package com.quizapp.sailing.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizapp.sailing.presentation.QuizState

@Composable
fun QuizScreen(
    state: QuizState,
    onStartQuiz: (Int, String) -> Unit,
    onOptionSelected: (Int) -> Unit,
    onUseLifeline: () -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            ),
                    ),
                ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
        ) {
            if (state is QuizState.Active) {
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    SailingProgressBar(
                        progress = (state.currentIndex + 1).toFloat() / state.totalQuestions,
                    )
                }
            }

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    val duration = 400
                    val easing = FastOutSlowInEasing
                    (
                        fadeIn(animationSpec = tween(duration, easing = easing)) +
                            slideInHorizontally(animationSpec = tween(duration, easing = easing), initialOffsetX = { it / 2 }) +
                            scaleIn(animationSpec = tween(duration, easing = easing), initialScale = 0.95f)
                    )
                        .togetherWith(
                            fadeOut(animationSpec = tween(duration, easing = easing)) +
                                slideOutHorizontally(animationSpec = tween(duration, easing = easing), targetOffsetX = { -it / 2 }) +
                                scaleOut(animationSpec = tween(duration, easing = easing), targetScale = 0.95f),
                        )
                },
                modifier = Modifier.fillMaxSize(),
                label = "MainContentTransition",
            ) { targetState ->
                when (targetState) {
                    is QuizState.Loading -> LoadingScreen()
                    is QuizState.Setup -> SetupScreen(targetState.categories, targetState.categoryStats, onStartQuiz)
                    is QuizState.Active ->
                        QuestionScreen(
                            state = targetState,
                            onOptionSelected = onOptionSelected,
                            onUseLifeline = onUseLifeline,
                            onNext = onNext,
                            onRestart = onRestart,
                        )
                    is QuizState.Finished ->
                        ResultsScreen(
                            state = targetState,
                            onRestart = onRestart,
                        )
                }
            }
        }
    }
}

@Composable
private fun SetupScreen(
    categories: List<String>,
    categoryStats: Map<String, Pair<Int, Map<Int, Int>>>,
    onStartQuiz: (Int, String) -> Unit,
) {
    var selectedCount by remember { mutableStateOf(10) }
    var selectedCategory by remember { mutableStateOf("All") }

    val totalAvailable = categoryStats[selectedCategory]?.first ?: 0
    val allowedCounts = listOf(5, 10, 20, 25).filter { it <= totalAvailable }

    LaunchedEffect(selectedCategory) {
        if (selectedCount !in allowedCounts) {
            selectedCount = allowedCounts.lastOrNull() ?: 5
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        // More compact header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Sailing,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sailing Master",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Knowledge is your compass",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
            )
        }

        // Compact Category Selection
        Column {
            Text(
                "Select Category",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { category ->
                            CategoryCard(
                                name = category,
                                isSelected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Compact Question Count Selection
        Column {
            Text(
                "Quiz Length",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                allowedCounts.forEach { count ->
                    val isSelected = selectedCount == count
                    val highScore = categoryStats[selectedCategory]?.second?.get(count) ?: 0

                    Surface(
                        modifier =
                            Modifier
                                .weight(1f)
                                .clickable { selectedCount = count },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else border(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        tonalElevation = 2.dp,
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            )
                            if (highScore > 0) {
                                Text(
                                    text = "Best: $highScore",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color =
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.onPrimary.copy(
                                                alpha = 0.8f,
                                            )
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        },
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onStartQuiz(selectedCount, selectedCategory) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        ) {
            Icon(Icons.Default.Anchor, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Begin Voyage", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CategoryCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon =
        when (name) {
            "All" -> Icons.Default.Explore
            "Seamanship & Basics" -> Icons.Default.DirectionsBoat
            "Sailing & Maneuvers" -> Icons.Default.WindPower
            "Navigation & Rules" -> Icons.Default.CompassCalibration
            "Safety & Knots" -> Icons.Default.HealthAndSafety
            else -> Icons.Default.Book
        }

    Surface(
        modifier =
            modifier
                .height(64.dp)
                .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border =
            if (isSelected) {
                border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                )
            } else {
                border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            },
        tonalElevation = if (isSelected) 2.dp else 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 2,
                modifier = Modifier.weight(1f),
                lineHeight = 14.sp,
            )
        }
    }
}

@Composable
private fun QuestionScreen(
    state: QuizState.Active,
    onOptionSelected: (Int) -> Unit,
    onUseLifeline: () -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val hasAnswered = state.selectedOptionIndex != null
    val isCorrect = hasAnswered && state.selectedOptionIndex == state.question.correctOptionIndex

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Label,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = state.question.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            if (state.streak > 0) {
                Surface(
                    color = Color(0xFFFF9800).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = border(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Whatshot, null, modifier = Modifier.size(16.dp), tint = Color(0xFFFF9800))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.streak}",
                            color = Color(0xFFFF9800),
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }

            Text(
                text = "${state.currentIndex + 1}/${state.totalQuestions}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = onRestart,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart Quiz",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Text(
                text = state.question.text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(32.dp),
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!hasAnswered) {
            OutlinedIconButton(
                onClick = onUseLifeline,
                enabled = state.isLifelineAvailable,
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors =
                    IconButtonDefaults.outlinedIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = Color.Gray,
                    ),
            ) {
                Icon(Icons.Default.AutoFixHigh, "50/50 Lifeline")
            }
            Text("50/50", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.question.options.forEachIndexed { index, option ->
            if (index !in state.hiddenOptionIndices) {
                val isSelected = state.selectedOptionIndex == index
                val isOptionCorrect = index == state.question.correctOptionIndex

                val buttonColor =
                    when {
                        hasAnswered && isOptionCorrect ->
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White,
                            )
                        hasAnswered && isSelected && !isCorrect ->
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            )
                        isSelected ->
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        else -> ButtonDefaults.outlinedButtonColors()
                    }

                OutlinedButton(
                    onClick = { if (!hasAnswered) onOptionSelected(index) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    border =
                        if (isSelected || (hasAnswered && isOptionCorrect)) {
                            null
                        } else {
                            border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                            )
                        },
                    colors = buttonColor,
                    elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = option,
                            fontSize = 17.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                        if (hasAnswered && isOptionCorrect) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        } else if (hasAnswered && isSelected && !isCorrect) {
                            Icon(Icons.Default.Cancel, null, tint = Color.White)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = hasAnswered,
            enter = expandVertically() + fadeIn(),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                ) {
                    val text = if (state.currentIndex < state.totalQuestions - 1) "Next Question" else "Complete Quiz"
                    Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SailingProgressBar(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "ProgressBarAnimation",
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(44.dp)) {
            val availableWidth = maxWidth - 32.dp
            Icon(
                imageVector = Icons.Default.Sailing,
                contentDescription = null,
                modifier = Modifier.offset(x = availableWidth * animatedProgress).size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
private fun ResultsScreen(
    state: QuizState.Finished,
    onRestart: () -> Unit,
) {
    val percentage = (state.score.toFloat() / state.totalQuestions * 100).toInt()
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Icon(
            imageVector =
                when {
                    percentage >= 90 -> Icons.Default.EmojiEvents
                    percentage >= 70 -> Icons.Default.Anchor
                    else -> Icons.Default.DirectionsBoat
                },
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text =
                when {
                    percentage >= 90 -> "Master Mariner!"
                    percentage >= 70 -> "Skilled Sailor!"
                    else -> "Novice Deckhand"
                },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(200.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                strokeWidth = 16.dp,
                strokeCap = StrokeCap.Round,
            )
            CircularProgressIndicator(
                progress = { state.score.toFloat() / state.totalQuestions },
                modifier = Modifier.size(200.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 16.dp,
                strokeCap = StrokeCap.Round,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$percentage%", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
                Text("${state.score} of ${state.totalQuestions} correct", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Icon(Icons.Default.Refresh, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Plan New Voyage", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun border(
    width: androidx.compose.ui.unit.Dp,
    color: Color,
) = androidx.compose.foundation.BorderStroke(width, color)
