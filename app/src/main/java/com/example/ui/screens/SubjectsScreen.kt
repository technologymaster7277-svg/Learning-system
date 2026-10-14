package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicClass
import com.example.data.model.EducationBoard
import com.example.data.model.SubjectCategory
import com.example.data.model.UserProfile
import com.example.data.repository.CurriculumRepository
import com.example.ui.components.AppDestination
import com.example.ui.theme.*

@Composable
fun SubjectsScreen(
    userProfile: UserProfile,
    onNavigateToDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedClass by remember { mutableStateOf(userProfile.selectedClass) }
    var selectedSubject by remember { mutableStateOf<SubjectCategory?>(null) }

    val subjects = if (selectedClass == AcademicClass.CLASS_9 || selectedClass == AcademicClass.CLASS_10) {
        listOf(
            SubjectCategory.MATHEMATICS,
            SubjectCategory.SCIENCE,
            SubjectCategory.ENGLISH,
            SubjectCategory.SOCIAL_SCIENCE,
            SubjectCategory.HINDI
        )
    } else {
        listOf(
            SubjectCategory.PHYSICS,
            SubjectCategory.CHEMISTRY,
            SubjectCategory.MATHEMATICS,
            SubjectCategory.BIOLOGY,
            SubjectCategory.ENGLISH
        )
    }

    val chapters = remember(selectedClass) {
        CurriculumRepository.allChapters.filter { it.classCode == selectedClass.code }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Class Selector Row
        Text(
            text = "Select Academic Class & Curriculum",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AsiCyan
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AcademicClass.values().forEach { cls ->
                val isSel = selectedClass == cls
                Surface(
                    color = if (isSel) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedClass = cls }
                        .testTag("class_selector_${cls.code}")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = cls.displayName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Subject Horizontal Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Surface(
                    color = if (selectedSubject == null) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { selectedSubject = null }
                ) {
                    Text(
                        text = "All Subjects",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selectedSubject == null) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (selectedSubject == null) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            items(subjects) { subj ->
                val isSel = selectedSubject == subj
                Surface(
                    color = if (isSel) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { selectedSubject = subj }
                ) {
                    Text(
                        text = subj.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chapters List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(chapters) { ch ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (ch.isDemoChapter) {
                                onNavigateToDemo()
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (ch.isDemoChapter) AsiCyan.copy(alpha = 0.2f) else AsiCobalt.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (ch.isDemoChapter) "DEMO READY • CHAPTER ${ch.number}" else "CHAPTER ${ch.number}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (ch.isDemoChapter) AsiCyan else AsiCobalt,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${ch.weightageMarks} Marks in Board Exam",
                                style = MaterialTheme.typography.labelSmall,
                                color = AsiAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${ch.title} (${ch.hindiTitle})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ch.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${ch.topicCount} Topics • ${ch.estimatedMinutes} Mins Study",
                                style = MaterialTheme.typography.labelSmall,
                                color = AsiTextSecondaryDark
                            )

                            if (ch.isDemoChapter) {
                                Button(
                                    onClick = onNavigateToDemo,
                                    colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Open Study Pack", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
