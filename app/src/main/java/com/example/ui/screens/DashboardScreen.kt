package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AcademicClass
import com.example.data.model.UserProfile
import com.example.ui.components.AppDestination
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        // Hero Visual Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("dashboard_hero_card")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dashboard_hero),
                        contentDescription = "Research Lab Intelligence Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        AsiNavyDarkest.copy(alpha = 0.85f),
                                        AsiNavyDarkest
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = AsiCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "ASI RESEARCH INTELLIGENCE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Learn Anything. Master Every Discipline. Advance Human Knowledge.",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Global Knowledge Intelligence: School to PhD, All Disciplines",
                            style = MaterialTheme.typography.bodySmall,
                            color = AsiCyan
                        )
                    }
                }
            }
        }

        // Global Knowledge Hierarchy Banner
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Global Knowledge Intelligence (School to PhD)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Text("11 Disciplines", fontSize = 11.sp, color = AsiAmber, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = AsiNavyCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigate(AppDestination.GLOBAL_TAXONOMY) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Public, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Universal Taxonomy", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("10 Tiers", fontSize = 10.sp, color = AsiTextSecondaryDark)
                            }
                        }

                        Surface(
                            color = AsiNavyCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigate(AppDestination.HIGHER_ED) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.School, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("University / B.Tech", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Semesters & Labs", fontSize = 10.sp, color = AsiTextSecondaryDark)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = AsiNavyCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigate(AppDestination.RESEARCH_LAB) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Biotech, contentDescription = null, tint = AsiPurple, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("PhD Research Workspace", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Literature & Hypotheses", fontSize = 10.sp, color = AsiTextSecondaryDark)
                            }
                        }

                        Surface(
                            color = AsiNavyCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigate(AppDestination.COMPETITIVE_EXAMS) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = AsiAmber, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Competitive Exam Hub", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("UPSC • JEE • NEET", fontSize = 10.sp, color = AsiTextSecondaryDark)
                            }
                        }
                    }
                }
            }
        }

        // Live Performance Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Streak",
                    value = "${userProfile.studyStreakDays} Days",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = AsiAmber,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Solved",
                    value = "${userProfile.totalQuestionsAttempted} Qs",
                    icon = Icons.Default.CheckCircle,
                    iconTint = AsiEmerald,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Accuracy",
                    value = "${userProfile.overallAccuracyPercent}%",
                    icon = Icons.Default.TrendingUp,
                    iconTint = AsiCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Time",
                    value = "${userProfile.studyHoursTotal} hrs",
                    icon = Icons.Default.Schedule,
                    iconTint = AsiCobalt,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Continue Learning Active Chapter (Quadratic Equations Demo)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AsiCyan, AsiCobalt))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppDestination.QUADRATIC_DEMO) }
                    .testTag("continue_learning_card")
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
                                text = "CONTINUE LEARNING • CH 4",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "68% Complete",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AsiEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Quadratic Equations (द्विघात समीकरण)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Next topic: Discriminant Nature of Roots & Upstream/Downstream Word Problems",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { 0.68f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = AsiCyan,
                        trackColor = AsiNavyBorder
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onNavigate(AppDestination.QUADRATIC_DEMO) },
                            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("Resume Chapter", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Quick Intelligence Workspaces
        item {
            Text(
                text = "Study Intelligence Tools",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToolCard(
                    title = "AI Study Tutor",
                    subtitle = "Gemini Powered",
                    icon = Icons.Default.Psychology,
                    accentColor = AsiCyan,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    modifier = Modifier.weight(1f)
                )
                QuickToolCard(
                    title = "Formula Master",
                    subtitle = "With Solvers",
                    icon = Icons.Default.Calculate,
                    accentColor = AsiEmerald,
                    onClick = { onNavigate(AppDestination.FORMULA_MASTER) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToolCard(
                    title = "Tricky Questions",
                    subtitle = "Board Standard",
                    icon = Icons.Default.Quiz,
                    accentColor = AsiAmber,
                    onClick = { onNavigate(AppDestination.QUESTION_BANK) },
                    modifier = Modifier.weight(1f)
                )
                QuickToolCard(
                    title = "Virtual Science Lab",
                    subtitle = "5 Simulators",
                    icon = Icons.Default.Science,
                    accentColor = AsiCobalt,
                    onClick = { onNavigate(AppDestination.VIRTUAL_LAB) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Adaptive AI Recommendations & Weak Areas
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AsiAmber, modifier = Modifier.size(20.dp))
                        Text(
                            text = "AI Adaptive Insights & Weak Areas",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    WeakTopicItem(
                        topic = "Stream Speed in Boat Problems",
                        chapter = "Ch 4 Quadratic Equations",
                        issue = "Sign inverted when calculating (u - v) upstream",
                        remedy = "Review Worked Example & Practice 2 Questions",
                        onPractice = { onNavigate(AppDestination.QUESTION_BANK) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WeakTopicItem(
                        topic = "Sign of 'b' in -b ± √D",
                        chapter = "Ch 4 Quadratic Formula",
                        issue = "When b is negative (e.g. b = -7), students write -7 instead of +7",
                        remedy = "Use Formula Master computational verifier",
                        onPractice = { onNavigate(AppDestination.FORMULA_MASTER) }
                    )
                }
            }
        }

        // Today's Study Plan
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Today's Target Revision Plan",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ScheduleItem(
                        time = "Session 1 • 20 mins",
                        title = "Quadratic Equations — Nature of Roots",
                        completed = true
                    )
                    ScheduleItem(
                        time = "Session 2 • 15 mins",
                        title = "Formula Master — Ohm's Law & Circuit Calculations",
                        completed = false
                    )
                    ScheduleItem(
                        time = "Session 3 • 25 mins",
                        title = "Board Mock Exam — Section B Numerical Problems",
                        completed = false
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AsiNavyCard),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WeakTopicItem(
    topic: String,
    chapter: String,
    issue: String,
    remedy: String,
    onPractice: () -> Unit
) {
    Surface(
        color = AsiNavyCardElevated,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = AsiAmber
                )
                Text(
                    text = chapter,
                    style = MaterialTheme.typography.labelSmall,
                    color = AsiTextSecondaryDark
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Detected mistake pattern: $issue",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = remedy,
                    style = MaterialTheme.typography.labelSmall,
                    color = AsiCyan
                )
                TextButton(
                    onClick = onPractice,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Practice Now", fontSize = 12.sp, color = AsiCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(
    time: String,
    title: String,
    completed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (completed) AsiEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
