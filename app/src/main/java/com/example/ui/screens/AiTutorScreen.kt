package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.data.ai.GeminiTutorService
import com.example.data.model.ChatMessage
import com.example.data.model.StudyLanguage
import com.example.data.model.TutorMode
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiTutorScreen(
    language: StudyLanguage = StudyLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(TutorMode.EXPLAIN_SIMPLY) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "welcome_msg",
                text = "Welcome to the ASI AI Study Tutor! I am grounded in CBSE & State Board curriculum for Classes 9–12. Choose a mode below or ask any concept, derivation, or problem from your syllabus.",
                isUser = false,
                mode = TutorMode.EXPLAIN_SIMPLY
            )
        )
    }

    val quickPrompts = listOf(
        "Explain Discriminant D = b² - 4ac and roots nature simply",
        "Step-by-step solution: Solve 2x² - 7x + 3 = 0",
        "Teach me upstream vs downstream boat problems through questions",
        "Exam Answer format: Prove why roots are equal when D = 0",
        "Give me a challenge problem on quadratic equations"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Mode Selector Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(TutorMode.values()) { mode ->
                val isSelected = selectedMode == mode
                Surface(
                    color = if (isSelected) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
                    ),
                    modifier = Modifier.clickable { selectedMode = mode }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = AsiNavyDarkest,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = mode.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Active Mode Tagline
        Surface(
            color = AsiNavyCardElevated,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(16.dp))
                Text(
                    text = "Mode: ${selectedMode.title} — ${selectedMode.tagLine}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AsiCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Prompts Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(quickPrompts) { prompt ->
                Surface(
                    color = AsiNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AsiNavyBorder)
                    ),
                    modifier = Modifier.clickable {
                        inputText = prompt
                    }
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Message Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(message = msg)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = AsiCyan,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "ASI Pedagogical Engine synthesizing response...",
                            style = MaterialTheme.typography.bodySmall,
                            color = AsiTextSecondaryDark
                        )
                    }
                }
            }
        }

        // Input Field and Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask a concept, problem, or doubt...", fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_tutor_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AsiCyan,
                    unfocusedBorderColor = AsiNavyBorder,
                    focusedContainerColor = AsiNavyCard,
                    unfocusedContainerColor = AsiNavyCard
                ),
                maxLines = 3
            )

            IconButton(
                onClick = {
                    val query = inputText.trim()
                    if (query.isNotBlank() && !isLoading) {
                        messages.add(
                            ChatMessage(
                                id = "u_${System.currentTimeMillis()}",
                                text = query,
                                isUser = true,
                                mode = selectedMode
                            )
                        )
                        inputText = ""
                        isLoading = true

                        coroutineScope.launch {
                            val history = messages.map { it.isUser to it.text }
                            val reply = GeminiTutorService.askTutor(
                                userQuery = query,
                                mode = selectedMode,
                                subject = "Mathematics",
                                chapter = "Quadratic Equations",
                                language = language,
                                chatHistory = history
                            )
                            messages.add(
                                ChatMessage(
                                    id = "a_${System.currentTimeMillis()}",
                                    text = reply,
                                    isUser = false,
                                    mode = selectedMode
                                )
                            )
                            isLoading = false
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                enabled = inputText.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank() && !isLoading) AsiCyan else AsiNavyCardElevated)
                    .testTag("ai_tutor_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send to AI Tutor",
                    tint = if (inputText.isNotBlank() && !isLoading) AsiNavyDarkest else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(14.dp))
                Text(
                    text = "ASI Study Tutor • ${message.mode.title}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
            }
        }

        Surface(
            color = if (isUser) AsiNavyCardElevated else AsiNavyCard,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(
                    if (isUser) AsiCobalt.copy(alpha = 0.4f) else AsiNavyBorder
                )
            ),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = if (isUser) AsiTextPrimaryDark else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}
