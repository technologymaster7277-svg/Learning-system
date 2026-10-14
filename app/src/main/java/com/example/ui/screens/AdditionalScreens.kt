package com.example.ui.screens

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.data.model.*
import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.ui.theme.*

// 1. REVISION PLANNER SCREEN
@Composable
fun RevisionPlannerScreen(
    modifier: Modifier = Modifier
) {
    val revisionList by AppStudyRepository.revisionItems.collectAsStateWithLifecycle()
    var newTopicText by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Surface(
            color = AsiCyan.copy(alpha = 0.15f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("ASI ADAPTIVE SPACED REPETITION ENGINE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                    Text("Retain 92% more concepts through Ebbinghaus forgetting curve scheduling.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add Topic", tint = AsiCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(revisionList) { record ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (record.isDone) AsiNavyDarkest else AsiNavyCard),
                    modifier = Modifier.clickable { AppStudyRepository.toggleRevisionDone(record.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = record.isDone,
                                onCheckedChange = { AppStudyRepository.toggleRevisionDone(record.id) },
                                colors = CheckboxDefaults.colors(checkedColor = AsiCyan)
                            )
                            Column {
                                Text(
                                    record.topic,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (record.isDone) AsiTextSecondaryDark else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(record.chapter, style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                            }
                        }

                        Surface(
                            color = when {
                                record.isDone -> AsiEmerald.copy(alpha = 0.2f)
                                record.dueDate.contains("Today") -> AsiAmber.copy(alpha = 0.2f)
                                else -> AsiNavyCardElevated
                            },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                if (record.isDone) "Completed ✓" else record.dueDate,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = when {
                                    record.isDone -> AsiEmerald
                                    record.dueDate.contains("Today") -> AsiAmber
                                    else -> AsiCyan
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = AsiNavyCardElevated,
            title = { Text("Schedule Topic for Revision", color = AsiCyan, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newTopicText,
                    onValueChange = { newTopicText = it },
                    label = { Text("Topic Name") },
                    placeholder = { Text("e.g. Lens Formula Ray Diagrams") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTopicText.isNotBlank()) {
                            AppStudyRepository.addRevisionItem(newTopicText.trim(), "Custom Goal", "Due in 3 Days")
                            newTopicText = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest)
                ) {
                    Text("Add to Schedule", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = AsiTextSecondaryDark)
                }
            }
        )
    }
}

// 2. MY PROGRESS & ANALYTICS SCREEN
@Composable
fun ProgressScreen(
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    val testScores by AppStudyRepository.testScores.collectAsStateWithLifecycle()
    val averageScore = remember(testScores) {
        if (testScores.isEmpty()) 84 else testScores.map { it.percentage }.average().toInt()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Overall Learning Intelligence Score", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("$averageScore%", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text("Exam Preparedness Index (CBSE Standard)", style = MaterialTheme.typography.labelSmall, color = AsiEmerald)
                        }
                        Surface(
                            color = if (averageScore >= 75) AsiEmerald.copy(alpha = 0.2f) else AsiAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (averageScore >= 75) "EXAM READY" else "NEEDS REVISION",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (averageScore >= 75) AsiEmerald else AsiAmber,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Recent Examination & Mock Attempts", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCobalt)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (testScores.isEmpty()) {
                        Text("No tests taken yet. Launch an exam from the Exam Lab to record scores.", style = MaterialTheme.typography.bodySmall, color = AsiTextSecondaryDark)
                    } else {
                        testScores.forEach { test ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(test.testTitle, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
                                    Text("${test.score} of ${test.totalQuestions} Questions Correct", style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                                }
                                Surface(
                                    color = if (test.percentage >= 70) AsiEmerald.copy(alpha = 0.2f) else AsiAmber.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        "${test.percentage}%",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (test.percentage >= 70) AsiEmerald else AsiAmber,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Subject Mastery Estimates", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiAmber)
                    Spacer(modifier = Modifier.height(12.dp))

                    SubjectProgressBar("Mathematics (Standard)", 0.88f, "88% Mastery", AsiCyan)
                    SubjectProgressBar("Science (Physics & Chemistry)", 0.79f, "79% Mastery", AsiCobalt)
                    SubjectProgressBar("English Language & Lit", 0.85f, "85% Mastery", AsiEmerald)
                    SubjectProgressBar("Social Science", 0.72f, "72% Mastery", AsiAmber)
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCardElevated)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Study Activity & Consistency", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Current Streak: ${userProfile.studyStreakDays} Consecutive Days 🔥", style = MaterialTheme.typography.bodySmall, color = AsiTextPrimaryDark)
                    Text("• Questions Attempted: ${userProfile.totalQuestionsAttempted} Questions", style = MaterialTheme.typography.bodySmall, color = AsiTextPrimaryDark)
                    Text("• Cumulative Study Time: ${userProfile.studyHoursTotal} Hours", style = MaterialTheme.typography.bodySmall, color = AsiTextPrimaryDark)
                }
            }
        }
    }
}

@Composable
fun SubjectProgressBar(subject: String, progress: Float, label: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(subject, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
            Text(label, style = MaterialTheme.typography.labelSmall, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color = color,
            trackColor = AsiNavyBorder
        )
    }
}

// 3. SAVED MATERIALS & BOOKMARKS SCREEN
@Composable
fun SavedMaterialsScreen(
    modifier: Modifier = Modifier
) {
    val bookmarkedFormulaIds by AppStudyRepository.bookmarkedFormulaIds.collectAsStateWithLifecycle()
    val savedNoteIds by AppStudyRepository.savedNoteIds.collectAsStateWithLifecycle()
    val savedBookPackages by AppStudyRepository.savedBookPackages.collectAsStateWithLifecycle()

    val bookmarkedFormulas = CurriculumRepository.allFormulas.filter { bookmarkedFormulaIds.contains(it.id) }
    val savedNotes = CurriculumRepository.allSmartNotes.filter { savedNoteIds.contains(it.id) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        // Saved Book Intelligence Packages
        item {
            Text("Saved Book Intelligence Packages (${savedBookPackages.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
        }

        if (savedBookPackages.isEmpty()) {
            item {
                Text("No book study packages saved yet. Open Book Intelligence to analyze and save textbook packages.", style = MaterialTheme.typography.bodySmall, color = AsiTextSecondaryDark)
            }
        } else {
            items(savedBookPackages) { pkg ->
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pkg.documentTitle, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                                Text("${pkg.academicClass.displayName} • ${pkg.subject.displayName} • ${pkg.chaptersIdentified.firstOrNull()?.title ?: "Chapter"}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { AppStudyRepository.removeBookPackage(pkg.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = AsiRuby, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(pkg.shortNotes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = AsiEmerald.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                Text("${pkg.extractedFormulas.size} Formulas", fontSize = 10.sp, color = AsiEmerald, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Surface(color = AsiCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                Text("${pkg.generatedQuestions.size} Practice Qs", fontSize = 10.sp, color = AsiCyan, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Bookmarked Formulas (${bookmarkedFormulas.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
            }
        }

        if (bookmarkedFormulas.isEmpty()) {
            item {
                Text("No formulas bookmarked yet. Tap the bookmark icon in Formula Master to save formulas.", style = MaterialTheme.typography.bodySmall, color = AsiTextSecondaryDark)
            }
        } else {
            items(bookmarkedFormulas) { f ->
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(f.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                            IconButton(onClick = { AppStudyRepository.toggleFormulaBookmark(f.id) }) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Remove", tint = AsiCyan, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(f.expression, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Units: ${f.unitAndDimension}", style = MaterialTheme.typography.labelSmall, color = AsiAmber)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Saved Chapter Notes (${savedNotes.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiEmerald)
        }

        if (savedNotes.isEmpty()) {
            item {
                Text("No notes saved yet. Tap the bookmark icon in Smart Notes Engine to save chapters.", style = MaterialTheme.typography.bodySmall, color = AsiTextSecondaryDark)
            }
        } else {
            items(savedNotes) { note ->
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(note.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                            IconButton(onClick = { AppStudyRepository.toggleNoteSaved(note.id) }) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Remove", tint = AsiEmerald, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(note.oneMinuteRevision, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// 4. PROFILE & SETTINGS SCREEN
@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onUpdateProfile: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var studentName by remember { mutableStateOf(userProfile.studentName) }
    var selectedClass by remember { mutableStateOf(userProfile.selectedClass) }
    var selectedBoard by remember { mutableStateOf(userProfile.selectedBoard) }
    var selectedLanguage by remember { mutableStateOf(userProfile.preferredLanguage) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Student Academic Profile", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = {
                            studentName = it
                            onUpdateProfile(userProfile.copy(studentName = it))
                        },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Selected Class:", style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AcademicClass.values().forEach { cls ->
                            val isSel = selectedClass == cls
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedClass = cls
                                    onUpdateProfile(userProfile.copy(selectedClass = cls))
                                },
                                label = { Text(cls.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Educational Board:", style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        EducationBoard.values().forEach { brd ->
                            val isSel = selectedBoard == brd
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedBoard = brd
                                    onUpdateProfile(userProfile.copy(selectedBoard = brd))
                                },
                                label = { Text(brd.code) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Study Language:", style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StudyLanguage.values().forEach { lang ->
                            val isSel = selectedLanguage == lang
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedLanguage = lang
                                    onUpdateProfile(userProfile.copy(preferredLanguage = lang))
                                },
                                label = { Text(lang.displayName) }
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = AsiNavyCard)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ASI Intelligence Engine Configuration", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiAmber)
                    Spacer(modifier = Modifier.height(8.dp))

                    val hasKey = BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Gemini API Engine Status", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Surface(
                            color = if (hasKey) AsiEmerald.copy(alpha = 0.2f) else AsiAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                if (hasKey) "CLOUD CONNECTED" else "ON-DEVICE PEDAGOGICAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (hasKey) AsiEmerald else AsiAmber,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Cloud AI is powered by gemini-3.5-flash. When operating without API keys, ASI activates the integrated On-Device Pedagogical Knowledge Engine.",
                        style = MaterialTheme.typography.labelSmall,
                        color = AsiTextSecondaryDark
                    )
                }
            }
        }
    }
}
