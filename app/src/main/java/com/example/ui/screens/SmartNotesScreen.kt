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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StudyLanguage
import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.ui.theme.*

@Composable
fun SmartNotesScreen(
    language: StudyLanguage = StudyLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var selectedChapterId by remember { mutableStateOf("math_10_ch04") }
    var selectedFormat by remember { mutableStateOf("1-Min Flash") }

    val formats = listOf("1-Min Flash", "5-Min Recap", "1-Page Summary", "Conceptual Notes", "Definitions", "Common Traps", "Memory Aids", "Checklist")

    val allNotes = remember { CurriculumRepository.allSmartNotes }
    val currentNote = remember(selectedChapterId) {
        CurriculumRepository.getSmartNoteForChapter(selectedChapterId)
    }

    val savedNoteIds by AppStudyRepository.savedNoteIds.collectAsStateWithLifecycle()
    val isSaved = savedNoteIds.contains(currentNote.id)
    val isHindi = language == StudyLanguage.HINDI

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Chapter Switcher Row
        Text(
            text = "Select Chapter:",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AsiCyan
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            items(allNotes) { noteItem ->
                val isSel = selectedChapterId == noteItem.chapterId
                val chapterObj = CurriculumRepository.allChapters.find { it.id == noteItem.chapterId }
                val label = chapterObj?.title ?: noteItem.title
                Surface(
                    color = if (isSel) AsiCobalt else AsiNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSel) AsiCyan else AsiNavyBorder)
                    ),
                    modifier = Modifier.clickable { selectedChapterId = noteItem.chapterId }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Top Format Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(formats) { fmt ->
                val isSel = selectedFormat == fmt
                Surface(
                    color = if (isSel) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSel) AsiCyan else AsiNavyBorder)
                    ),
                    modifier = Modifier.clickable { selectedFormat = fmt }
                ) {
                    Text(
                        text = fmt,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Action bar (Save note, download, language indicator)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentNote.title} (${if (isHindi) "हिंदी" else "English"})",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )

            IconButton(onClick = { AppStudyRepository.toggleNoteSaved(currentNote.id) }) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save Note",
                    tint = if (isSaved) AsiCyan else AsiTextSecondaryDark
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            when (selectedFormat) {
                "1-Min Flash" -> {
                    item {
                        NoteContentCard(
                            title = "⚡ 1-Minute High-Yield Flash",
                            subtitle = "Immediate recall for pre-exam review",
                            body = if (isHindi && currentNote.hindiSummary.isNotBlank()) currentNote.hindiSummary else currentNote.oneMinuteRevision,
                            badgeColor = AsiCyan
                        )
                    }
                }
                "5-Min Recap" -> {
                    item {
                        NoteContentCard(
                            title = "⏱ 5-Minute Comprehensive Recap",
                            subtitle = "Core methods, relations & exam conditions",
                            body = currentNote.fiveMinuteRevision,
                            badgeColor = AsiAmber
                        )
                    }
                }
                "1-Page Summary" -> {
                    item {
                        NoteContentCard(
                            title = "📄 1-Page Chapter Summary",
                            subtitle = "Complete architectural overview of the chapter",
                            body = currentNote.onePageSummary,
                            badgeColor = AsiEmerald
                        )
                    }
                }
                "Conceptual Notes" -> {
                    item {
                        NoteContentCard(
                            title = "🧠 Deep Conceptual Derivations",
                            subtitle = "First-principles deduction and proofs",
                            body = currentNote.conceptualNotes,
                            badgeColor = AsiCobalt
                        )
                    }
                }
                "Definitions" -> {
                    items(currentNote.importantDefinitions) { (term, def) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(term, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(def, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
                "Common Traps" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("⚠️ High-Frequency Student Misconceptions", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiRuby)
                                Spacer(modifier = Modifier.height(8.dp))
                                currentNote.commonMistakes.forEach { m ->
                                    Row(modifier = Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("•", color = AsiRuby, fontWeight = FontWeight.Bold)
                                        Text(m, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }
                "Memory Aids" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("💡 Mnemonics & Cognitive Memory Aids", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiAmber)
                                Spacer(modifier = Modifier.height(8.dp))
                                currentNote.memoryAids.forEach { aid ->
                                    Text("🎯 $aid", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
                "Checklist" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("✓ Chapter Mastery Checklist", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AsiEmerald)
                                Spacer(modifier = Modifier.height(8.dp))
                                currentNote.checklist.forEach { item ->
                                    var checked by remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { checked = !checked }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(checked = checked, onCheckedChange = { checked = it }, colors = CheckboxDefaults.colors(checkedColor = AsiCyan))
                                        Text(item, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteContentCard(
    title: String,
    subtitle: String,
    body: String,
    badgeColor: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = badgeColor)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = AsiTextSecondaryDark)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
