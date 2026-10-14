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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompetitiveExam
import com.example.data.repository.GlobalKnowledgeRepository
import com.example.ui.theme.*

@Composable
fun CompetitiveExamsScreen(
    modifier: Modifier = Modifier
) {
    val allExams = GlobalKnowledgeRepository.competitiveExams
    var selectedExamId by remember { mutableStateOf(allExams.first().id) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Pattern & Weightage, 1: Syllabus Units, 2: Practice & Cutoffs

    val currentExam = remember(selectedExamId) {
        allExams.find { it.id == selectedExamId } ?: allExams.first()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
    ) {
        // Competitive Exam Hub Header
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCardElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AsiAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "COMPETITIVE EXAMINATIONS INTELLIGENCE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiAmber,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Level 9: Exam Strategy",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsiCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "National & State Competitive Exam Strategy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Comprehensive intelligence for UPSC Civil Services, JEE Advanced, NEET UG, GATE CS, and State PSCs. Topic-wise weightage mappings, stage architectures, previous-year cutoff trends, and timed problem sets.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Exam Switcher
        item {
            Text(
                text = "Select Target Competitive Examination:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(allExams) { exam ->
                    val isSel = exam.id == currentExam.id
                    Surface(
                        color = if (isSel) AsiNavyCardElevated else AsiNavyDarkest,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(if (isSel) AsiAmber else AsiNavyBorder)
                        ),
                        modifier = Modifier.clickable { selectedExamId = exam.id }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(
                                text = exam.shortCode,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) AsiAmber else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = exam.category,
                                fontSize = 10.sp,
                                color = if (isSel) AsiTextPrimaryDark else AsiTextSecondaryDark,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Active Exam Overview Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = currentExam.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AsiAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Conducting Body: ${currentExam.conductingBody}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Eligibility: ${currentExam.eligibility}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = AsiCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Mock Duration: ${currentExam.mockDurationMinutes} mins",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Surface(
                            color = AsiEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Total Marks: ${currentExam.totalMarks}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsiEmerald,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Tabs for Examination Details
        item {
            val tabs = listOf("Weightage & Cutoffs", "Syllabus Structure (${currentExam.syllabusUnits.size})", "Examination Stages")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AsiNavyDarkest,
                contentColor = AsiAmber
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(title, fontSize = 11.sp, fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal)
                        }
                    )
                }
            }
        }

        // Tab 0: Weightage & Cutoffs
        if (selectedTab == 0) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Historical Topic-Wise Weightage Analysis (%):",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiAmber
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        currentExam.weightageAnalysis.forEach { (topic, pct) ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(topic, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    Text("$pct%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { pct / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (pct >= 20) AsiEmerald else AsiCyan,
                                    trackColor = AsiNavyDarkest
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Historical Cutoff Trends & Qualifying Rules:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiEmerald
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = AsiNavyDarkest,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = currentExam.previousYearCutoffs,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Tab 1: Syllabus Structure
        if (selectedTab == 1) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Official Syllabus Coverage Mapping:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        currentExam.syllabusUnits.forEachIndexed { idx, unitTitle ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = AsiNavyBorder,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "U${idx + 1}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsiAmber,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = unitTitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: Stages
        if (selectedTab == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Examination Progression Stages:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        currentExam.stages.forEach { stage ->
                            Surface(
                                color = AsiNavyDarkest,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stage,
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
    }
}
