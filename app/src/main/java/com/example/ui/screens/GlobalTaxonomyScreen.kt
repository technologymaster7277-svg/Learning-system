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
import com.example.data.model.DisciplineGroup
import com.example.data.model.EducationTier
import com.example.data.repository.GlobalKnowledgeRepository
import com.example.ui.components.AppDestination
import com.example.ui.theme.*

@Composable
fun GlobalTaxonomyScreen(
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTier by remember { mutableStateOf(EducationTier.SECONDARY) }
    var selectedDiscipline by remember { mutableStateOf(DisciplineGroup.MATHEMATICS) }
    var searchQuery by remember { mutableStateOf("") }

    val allTiers = GlobalKnowledgeRepository.allTiers
    val allDisciplines = GlobalKnowledgeRepository.allDisciplines

    val filteredDisciplines = remember(searchQuery) {
        if (searchQuery.isBlank()) allDisciplines
        else allDisciplines.filter {
            it.displayName.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true) ||
                    it.representativeFields.any { f -> f.contains(searchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
    ) {
        // Hero Header: Universal Knowledge Vision
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
                                text = "GLOBAL KNOWLEDGE TAXONOMY",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "School → PhD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsiAmber
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Unified Knowledge Architecture for Human Learning",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Learn anything across 10 academic tiers and 11 universal discipline groups. From Class 1 foundations and Class 10/12 Board mastery, to B.Tech engineering, competitive exams, and peer-reviewed PhD research.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Jump shortcuts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppDestination.HIGHER_ED) },
                            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("University / B.Tech", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onNavigate(AppDestination.RESEARCH_LAB) },
                            colors = ButtonDefaults.buttonColors(containerColor = AsiCobalt, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PhD Workspace", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onNavigate(AppDestination.COMPETITIVE_EXAMS) },
                            colors = ButtonDefaults.buttonColors(containerColor = AsiAmber, contentColor = AsiNavyDarkest),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Competitive", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search any discipline, subject, or domain...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AsiCyan) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = AsiTextSecondaryDark)
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AsiCyan,
                    unfocusedBorderColor = AsiNavyBorder,
                    focusedContainerColor = AsiNavyCard,
                    unfocusedContainerColor = AsiNavyCard
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 1. Education Tier Selector
        item {
            Column {
                Text(
                    text = "1. Select Education Tier (10 Global Stages):",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(allTiers) { tier ->
                        val isSelected = selectedTier == tier
                        Surface(
                            color = if (isSelected) AsiCyan else AsiNavyCard,
                            shape = RoundedCornerShape(8.dp),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
                            ),
                            modifier = Modifier.clickable { selectedTier = tier }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = tier.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = tier.typicalDegree,
                                    fontSize = 10.sp,
                                    color = if (isSelected) AsiNavyDarkest.copy(alpha = 0.8f) else AsiTextSecondaryDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Tier Card Breakdown
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
                            text = "Tier Focus: ${selectedTier.displayName}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiEmerald
                        )
                        Surface(
                            color = AsiEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Code: ${selectedTier.code}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsiEmerald,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedTier.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Pedagogical Depth: " + when (selectedTier) {
                            EducationTier.PRIMARY -> "Level 1: Intuitive visual analogies, interactive stories, basic arithmetic."
                            EducationTier.MIDDLE -> "Level 2: Phenomenon observations, elementary algebra, introductory history."
                            EducationTier.SECONDARY -> "Level 3: Rigorous NCERT/Board concepts, proof of formulas, exam preparation."
                            EducationTier.SENIOR_SECONDARY -> "Level 4: Advanced calculus, thermodynamics, organic mechanisms, entrance focus."
                            EducationTier.VOCATIONAL_DIPLOMA -> "Level 5: Practical shop floor skills, instrumentation, circuit fabrication."
                            EducationTier.UNDERGRADUATE -> "Level 6: Formal theorem proving, university semester syllabus, lab experiments."
                            EducationTier.POSTGRADUATE -> "Level 7: Graduate seminars, advanced matrix analysis, specialized research."
                            EducationTier.DOCTORAL_PHD -> "Level 8: Original literature gaps, hypothesis formulation, peer-review publishing."
                            EducationTier.COMPETITIVE_EXAMS -> "Level 9: Speed, precision, negative-marking strategy, PYQ pattern mastery."
                            EducationTier.LIFELONG_PROFESSIONAL -> "Level 10: Continuous professional adaptation and industrial standards."
                        },
                        fontSize = 11.sp,
                        color = AsiAmber
                    )
                }
            }
        }

        // 2. Global Discipline Catalogue (11 Universal Groups)
        item {
            Text(
                text = "2. Universal Discipline Groups (${filteredDisciplines.size}):",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )
        }

        items(filteredDisciplines) { group ->
            val isSelected = selectedDiscipline == group
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) AsiNavyCardElevated else AsiNavyCard
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedDiscipline = group }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.displayName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) AsiCyan else MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = if (isSelected) AsiCyan else AsiTextSecondaryDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = group.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Representative Fields & Branches:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsiTextPrimaryDark)
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        group.representativeFields.take(4).forEach { field ->
                            Surface(
                                color = AsiNavyDarkest,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = field,
                                    fontSize = 10.sp,
                                    color = AsiCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
