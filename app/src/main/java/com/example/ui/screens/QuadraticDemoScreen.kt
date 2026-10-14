package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionItem
import com.example.data.model.StudyLanguage
import com.example.data.repository.CurriculumRepository
import com.example.data.repository.FormulaSolvers
import com.example.data.repository.QuadraticResult
import com.example.ui.theme.*

@Composable
fun QuadraticDemoScreen(
    language: StudyLanguage = StudyLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview & Notes", "Interactive Solver", "Practice Qs", "Chapter Test", "Revision Plan")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Demonstration Header Banner
        Surface(
            color = AsiCyan.copy(alpha = 0.12f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(18.dp))
                Column {
                    Text(
                        text = "ASI CURRICULUM DEMONSTRATION MODULE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp),
                        color = AsiCyan
                    )
                    Text(
                        text = "Class 10 CBSE / BSEB Mathematics: Chapter 4 — Quadratic Equations",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Secondary Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = AsiCyan,
            edgePadding = 0.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) AsiCyan else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.testTag("demo_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> QuadraticNotesTab(language)
            1 -> QuadraticSolverTab()
            2 -> QuadraticPracticeTab(language)
            3 -> QuadraticTestTab(language)
            4 -> QuadraticRevisionPlanTab(language)
        }
    }
}

@Composable
fun QuadraticNotesTab(language: StudyLanguage) {
    val note = CurriculumRepository.quadraticSmartNote
    val isHindi = language == StudyLanguage.HINDI

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "1 मिनट द्रुत पुनरीक्षण (1-Minute Revision)" else "1-Minute High-Yield Flash",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi) note.hindiSummary else note.oneMinuteRevision,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "5-Minute Comprehensive Recap",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiAmber
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.fiveMinuteRevision,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Core Theoretical Derivation",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiEmerald
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.conceptualNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Common Board Exam Pitfalls & Misconceptions",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiRuby
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    note.commonMistakes.forEach { mistake ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("•", color = AsiRuby, fontWeight = FontWeight.Bold)
                            Text(mistake, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuadraticSolverTab() {
    var aText by remember { mutableStateOf("2") }
    var bText by remember { mutableStateOf("-7") }
    var cText by remember { mutableStateOf("3") }
    var result by remember { mutableStateOf<QuadraticResult?>(FormulaSolvers.solveQuadratic(2.0, -7.0, 3.0)) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Interactive Quadratic Engine & Root Verifier",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Text(
                        text = "Enter coefficients for ax² + bx + c = 0 to calculate Discriminant (D), examine nature of roots, and observe step-by-step algebraic deduction.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = aText,
                            onValueChange = { aText = it },
                            label = { Text("a (x²)") },
                            modifier = Modifier.weight(1f).testTag("input_a"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = bText,
                            onValueChange = { bText = it },
                            label = { Text("b (x)") },
                            modifier = Modifier.weight(1f).testTag("input_b"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = cText,
                            onValueChange = { cText = it },
                            label = { Text("c (const)") },
                            modifier = Modifier.weight(1f).testTag("input_c"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val a = aText.toDoubleOrNull() ?: 1.0
                            val b = bText.toDoubleOrNull() ?: 0.0
                            val c = cText.toDoubleOrNull() ?: 0.0
                            result = FormulaSolvers.solveQuadratic(a, b, c)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("solve_quadratic_button")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Calculate Discriminant & Roots", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        result?.let { res ->
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
                                text = "Nature of Roots",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AsiTextSecondaryDark
                            )
                            Surface(
                                color = if (res.isReal) AsiEmerald.copy(alpha = 0.2f) else AsiRuby.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = res.nature,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (res.isReal) AsiEmerald else AsiRuby,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Discriminant D = b² - 4ac = ${"%.2f".format(res.discriminant)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )

                        if (res.root1 != null && res.root2 != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Root 1 (x₁) = ${"%.3f".format(res.root1)}   |   Root 2 (x₂) = ${"%.3f".format(res.root2)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = AsiNavyBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Step-by-Step Algebraic Verification:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiTextSecondaryDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        res.steps.forEach { step ->
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuadraticPracticeTab(language: StudyLanguage) {
    val questions = CurriculumRepository.quadraticQuestions

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(questions) { q ->
            PracticeQuestionCard(question = q, language = language)
        }
    }
}

@Composable
fun PracticeQuestionCard(question: QuestionItem, language: StudyLanguage) {
    var expandedSolution by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = AsiCobalt.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${question.type.displayName} • ${question.marks} Mark",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCobalt,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = when (question.difficulty) {
                        "Basic" -> AsiEmerald.copy(alpha = 0.2f)
                        "Intermediate" -> AsiAmber.copy(alpha = 0.2f)
                        else -> AsiRuby.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = question.difficulty,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = when (question.difficulty) {
                            "Basic" -> AsiEmerald
                            "Intermediate" -> AsiAmber
                            else -> AsiRuby
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (language == StudyLanguage.HINDI && question.questionHindi.isNotBlank()) question.questionHindi else question.question,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (question.options.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                question.options.forEach { opt ->
                    val isChosen = selectedOption == opt
                    val isCorrect = opt == question.correctAnswer
                    val btnBg = when {
                        !isSubmitted -> if (isChosen) AsiNavyCardElevated else AsiNavyDarkest
                        isCorrect -> AsiEmerald.copy(alpha = 0.25f)
                        isChosen && !isCorrect -> AsiRuby.copy(alpha = 0.25f)
                        else -> AsiNavyDarkest
                    }

                    Surface(
                        color = btnBg,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (isChosen) AsiCyan else AsiNavyBorder
                            )
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                if (!isSubmitted) selectedOption = opt
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isChosen,
                                onClick = { if (!isSubmitted) selectedOption = opt },
                                colors = RadioButtonDefaults.colors(selectedColor = AsiCyan)
                            )
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (question.options.isNotEmpty() && !isSubmitted) {
                    Button(
                        onClick = { isSubmitted = true },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else if (isSubmitted) {
                    val correct = selectedOption == question.correctAnswer
                    Text(
                        text = if (correct) "✓ Correct Answer!" else "✗ Incorrect (Try reviewing steps)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (correct) AsiEmerald else AsiRuby
                    )
                }

                TextButton(
                    onClick = { expandedSolution = !expandedSolution }
                ) {
                    Text(
                        text = if (expandedSolution) "Hide Solution" else "View Step-by-Step",
                        color = AsiCyan,
                        fontSize = 12.sp
                    )
                    Icon(
                        imageVector = if (expandedSolution) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AsiCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            AnimatedVisibility(visible = expandedSolution) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(AsiNavyDarkest, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Correct Answer: ${question.correctAnswer}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiEmerald
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.stepByStepSolution,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (question.commonMisconceptions.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 Board Misconception Alert: ${question.commonMisconceptions}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiAmber
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuadraticTestTab(language: StudyLanguage) {
    val questions = CurriculumRepository.quadraticQuestions
    val userAnswers = remember { mutableStateMapOf<String, String>() }
    var testSubmitted by remember { mutableStateOf(false) }

    val score = remember(testSubmitted) {
        if (!testSubmitted) 0 else questions.count { q -> userAnswers[q.id] == q.correctAnswer }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Class 10 Chapter Mock Test (5 Questions)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = AsiCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Total: 8 Marks",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Test your grasp on Discriminant, middle-term splitting, and speed-stream word problems. Immediate score calculation & mistake analysis upon submission.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                                text = "🏆 Chapter Test Report",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "$score / ${questions.size} Correct (${(score * 100) / questions.size}%)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (score >= 4) AsiEmerald else AsiAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Automated Mistake Diagnosis:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AsiTextSecondaryDark
                        )

                        questions.forEach { q ->
                            val userAns = userAnswers[q.id]
                            val isCorrect = userAns == q.correctAnswer
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isCorrect) AsiEmerald else AsiRuby,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isCorrect) "${q.question.take(40)}... [CORRECT]" else "${q.question.take(40)}... (Your: ${userAns ?: "Unanswered"}, Expected: ${q.correctAnswer})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isCorrect) MaterialTheme.colorScheme.onSurface else AsiRuby
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                userAnswers.clear()
                                testSubmitted = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Retake Test", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        items(questions) { q ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    q.options.forEach { opt ->
                        val isChosen = userAnswers[q.id] == opt
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
                                .clickable {
                                    if (!testSubmitted) userAnswers[q.id] = opt
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = isChosen,
                                    onClick = { if (!testSubmitted) userAnswers[q.id] = opt },
                                    colors = RadioButtonDefaults.colors(selectedColor = AsiCyan)
                                )
                                Text(opt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }

        if (!testSubmitted) {
            item {
                Button(
                    onClick = {
                        testSubmitted = true
                        val finalScore = questions.count { q -> userAnswers[q.id] == q.correctAnswer }
                        com.example.data.repository.AppStudyRepository.recordTestScore("Quadratic Equations Chapter Test", finalScore, questions.size)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AsiEmerald, contentColor = AsiNavyDarkest),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag("submit_chapter_test_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Chapter Test & Calculate Score", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun QuadraticRevisionPlanTab(language: StudyLanguage) {
    val checklist = CurriculumRepository.quadraticSmartNote.checklist

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quadratic Equations — 7-Day Spaced Repetition Plan",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PlanDayRow(day = "Day 1 (Today)", task = "Complete Middle-term factorisation & Formula derivation", status = "Done", color = AsiEmerald)
                    PlanDayRow(day = "Day 3 (+48 hrs)", task = "Solve 4 Upstream/Downstream and Area word problems", status = "Due Soon", color = AsiAmber)
                    PlanDayRow(day = "Day 7 (+1 week)", task = "Re-attempt Discriminant D=0 condition for 'k' test", status = "Scheduled", color = AsiCobalt)
                    PlanDayRow(day = "Day 14 (+2 weeks)", task = "Full Class 10 Board Mock simulation paper", status = "Scheduled", color = AsiCobalt)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Exam Readiness Checklist",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiEmerald
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    checklist.forEach { item ->
                        var checked by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checked = !checked }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { checked = it },
                                colors = CheckboxDefaults.colors(checkedColor = AsiCyan)
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanDayRow(day: String, task: String, status: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(day, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AsiTextPrimaryDark)
            Text(task, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(
            color = color.copy(alpha = 0.18f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = color,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}
