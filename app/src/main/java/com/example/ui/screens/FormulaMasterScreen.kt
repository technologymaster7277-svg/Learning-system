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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FormulaItem
import com.example.data.model.SubjectCategory
import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.data.repository.FormulaSolvers
import com.example.ui.theme.*

@Composable
fun FormulaMasterScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf<SubjectCategory?>(null) }
    var isFlashcardMode by remember { mutableStateOf(false) }
    var showSolverDialog by remember { mutableStateOf<FormulaItem?>(null) }

    val allFormulas = remember { CurriculumRepository.allFormulas }
    val bookmarkedIds by AppStudyRepository.bookmarkedFormulaIds.collectAsStateWithLifecycle()

    val filteredFormulas = remember(searchQuery, selectedSubject) {
        allFormulas.filter { formula ->
            val matchesSubject = selectedSubject == null || formula.subject == selectedSubject
            val matchesSearch = searchQuery.isBlank() ||
                    formula.name.contains(searchQuery, ignoreCase = true) ||
                    formula.expression.contains(searchQuery, ignoreCase = true) ||
                    formula.conditions.contains(searchQuery, ignoreCase = true)
            matchesSubject && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search & Controls Row
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search formulas (e.g. Quadratic, Ohm, Lens)...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AsiCyan) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = AsiTextSecondaryDark)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("formula_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AsiCyan,
                unfocusedBorderColor = AsiNavyBorder,
                focusedContainerColor = AsiNavyCard,
                unfocusedContainerColor = AsiNavyCard
            ),
            singleLine = true
        )

        // Filter Pills & Flashcard toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    SubjectPill(
                        title = "All Subjects",
                        isSelected = selectedSubject == null,
                        onClick = { selectedSubject = null }
                    )
                }
                item {
                    SubjectPill(
                        title = "Mathematics",
                        isSelected = selectedSubject == SubjectCategory.MATHEMATICS,
                        onClick = { selectedSubject = SubjectCategory.MATHEMATICS }
                    )
                }
                item {
                    SubjectPill(
                        title = "Physics",
                        isSelected = selectedSubject == SubjectCategory.PHYSICS,
                        onClick = { selectedSubject = SubjectCategory.PHYSICS }
                    )
                }
            }

            Surface(
                color = if (isFlashcardMode) AsiCyan else AsiNavyCard,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { isFlashcardMode = !isFlashcardMode }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipToBack,
                        contentDescription = null,
                        tint = if (isFlashcardMode) AsiNavyDarkest else AsiCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Cards",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isFlashcardMode) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Formula List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredFormulas) { item ->
                val isBookmarked = bookmarkedIds.contains(item.id)
                FormulaDetailCard(
                    formula = item,
                    isFlashcard = isFlashcardMode,
                    isBookmarked = isBookmarked,
                    onToggleBookmark = { AppStudyRepository.toggleFormulaBookmark(item.id) },
                    onOpenSolver = { showSolverDialog = item }
                )
            }
        }
    }

    // Modal Solver Dialog
    showSolverDialog?.let { formula ->
        FormulaSolverDialog(formula = formula, onDismiss = { showSolverDialog = null })
    }
}

@Composable
fun SubjectPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) AsiCyan else AsiNavyCard,
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun FormulaDetailCard(
    formula: FormulaItem,
    isFlashcard: Boolean,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onOpenSolver: () -> Unit
) {
    var isRevealed by remember { mutableStateOf(!isFlashcard) }
    var isExpandedDetails by remember { mutableStateOf(false) }

    LaunchedEffect(isFlashcard) {
        isRevealed = !isFlashcard
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
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
                        text = formula.subject.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCobalt,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = AsiEmerald, modifier = Modifier.size(14.dp))
                        Text(
                            text = "Verified",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = AsiEmerald
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark Formula",
                            tint = if (isBookmarked) AsiCyan else AsiTextSecondaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formula.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Mathematical Expression Surface
            Surface(
                color = AsiNavyDarkest,
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(AsiNavyBorder)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (isFlashcard) isRevealed = !isRevealed }
            ) {
                Box(
                    modifier = Modifier.padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRevealed) {
                        Text(
                            text = formula.expression,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = AsiCyan
                        )
                    } else {
                        Text(
                            text = "Tap to Reveal Formula (Flashcard Mode)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = AsiTextSecondaryDark
                        )
                    }
                }
            }

            if (isRevealed) {
                Spacer(modifier = Modifier.height(12.dp))

                // Meaning of symbols
                Text(
                    text = "Symbol Breakdown & Units:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiTextSecondaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                formula.symbolsMeaning.forEach { (sym, desc) ->
                    Text(
                        text = "• $sym : $desc",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Units & Dimensions: ${formula.unitAndDimension}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AsiAmber
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (formula.solverType != null) {
                        Button(
                            onClick = onOpenSolver,
                            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Calculator", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    TextButton(onClick = { isExpandedDetails = !isExpandedDetails }) {
                        Text(
                            text = if (isExpandedDetails) "Hide Example" else "Worked Example",
                            color = AsiCyan,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = if (isExpandedDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = AsiCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                AnimatedVisibility(visible = isExpandedDetails) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(AsiNavyCardElevated, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Conditions of Use: ${formula.conditions}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Worked Example:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiEmerald
                        )
                        Text(
                            text = formula.workedExample,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaSolverDialog(
    formula: FormulaItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AsiNavyCardElevated,
        title = {
            Text(
                text = "${formula.name} — Computational Verifier",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AsiCyan
            )
        },
        text = {
            when (formula.solverType) {
                "OHM" -> OhmSolverContent()
                "LENS" -> LensSolverContent()
                else -> QuadraticSolverDialogContent()
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = AsiCyan, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun QuadraticSolverDialogContent() {
    var a by remember { mutableStateOf("1") }
    var b by remember { mutableStateOf("-5") }
    var c by remember { mutableStateOf("6") }
    var res by remember { mutableStateOf(FormulaSolvers.solveQuadratic(1.0, -5.0, 6.0)) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("a") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = b, onValueChange = { b = it }, label = { Text("b") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = c, onValueChange = { c = it }, label = { Text("c") }, modifier = Modifier.weight(1f))
        }
        Button(
            onClick = {
                res = FormulaSolvers.solveQuadratic(a.toDoubleOrNull() ?: 1.0, b.toDoubleOrNull() ?: 0.0, c.toDoubleOrNull() ?: 0.0)
            },
            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compute D and Roots", fontWeight = FontWeight.Bold)
        }
        Surface(color = AsiNavyDarkest, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("D = b² - 4ac = ${"%.2f".format(res.discriminant)}", color = AsiCyan, fontWeight = FontWeight.Bold)
                Text("Nature: ${res.nature}", color = AsiEmerald, fontSize = 12.sp)
                if (res.root1 != null) {
                    Text("x₁ = ${"%.3f".format(res.root1)}  |  x₂ = ${"%.3f".format(res.root2)}", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun OhmSolverContent() {
    var vText by remember { mutableStateOf("220") }
    var rText by remember { mutableStateOf("44") }
    var res by remember { mutableStateOf(FormulaSolvers.solveOhm(220.0, null, 44.0)) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = vText, onValueChange = { vText = it }, label = { Text("Voltage V (Volts)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = rText, onValueChange = { rText = it }, label = { Text("Resistance R (Ohms)") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val v = vText.toDoubleOrNull()
                val r = rText.toDoubleOrNull()
                res = FormulaSolvers.solveOhm(v, null, r)
            },
            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compute Current & Power", fontWeight = FontWeight.Bold)
        }

        Surface(color = AsiNavyDarkest, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Current I = V/R = ${"%.2f".format(res.current)} Amperes (A)", color = AsiCyan, fontWeight = FontWeight.Bold)
                Text("Power P = V × I = ${"%.2f".format(res.powerWatts)} Watts (W)", color = AsiEmerald, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun LensSolverContent() {
    var fText by remember { mutableStateOf("15") }
    var uText by remember { mutableStateOf("-30") }
    var res by remember { mutableStateOf(FormulaSolvers.solveLens(15.0, -30.0)) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = fText, onValueChange = { fText = it }, label = { Text("Focal length f (+ for convex, - for concave) cm") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = uText, onValueChange = { uText = it }, label = { Text("Object distance u (always negative) cm") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val f = fText.toDoubleOrNull() ?: 15.0
                val u = uText.toDoubleOrNull() ?: -30.0
                res = FormulaSolvers.solveLens(f, u)
            },
            colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compute Image Distance & Magnification", fontWeight = FontWeight.Bold)
        }

        Surface(color = AsiNavyDarkest, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Image Distance (v) = ${"%.2f".format(res.imageDistance)} cm", color = AsiCyan, fontWeight = FontWeight.Bold)
                Text("Magnification (m) = ${"%.2f".format(res.magnification)}", color = AsiAmber, fontSize = 13.sp)
                Text("Nature: ${if (res.isReal) "Real and Inverted" else "Virtual and Erect"}, ${res.sizeDescription}", color = AsiEmerald, fontSize = 12.sp)
            }
        }
    }
}
