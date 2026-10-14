package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyLanguage
import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ExamLabScreen(
    language: StudyLanguage = StudyLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var examType by remember { mutableStateOf("Board Exam Simulation") }
    val examTypes = listOf("Board Exam Simulation", "Half-Yearly Mock", "Final Pre-Board", "Chapter Mastery")

    var isExamRunning by remember { mutableStateOf(false) }
    var testSubmitted by remember { mutableStateOf(false) }

    val currentExam = remember(examType) {
        CurriculumRepository.getExamForType(examType)
    }

    var remainingSeconds by remember(currentExam) {
        mutableIntStateOf(currentExam.durationMinutes * 60)
    }

    val questions = currentExam.questions
    val studentAnswers = remember { mutableStateMapOf<String, String>() }

    // Countdown Timer Effect
    LaunchedEffect(isExamRunning) {
        if (isExamRunning) {
            while (remainingSeconds > 0 && isExamRunning) {
                delay(1000)
                remainingSeconds--
            }
            if (remainingSeconds == 0) {
                testSubmitted = true
                isExamRunning = false
                val finalScore = questions.count { q -> studentAnswers[q.id] == q.correctAnswer }
                AppStudyRepository.recordTestScore(currentExam.title, finalScore, questions.size)
            }
        }
    }

    val score = remember(testSubmitted) {
        if (!testSubmitted) 0 else questions.count { q -> studentAnswers[q.id] == q.correctAnswer }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Exam Lab Header
        Surface(
            color = AsiCobalt.copy(alpha = 0.15f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CBSE & BOARD EXAMINATION LAB",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp),
                        color = AsiCyan
                    )
                    Text(
                        text = "Timed Mock Papers, Scoring Guidance & Post-Exam Plans",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (isExamRunning) {
                    val mins = remainingSeconds / 60
                    val secs = remainingSeconds % 60
                    Surface(
                        color = AsiRuby.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⏱ %02d:%02d".format(mins, secs),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiRuby,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Exam Type Selector Chips
        if (!isExamRunning && !testSubmitted) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(examTypes) { type ->
                    val isSel = examType == type
                    Surface(
                        color = if (isSel) AsiCyan else AsiNavyCard,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable {
                            examType = type
                            studentAnswers.clear()
                        }
                    ) {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (!isExamRunning && !testSubmitted) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Exam Configuration & Blueprint",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("• Pattern: ${currentExam.title}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("• Class: Class ${currentExam.classCode} | Board: ${currentExam.board}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("• Question Count: ${questions.size} Questions (MCQ, Assertion-Reason, Numerical, Case-Based)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("• Time Allowance: ${currentExam.durationMinutes} Minutes | Total Marks: ${currentExam.totalMarks} Marks", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    isExamRunning = true
                                    remainingSeconds = currentExam.durationMinutes * 60
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("start_exam_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Start Timed Exam Simulation", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (testSubmitted) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AsiNavyCardElevated)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📊 Post-Examination Assessment Report",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Surface(
                                    color = if (score >= 4) AsiEmerald.copy(alpha = 0.2f) else AsiAmber.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "$score / ${questions.size} Marks (${(score * 100) / questions.size}%)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (score >= 4) AsiEmerald else AsiAmber,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Personalized Post-Test Improvement Plan:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("1. Focus on negative sign handling in quadratic formula: -(-b) = +b.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("2. Practice Upstream/Downstream boat questions before next mock.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("3. Revise Discriminant boundary conditions (D = 0 for equal roots).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    testSubmitted = false
                                    isExamRunning = false
                                    studentAnswers.clear()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Configure New Examination Mock", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (isExamRunning) {
                items(questions) { q ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${q.type.displayName} • ${q.marks} Mark",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AsiCobalt
                                )
                                Text(q.difficulty, style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = q.question,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            q.options.forEach { opt ->
                                val isChosen = studentAnswers[q.id] == opt
                                Surface(
                                    color = if (isChosen) AsiNavyCardElevated else AsiNavyDarkest,
                                    shape = RoundedCornerShape(8.dp),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(
                                            if (isChosen) AsiCyan else AsiNavyBorder
                                        )
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clickable { studentAnswers[q.id] = opt }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = isChosen,
                                            onClick = { studentAnswers[q.id] = opt },
                                            colors = RadioButtonDefaults.colors(selectedColor = AsiCyan)
                                        )
                                        Text(opt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            isExamRunning = false
                            testSubmitted = true
                            val finalScore = questions.count { q -> studentAnswers[q.id] == q.correctAnswer }
                            AppStudyRepository.recordTestScore(currentExam.title, finalScore, questions.size)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AsiEmerald, contentColor = AsiNavyDarkest),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_exam_button")
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Submit Exam Paper & Generate Marking Scheme", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
