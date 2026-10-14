package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResearchProject
import com.example.data.repository.GlobalKnowledgeRepository
import com.example.ui.theme.*

@Composable
fun ResearchWorkspaceScreen(
    modifier: Modifier = Modifier
) {
    val sampleProjects = GlobalKnowledgeRepository.sampleResearchProjects
    var selectedProjectId by remember { mutableStateOf(sampleProjects.first().id) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Question & Hypotheses, 1: Literature Review, 2: Methodology & Checklist, 3: Thesis Outline

    val currentProject = remember(selectedProjectId) {
        sampleProjects.find { it.id == selectedProjectId } ?: sampleProjects.first()
    }

    var reproducibilityState by remember(currentProject) {
        mutableStateOf(currentProject.reproducibilityChecklist.toMap())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
    ) {
        // Research Workspace Header
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
                            color = AsiCobalt.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "PHD & DOCTORAL RESEARCH WORKSPACE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCobalt,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Level 8: Academic Research",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsiPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Literature Reviews, Hypotheses & Reproducibility",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scientific workspace for doctoral researchers. Formulate research questions, audit verified peer-reviewed publications, organize reproducible experimental protocols, and structure thesis chapters. Distinguishes verified facts from working hypotheses.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Project Switcher
        item {
            Text(
                text = "Active Doctoral Research Projects:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(sampleProjects) { proj ->
                    val isSel = proj.id == currentProject.id
                    Surface(
                        color = if (isSel) AsiNavyCardElevated else AsiNavyDarkest,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(if (isSel) AsiCyan else AsiNavyBorder)
                        ),
                        modifier = Modifier
                            .widthIn(max = 260.dp)
                            .clickable { selectedProjectId = proj.id }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = proj.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) AsiCyan else MaterialTheme.colorScheme.onSurface,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = proj.fieldOfStudy,
                                fontSize = 10.sp,
                                color = if (isSel) AsiTextPrimaryDark else AsiTextSecondaryDark
                            )
                        }
                    }
                }
            }
        }

        // Active Project Overview Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = currentProject.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Field: ${currentProject.fieldOfStudy}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = AsiPurple.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = currentProject.verificationStatus,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsiPurple,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // Tab Selector for Research Workspace
        item {
            val tabs = listOf(
                "Question & Hypotheses",
                "Literature Review (${currentProject.literatureReviewOutlines.size})",
                "Methodology & Protocol",
                "Thesis Outline (${currentProject.thesisChapters.size})"
            )
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
                            Text(title, fontSize = 10.sp, fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal)
                        }
                    )
                }
            }
        }

        // Tab 0: Question & Hypotheses
        if (selectedTab == 0) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Central Research Question:",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentProject.researchQuestion,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Primary Working Hypothesis (Unverified Candidate):",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = AsiNavyDarkest,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = currentProject.primaryHypothesis,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Identified Open Research Gaps:",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        currentProject.openGapsIdentified.forEach { gap ->
                            Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(gap, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Tab 1: Literature Review Manager
        if (selectedTab == 1) {
            items(currentProject.literatureReviewOutlines) { lit ->
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
                                text = "[${lit.citationKey}]",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsiCyan
                            )
                            Text(
                                text = "${lit.year} • ${lit.venueOrJournal}",
                                fontSize = 11.sp,
                                color = AsiAmber,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lit.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Authors: ${lit.authors}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiTextSecondaryDark
                        )
                        Text(
                            text = lit.doiOrRef,
                            style = MaterialTheme.typography.labelSmall,
                            color = AsiCobalt
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = AsiNavyDarkest,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Core Verified Findings:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsiEmerald)
                                Text(lit.coreFindings, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Identified Limitation / Gap:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsiRuby)
                                Text(lit.limitationOrGap, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: Methodology & Reproducibility Checklist
        if (selectedTab == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Experimental Methodology Steps:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        currentProject.methodologySteps.forEach { step ->
                            Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(12.dp).padding(top = 4.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(step, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Scientific Reproducibility Checklist:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiEmerald
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        reproducibilityState.forEach { (itemTitle, isChecked) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable {
                                        reproducibilityState = reproducibilityState + (itemTitle to !isChecked)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        reproducibilityState = reproducibilityState + (itemTitle to checked)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = AsiEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = itemTitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isChecked) MaterialTheme.colorScheme.onSurface else AsiTextSecondaryDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tab 3: Thesis Chapters Outline
        if (selectedTab == 3) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Dissertation Chapter Architecture:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        currentProject.thesisChapters.forEachIndexed { idx, ch ->
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
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = AsiPurple, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ch,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
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
