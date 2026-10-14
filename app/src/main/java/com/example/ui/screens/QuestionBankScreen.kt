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
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.model.StudyLanguage
import com.example.data.repository.CurriculumRepository
import com.example.ui.theme.*

@Composable
fun QuestionBankScreen(
    language: StudyLanguage = StudyLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<QuestionType?>(null) }
    var selectedChapterId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val allQuestions = remember { CurriculumRepository.allQuestions }

    val filtered = remember(selectedDifficulty, selectedType, selectedChapterId, searchQuery) {
        allQuestions.filter { q ->
            val matchesDiff = selectedDifficulty == null || q.difficulty == selectedDifficulty
            val matchesType = selectedType == null || q.type == selectedType
            val matchesChapter = selectedChapterId == null || q.chapterId == selectedChapterId
            val matchesSearch = searchQuery.isBlank() ||
                    q.question.contains(searchQuery, ignoreCase = true) ||
                    q.conceptExplanation.contains(searchQuery, ignoreCase = true)
            matchesDiff && matchesType && matchesChapter && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search question bank or concepts...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AsiCyan) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = null, tint = AsiTextSecondaryDark)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("question_bank_search"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AsiCyan,
                unfocusedBorderColor = AsiNavyBorder,
                focusedContainerColor = AsiNavyCard,
                unfocusedContainerColor = AsiNavyCard
            ),
            singleLine = true
        )

        // Chapter Filter Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            item {
                DiffPill("All Chapters", selectedChapterId == null) { selectedChapterId = null }
            }
            item {
                DiffPill("Quadratic Equations (Math)", selectedChapterId == "math_10_ch04") { selectedChapterId = "math_10_ch04" }
            }
            item {
                DiffPill("Light Refraction (Science)", selectedChapterId == "sci_10_ch10") { selectedChapterId = "sci_10_ch10" }
            }
            item {
                DiffPill("Electricity (Science)", selectedChapterId == "sci_10_ch12") { selectedChapterId = "sci_10_ch12" }
            }
        }

        // Difficulty Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            item {
                DiffPill("All Difficulties", selectedDifficulty == null) { selectedDifficulty = null }
            }
            item {
                DiffPill("Basic", selectedDifficulty == "Basic") { selectedDifficulty = "Basic" }
            }
            item {
                DiffPill("Intermediate", selectedDifficulty == "Intermediate") { selectedDifficulty = "Intermediate" }
            }
            item {
                DiffPill("Challenging", selectedDifficulty == "Challenging") { selectedDifficulty = "Challenging" }
            }
        }

        // Question Type Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            item {
                TypePill("All Types", selectedType == null) { selectedType = null }
            }
            items(QuestionType.values()) { type ->
                TypePill(type.displayName, selectedType == type) { selectedType = type }
            }
        }

        // List of Practice Questions
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filtered) { q ->
                PracticeQuestionCard(question = q, language = language)
            }
        }
    }
}

@Composable
fun DiffPill(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) AsiAmber else AsiNavyCard,
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiAmber else AsiNavyBorder)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
            color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun TypePill(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) AsiCyan else AsiNavyCard,
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
            color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
