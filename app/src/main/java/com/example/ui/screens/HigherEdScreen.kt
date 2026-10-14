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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.model.CourseUnit
import com.example.data.model.UniversityCourse
import com.example.data.repository.GlobalKnowledgeRepository
import com.example.ui.theme.*

@Composable
fun HigherEdScreen(
    modifier: Modifier = Modifier
) {
    val allCourses = GlobalKnowledgeRepository.universityCourses
    var selectedCourseId by remember { mutableStateOf(allCourses.first().id) }
    var selectedUnitId by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Units & Syllabus, 1: Derivations & Labs, 2: Textbooks & Credits

    val currentCourse = remember(selectedCourseId) {
        allCourses.find { it.id == selectedCourseId } ?: allCourses.first()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
    ) {
        // Higher Education Header
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
                            color = AsiCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "HIGHER EDUCATION & ENGINEERING MODULE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Semester & Credit Model",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsiEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "University Courses, Derivations & Lab Activities",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Dedicated architecture for Undergraduate (B.Tech, B.Sc) and Postgraduate programmes. Includes course codes, unit syllabi, credits, rigorous derivations, and laboratory experiments.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Course Selector Row
        item {
            Text(
                text = "Select Course & Programme:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(allCourses) { crs ->
                    val isSel = crs.id == currentCourse.id
                    Surface(
                        color = if (isSel) AsiNavyCardElevated else AsiNavyDarkest,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(if (isSel) AsiCyan else AsiNavyBorder)
                        ),
                        modifier = Modifier.clickable { selectedCourseId = crs.id }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(
                                text = crs.courseCode,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) AsiCyan else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = crs.programme,
                                fontSize = 10.sp,
                                color = if (isSel) AsiTextPrimaryDark else AsiTextSecondaryDark
                            )
                        }
                    }
                }
            }
        }

        // Active Course Metadata Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currentCourse.courseCode}: ${currentCourse.title}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Surface(
                            color = AsiCobalt.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${currentCourse.credits} Credits • Sem ${currentCourse.semester}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsiCobalt,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Programme: ${currentCourse.programme} • Discipline: ${currentCourse.discipline.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCourse.syllabusOverview,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = AsiEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = currentCourse.verificationLabel,
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

        // Tab Selector for Course Content
        item {
            val tabs = listOf("Units & Syllabus (${currentCourse.units.size})", "Derivations & Lab Activities", "Textbooks & Prerequisites")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AsiNavyDarkest,
                contentColor = AsiCyan
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

        // Tab 0: Course Units
        if (selectedTab == 0) {
            items(currentCourse.units) { unit ->
                val isExpanded = selectedUnitId == unit.id
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedUnitId = if (isExpanded) null else unit.id }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Unit ${unit.unitNumber}: ${unit.title}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = AsiCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${unit.topics.size} Topics • ${unit.learningOutcomes.size} Learning Outcomes",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiTextSecondaryDark
                        )

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Text("Core Syllabus Topics:", style = MaterialTheme.typography.labelSmall, color = AsiCyan, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                unit.topics.forEach { t ->
                                    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ArrowRight, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(16.dp))
                                        Text(t, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Formal Learning Outcomes:", style = MaterialTheme.typography.labelSmall, color = AsiEmerald, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                unit.learningOutcomes.forEach { lo ->
                                    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(lo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab 1: Derivations & Labs
        if (selectedTab == 1) {
            items(currentCourse.units) { unit ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Unit ${unit.unitNumber}: ${unit.title}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )

                        if (unit.workedDerivations.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Mathematical / Theoretical Derivations:", style = MaterialTheme.typography.labelSmall, color = AsiAmber, fontWeight = FontWeight.Bold)
                            unit.workedDerivations.forEach { d ->
                                Surface(
                                    color = AsiNavyDarkest,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = d,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        if (unit.labActivities.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Practical Laboratory Experiments:", style = MaterialTheme.typography.labelSmall, color = AsiEmerald, fontWeight = FontWeight.Bold)
                            unit.labActivities.forEach { lab ->
                                Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Science, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(lab, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: Textbooks & Prerequisites
        if (selectedTab == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Prerequisites Knowledge:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                        Spacer(modifier = Modifier.height(6.dp))
                        currentCourse.prerequisites.forEach { req ->
                            Text("• $req", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Standard University Textbooks & References:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiEmerald)
                        Spacer(modifier = Modifier.height(6.dp))
                        currentCourse.textbooks.forEach { tb ->
                            Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tb, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }
    }
}
