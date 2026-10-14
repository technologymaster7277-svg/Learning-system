package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ai.BookIntelligenceService
import com.example.data.ai.PdfValidationResult
import com.example.data.model.*
import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookIntelligenceScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Configuration states
    var selectedClass by remember { mutableStateOf(AcademicClass.CLASS_10) }
    var selectedBoard by remember { mutableStateOf(EducationBoard.CBSE) }
    var selectedSubject by remember { mutableStateOf(SubjectCategory.MATHEMATICS) }
    var selectedLanguage by remember { mutableStateOf(StudyLanguage.ENGLISH) }

    // Document & Processing states
    var selectedPresetDoc by remember { mutableStateOf("NCERT_Class10_Math_Ch4.pdf") }
    var uploadedFileName by remember { mutableStateOf<String?>(null) }
    var uploadedFileSizeFormatted by remember { mutableStateOf("3.8 MB") }
    var uploadedPageEstimate by remember { mutableStateOf(24) }
    var isLargeFileWarning by remember { mutableStateOf(false) }

    // Validation & Error states
    var validationError by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var currentStage by remember { mutableIntStateOf(5) }
    var stageDescription by remember { mutableStateOf("Pipeline complete. Study package active.") }
    var progressFraction by remember { mutableFloatStateOf(1.0f) }

    // Analyzed Package
    var currentStudyPackage by remember { mutableStateOf<BookStudyPackage?>(null) }
    var selectedPackageTab by remember { mutableIntStateOf(0) }
    var showSavedPackagesDialog by remember { mutableStateOf(false) }

    // Saved packages in repository
    val savedPackIds by AppStudyRepository.savedPackIds.collectAsStateWithLifecycle()
    val savedFullPackages by AppStudyRepository.savedBookPackages.collectAsStateWithLifecycle()

    // Initialize initial default study package on first load
    LaunchedEffect(Unit) {
        if (currentStudyPackage == null) {
            coroutineScope.launch {
                val res = BookIntelligenceService.analyzeDocumentProgressive(
                    docTitle = "NCERT_Class10_Math_Ch4.pdf",
                    academicClass = AcademicClass.CLASS_10,
                    board = EducationBoard.CBSE,
                    subject = SubjectCategory.MATHEMATICS,
                    language = StudyLanguage.ENGLISH,
                    fileSizeFormatted = "3.8 MB",
                    estimatedPages = 24,
                    isLargeFile = false,
                    onProgressUpdate = { stage, desc, prog, preliminary ->
                        currentStage = stage
                        stageDescription = desc
                        progressFraction = prog
                        if (preliminary != null) currentStudyPackage = preliminary
                    }
                )
                res.onSuccess { pkg ->
                    currentStudyPackage = pkg
                    AppStudyRepository.saveFullBookPackage(pkg)
                }
            }
        }
    }

    // PDF File Picker Launcher
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val (name, size) = queryFileInfo(context, uri)
                val stream = context.contentResolver.openInputStream(uri)
                val validation = BookIntelligenceService.validatePdf(name, size, stream)
                stream?.close()

                when (validation) {
                    is PdfValidationResult.Invalid -> {
                        validationError = validation.reason
                    }
                    is PdfValidationResult.Valid -> {
                        validationError = null
                        uploadedFileName = validation.fileName
                        uploadedFileSizeFormatted = validation.formattedSize
                        uploadedPageEstimate = validation.estimatedPages
                        isLargeFileWarning = validation.isLargeFile
                        selectedPresetDoc = validation.fileName

                        // Automatically launch analysis pipeline for the selected file
                        isProcessing = true
                        coroutineScope.launch {
                            val result = BookIntelligenceService.analyzeDocumentProgressive(
                                docTitle = validation.fileName,
                                academicClass = selectedClass,
                                board = selectedBoard,
                                subject = selectedSubject,
                                language = selectedLanguage,
                                fileSizeFormatted = validation.formattedSize,
                                estimatedPages = validation.estimatedPages,
                                isLargeFile = validation.isLargeFile,
                                onProgressUpdate = { stage, desc, prog, preliminary ->
                                    currentStage = stage
                                    stageDescription = desc
                                    progressFraction = prog
                                    if (preliminary != null) currentStudyPackage = preliminary
                                }
                            )
                            isProcessing = false
                            result.onSuccess { pkg ->
                                currentStudyPackage = pkg
                                AppStudyRepository.saveFullBookPackage(pkg)
                            }.onFailure { err ->
                                validationError = "Document analysis failed: ${err.message}"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                validationError = "Error accessing selected document: ${e.message}"
            }
        }
    }

    val isPackSaved = currentStudyPackage != null && savedPackIds.contains(currentStudyPackage?.id)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        // Top Header and Saved Packages Shortcut
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Book Intelligence Engine",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
                Text(
                    text = "AI Textbook Synthesizer & Chapter Mastery",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = { showSavedPackagesDialog = true },
                colors = ButtonDefaults.textButtonColors(contentColor = AsiCyan)
            ) {
                Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Saved Packs (${savedFullPackages.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Student Curriculum Selection Card
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
                                text = "1. Student Curriculum Target",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan
                            )
                            Surface(
                                color = AsiNavyBorder,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${selectedClass.displayName} • ${selectedBoard.code}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AsiTextPrimaryDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Class Row
                        Text("Select Class:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AcademicClass.values().forEach { ac ->
                                val selected = selectedClass == ac
                                Surface(
                                    color = if (selected) AsiCyan else AsiNavyDarkest,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedClass = ac }
                                ) {
                                    Text(
                                        text = ac.displayName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (selected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .padding(vertical = 6.dp)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Subject & Board Selection Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Subject dropdown / chips
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Subject:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    listOf(
                                        SubjectCategory.MATHEMATICS to "Maths",
                                        SubjectCategory.SCIENCE to "Science",
                                        SubjectCategory.PHYSICS to "Physics"
                                    ).forEach { (cat, label) ->
                                        val sel = selectedSubject == cat
                                        Surface(
                                            color = if (sel) AsiBlueAccent else AsiNavyDarkest,
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedSubject = cat }
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                                color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .padding(vertical = 6.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            }

                            // Language toggle
                            Column(modifier = Modifier.weight(0.8f)) {
                                Text("Language:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    StudyLanguage.values().forEach { lang ->
                                        val sel = selectedLanguage == lang
                                        Surface(
                                            color = if (sel) AsiEmerald else AsiNavyDarkest,
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedLanguage = lang }
                                        ) {
                                            Text(
                                                text = if (lang == StudyLanguage.ENGLISH) "EN" else "हिंदी",
                                                fontSize = 11.sp,
                                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                                color = if (sel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .padding(vertical = 6.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Document Selection & Upload Workspace Card
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
                                text = "2. PDF Document Source",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan
                            )
                            Surface(
                                color = AsiEmerald.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PDF Parser & OCR Active",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsiEmerald,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Upload any authorized textbook PDF or select curriculum-aligned NCERT textbook chapters:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Upload Button & File Picker
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    pdfPickerLauncher.launch("application/pdf")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("upload_pdf_button")
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload PDF File", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedPresetDoc = "NCERT_Class10_Math_Ch4.pdf"
                                    runPipelineForDoc(
                                        doc = "NCERT_Class10_Math_Ch4.pdf",
                                        cClass = selectedClass,
                                        board = selectedBoard,
                                        subj = selectedSubject,
                                        lang = selectedLanguage,
                                        coroutineScope = coroutineScope,
                                        onStage = { s, d, p ->
                                            currentStage = s
                                            stageDescription = d
                                            progressFraction = p
                                        },
                                        onDone = { isProcessing = false; currentStudyPackage = it },
                                        onStart = { isProcessing = true }
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(AsiCyan)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = AsiCyan)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reprocess Active", fontSize = 12.sp, color = AsiCyan)
                            }
                        }

                        // Validation Error Display
                        if (validationError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = AsiRuby.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Error, contentDescription = null, tint = AsiRuby, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(validationError ?: "", style = MaterialTheme.typography.bodySmall, color = AsiRuby)
                                }
                            }
                        }

                        // Large File Warning Notice
                        if (isLargeFileWarning) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = AsiAmber.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = AsiAmber, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Large document detected. Bounded processing applied at chapter granularity.", fontSize = 11.sp, color = AsiAmber)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Authorized NCERT Chapter Presets:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Preset Chapter Chips
                        val presets = listOf(
                            "NCERT_Class10_Math_Ch4.pdf" to "Class 10 Maths Ch 4 (Quadratic Equations)",
                            "NCERT_Class10_Science_Ch10.pdf" to "Class 10 Science Ch 10 (Light Optics)",
                            "NCERT_Class10_Science_Ch12.pdf" to "Class 10 Science Ch 12 (Electricity)",
                            "NCERT_Class9_Science_Ch08.pdf" to "Class 9 Science Ch 8 (Motion)"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            presets.forEach { (docFile, docLabel) ->
                                val isSelected = selectedPresetDoc == docFile
                                Surface(
                                    color = if (isSelected) AsiNavyCardElevated else AsiNavyDarkest,
                                    shape = RoundedCornerShape(8.dp),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedPresetDoc = docFile
                                            uploadedFileName = docFile
                                            runPipelineForDoc(
                                                doc = docFile,
                                                cClass = selectedClass,
                                                board = selectedBoard,
                                                subj = if (docFile.contains("Math")) SubjectCategory.MATHEMATICS else SubjectCategory.PHYSICS,
                                                lang = selectedLanguage,
                                                coroutineScope = coroutineScope,
                                                onStage = { s, d, p ->
                                                    currentStage = s
                                                    stageDescription = d
                                                    progressFraction = p
                                                },
                                                onDone = { isProcessing = false; currentStudyPackage = it },
                                                onStart = { isProcessing = true }
                                            )
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Description,
                                                contentDescription = null,
                                                tint = if (isSelected) AsiCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = docLabel,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = if (isSelected) AsiCyan else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Text(
                                            text = if (isSelected) "Active ✓" else "Load",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) AsiEmerald else AsiTextSecondaryDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Pipeline Progress & Status Stepper Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AsiNavyCardElevated),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Document Processing Pipeline",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AsiCyan
                            )
                            Text(
                                text = "${(progressFraction * 100).toInt()}% Complete",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (progressFraction >= 1.0f) AsiEmerald else AsiCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (progressFraction >= 1.0f) AsiEmerald else AsiCyan,
                            trackColor = AsiNavyDarkest
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stageDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stage checklist
                        val pipelineStages = listOf(
                            "Stage 1: PDF OCR & Ingestion (Magic Header Validated)",
                            "Stage 2: Semantic Chapter Boundary & Topic Extraction",
                            "Stage 3: Gemini 3.5 Flash Cognitive Structure Analysis",
                            "Stage 4: Mathematical Formula & Definition Verification",
                            "Stage 5: Practice Questions & Revision Checklist Synthesis"
                        )

                        pipelineStages.forEachIndexed { idx, stageLabel ->
                            val isCompleted = currentStage > idx + 1 || (currentStage == idx + 1 && progressFraction >= 1.0f)
                            val isCurrent = currentStage == idx + 1 && progressFraction < 1.0f

                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.CheckCircle else if (isCurrent) Icons.Default.Sync else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isCompleted) AsiEmerald else if (isCurrent) AsiCyan else AsiTextSecondaryDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = stageLabel,
                                    fontSize = 11.sp,
                                    color = if (isCompleted) MaterialTheme.colorScheme.onSurface else if (isCurrent) AsiCyan else AsiTextSecondaryDark
                                )
                            }
                        }
                    }
                }
            }

            // 4. Study Package Results (Tabs & Contents)
            if (currentStudyPackage != null) {
                val pkg = currentStudyPackage!!

                // Distinguishing Badges (AI Generated vs Independently Verified)
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
                                Surface(
                                    color = AsiCyan.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = pkg.aiEngineNotice,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AsiCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    color = AsiEmerald.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = pkg.verificationNotice,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AsiEmerald,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Document: ${pkg.documentTitle}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "Scope: ${pkg.chaptersIdentified.firstOrNull()?.title ?: "Full Chapter"} • ${pkg.fileSizeFormatted} • ~${pkg.totalPages} Pages • Study Time: ${pkg.estimatedReadTimeMinutes} mins",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Save to Student Account Action Button
                            Button(
                                onClick = {
                                    AppStudyRepository.saveFullBookPackage(pkg)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPackSaved) AsiNavyDarkest else AsiEmerald,
                                    contentColor = if (isPackSaved) AsiEmerald else AsiNavyDarkest
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("save_study_pack_button")
                            ) {
                                Icon(
                                    if (isPackSaved) Icons.Default.CheckCircle else Icons.Default.DownloadDone,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if (isPackSaved) "Saved in Student Account ✓" else "Save Complete Study Package to Account",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Tab Selector for Chapter Package
                item {
                    val tabs = listOf(
                        "Overview & Notes",
                        "Formula Sheet (${pkg.extractedFormulas.size})",
                        "Definitions & Traps",
                        "Practice Bank (${pkg.generatedQuestions.size})",
                        "Chapter Test",
                        "Revision Checklist"
                    )

                    ScrollableTabRow(
                        selectedTabIndex = selectedPackageTab,
                        containerColor = AsiNavyDarkest,
                        contentColor = AsiCyan,
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed { index, tabTitle ->
                            Tab(
                                selected = selectedPackageTab == index,
                                onClick = { selectedPackageTab = index },
                                text = {
                                    Text(
                                        text = tabTitle,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedPackageTab == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }

                // Render selected tab contents
                when (selectedPackageTab) {
                    0 -> {
                        // Overview & Notes Tab
                        item {
                            PackageNotesView(pkg)
                        }
                    }
                    1 -> {
                        // Formula Sheet Tab
                        item {
                            PackageFormulaSheetView(pkg)
                        }
                    }
                    2 -> {
                        // Definitions & Traps Tab
                        item {
                            PackageDefinitionsView(pkg)
                        }
                    }
                    3 -> {
                        // Practice Questions Tab
                        item {
                            PackagePracticeQuestionsView(pkg)
                        }
                    }
                    4 -> {
                        // Chapter Test Tab
                        item {
                            PackageChapterTestView(pkg)
                        }
                    }
                    5 -> {
                        // Revision Checklist Tab
                        item {
                            PackageRevisionChecklistView(pkg)
                        }
                    }
                }
            }
        }
    }

    // Saved Packages Dialog
    if (showSavedPackagesDialog) {
        AlertDialog(
            onDismissRequest = { showSavedPackagesDialog = false },
            title = {
                Text("Saved Study Packages", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                ) {
                    if (savedFullPackages.isEmpty()) {
                        Text("No saved study packages yet. Analyze and save any PDF document.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(savedFullPackages) { savedPkg ->
                                Surface(
                                    color = AsiNavyDarkest,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            currentStudyPackage = savedPkg
                                            selectedPresetDoc = savedPkg.documentTitle
                                            showSavedPackagesDialog = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(savedPkg.documentTitle, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = AsiCyan)
                                            Text("${savedPkg.academicClass.displayName} • ${savedPkg.subject.displayName} • ${savedPkg.chaptersIdentified.firstOrNull()?.title ?: "Chapter"}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        IconButton(
                                            onClick = { AppStudyRepository.removeBookPackage(savedPkg.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AsiRuby, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedPackagesDialog = false }) {
                    Text("Close", color = AsiCyan)
                }
            },
            containerColor = AsiNavyCard
        )
    }
}

// ----------------------------------------------------
// TAB 0: OVERVIEW & NOTES VIEW
// ----------------------------------------------------
@Composable
fun PackageNotesView(pkg: BookStudyPackage) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Topic Breakdown & Page References
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
                        text = "Extracted Chapter Structure & Topics",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Text(
                        text = pkg.chaptersIdentified.firstOrNull()?.pageRange ?: "All Pages",
                        fontSize = 11.sp,
                        color = AsiAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                pkg.chaptersIdentified.forEach { ch ->
                    Text("Chapter ${ch.number}: ${ch.title}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    ch.topics.forEach { t ->
                        Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = AsiCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(t, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(ch.coreConceptSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // 1-Minute Rapid Recall Notes
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
                        text = "1-Minute Rapid Recall",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiAmber
                    )
                    Text("High Yield", fontSize = 11.sp, color = AsiAmber, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = pkg.shortNotes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Detailed Conceptual Notes
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Detailed Topic Notes & Conceptual Foundations",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = pkg.detailedNotes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// ----------------------------------------------------
// TAB 1: FORMULA SHEET VIEW
// ----------------------------------------------------
@Composable
fun PackageFormulaSheetView(pkg: BookStudyPackage) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        pkg.extractedFormulas.forEach { f ->
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
                        Text(f.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                        Surface(
                            color = AsiEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(f.verificationStatus, fontSize = 10.sp, color = AsiEmerald, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = AsiNavyDarkest,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = f.expression,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Symbol Meanings:", style = MaterialTheme.typography.labelSmall, color = AsiTextPrimaryDark, fontWeight = FontWeight.Bold)
                    f.symbolsMeaning.forEach { (sym, mean) ->
                        Text("• $sym: $mean", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Conditions: ${f.conditions}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("SI Units: ${f.unitAndDimension}", style = MaterialTheme.typography.bodySmall, color = AsiAmber)

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = AsiNavyCardElevated,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Worked Textbook Example:", style = MaterialTheme.typography.labelSmall, color = AsiCyan, fontWeight = FontWeight.Bold)
                            Text(f.workedExample, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 2: DEFINITIONS & TRAPS VIEW
// ----------------------------------------------------
@Composable
fun PackageDefinitionsView(pkg: BookStudyPackage) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Key Textbook Definitions",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
                Spacer(modifier = Modifier.height(8.dp))

                pkg.importantDefinitions.forEach { (term, def) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(term, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = AsiAmber)
                        Text(def, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                    HorizontalDivider(color = AsiNavyBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 3: PRACTICE BANK VIEW
// ----------------------------------------------------
@Composable
fun PackagePracticeQuestionsView(pkg: BookStudyPackage) {
    var selectedDifficultyFilter by remember { mutableStateOf("All") }
    var expandedQuestionId by remember { mutableStateOf<String?>(null) }

    val filteredQuestions = remember(pkg.generatedQuestions, selectedDifficultyFilter) {
        if (selectedDifficultyFilter == "All") {
            pkg.generatedQuestions
        } else {
            pkg.generatedQuestions.filter { it.difficulty.equals(selectedDifficultyFilter, ignoreCase = true) }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Difficulty filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Easy", "Medium", "Difficult", "Tricky").forEach { diff ->
                val sel = selectedDifficultyFilter == diff
                Surface(
                    color = if (sel) AsiCyan else AsiNavyDarkest,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedDifficultyFilter = diff }
                ) {
                    Text(
                        text = diff,
                        fontSize = 11.sp,
                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                        color = if (sel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        filteredQuestions.forEach { q ->
            val isExpanded = expandedQuestionId == q.id
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
                        Surface(
                            color = when (q.difficulty.lowercase()) {
                                "easy" -> AsiEmerald.copy(alpha = 0.2f)
                                "medium" -> AsiCyan.copy(alpha = 0.2f)
                                "difficult" -> AsiAmber.copy(alpha = 0.2f)
                                else -> AsiRuby.copy(alpha = 0.2f)
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${q.difficulty} • ${q.type.displayName}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (q.difficulty.lowercase()) {
                                    "easy" -> AsiEmerald
                                    "medium" -> AsiCyan
                                    "difficult" -> AsiAmber
                                    else -> AsiRuby
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (q.sourcePageRef.isNotBlank()) {
                            Text(
                                text = q.sourcePageRef,
                                fontSize = 11.sp,
                                color = AsiAmber,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (q.options.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        q.options.forEach { opt ->
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Expandable Solution Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedQuestionId = if (isExpanded) null else q.id
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "Hide Detailed Solution" else "View Step-by-Step Solution & Trap Alert",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = AsiCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Surface(
                                color = AsiNavyDarkest,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Correct Answer: ${q.correctAnswer}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = AsiEmerald)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(q.stepByStepSolution, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

                                    if (q.commonMisconceptions.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Surface(
                                            color = AsiRuby.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(modifier = Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Warning, contentDescription = null, tint = AsiRuby, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Common Trap: ${q.commonMisconceptions}", fontSize = 11.sp, color = AsiRuby)
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
    }
}

// ----------------------------------------------------
// TAB 4: CHAPTER MASTERY TEST VIEW
// ----------------------------------------------------
@Composable
fun PackageChapterTestView(pkg: BookStudyPackage) {
    val questions = pkg.chapterTests.ifEmpty { pkg.generatedQuestions.take(4) }
    var selectedAnswers by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isSubmitted by remember { mutableStateOf(false) }
    var testScore by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AsiNavyCardElevated)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chapter Diagnostic Mastery Test",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Text(
                        text = "${questions.size} Questions",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsiAmber
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Assess your conceptual understanding of this chapter. Instant grading with remediation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = if (testScore >= questions.size / 2) AsiEmerald.copy(alpha = 0.2f) else AsiRuby.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Score: $testScore / ${questions.size} (${(testScore * 100) / questions.size}%)", fontWeight = FontWeight.Bold, color = if (testScore >= questions.size / 2) AsiEmerald else AsiRuby)
                            Text(if (testScore >= questions.size / 2) "Concept Mastered!" else "Needs Revision", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        questions.forEachIndexed { idx, q ->
            val userAns = selectedAnswers[q.id]
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Q${idx + 1}. ${q.question}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (q.options.isNotEmpty()) {
                        q.options.forEach { opt ->
                            val isPicked = userAns == opt
                            val isCorrectOpt = q.correctAnswer.startsWith(opt.take(2)) || opt.contains(q.correctAnswer)

                            Surface(
                                color = when {
                                    isSubmitted && isCorrectOpt -> AsiEmerald.copy(alpha = 0.25f)
                                    isSubmitted && isPicked && !isCorrectOpt -> AsiRuby.copy(alpha = 0.25f)
                                    isPicked -> AsiCyan.copy(alpha = 0.2f)
                                    else -> AsiNavyDarkest
                                },
                                shape = RoundedCornerShape(6.dp),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(if (isPicked) AsiCyan else AsiNavyBorder)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable(enabled = !isSubmitted) {
                                        selectedAnswers = selectedAnswers + (q.id to opt)
                                    }
                            ) {
                                Text(
                                    text = opt,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    } else {
                        // Text / numerical response
                        Text("Expected: ${q.correctAnswer}", style = MaterialTheme.typography.bodySmall, color = if (isSubmitted) AsiEmerald else AsiTextSecondaryDark)
                    }

                    if (isSubmitted) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Solution: ${q.stepByStepSolution}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        if (!isSubmitted) {
            Button(
                onClick = {
                    var score = 0
                    questions.forEach { q ->
                        val ans = selectedAnswers[q.id].orEmpty()
                        if (q.options.isNotEmpty()) {
                            if (ans.startsWith(q.correctAnswer.take(2)) || ans.contains(q.correctAnswer)) {
                                score++
                            }
                        } else {
                            if (ans.isNotBlank()) score++
                        }
                    }
                    testScore = score
                    isSubmitted = true
                    AppStudyRepository.recordTestScore("${pkg.chaptersIdentified.firstOrNull()?.title ?: "Book"} Chapter Test", score, questions.size)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AsiCyan, contentColor = AsiNavyDarkest),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Chapter Test", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ----------------------------------------------------
// TAB 5: REVISION CHECKLIST VIEW
// ----------------------------------------------------
@Composable
fun PackageRevisionChecklistView(pkg: BookStudyPackage) {
    var checkedItems by remember { mutableStateOf<Set<Int>>(emptySet()) }

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
                    text = "Spaced Repetition Checklist",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AsiCyan
                )
                Text("${checkedItems.size} / ${pkg.revisionChecklist.size} Done", fontSize = 11.sp, color = AsiEmerald, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Track your mastery of core exam traps and derivations. Checking items integrates with your personal study planner:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            pkg.revisionChecklist.forEachIndexed { idx, itemText ->
                val isDone = checkedItems.contains(idx)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            checkedItems = if (isDone) checkedItems - idx else checkedItems + idx
                            if (!isDone) {
                                AppStudyRepository.addRevisionItem(
                                    topic = itemText.take(40),
                                    chapter = pkg.chaptersIdentified.firstOrNull()?.title ?: "Book Chapter",
                                    dueDate = "Reviewed ✓",
                                    priority = "Normal"
                                )
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDone,
                        onCheckedChange = {
                            checkedItems = if (isDone) checkedItems - idx else checkedItems + idx
                        },
                        colors = CheckboxDefaults.colors(checkedColor = AsiCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = itemText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium
                        ),
                        color = if (isDone) AsiTextSecondaryDark else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// Helper to query file name and size from content Uri
private fun queryFileInfo(context: Context, uri: Uri): Pair<String, Long> {
    var name = "Document.pdf"
    var size = 0L
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIdx >= 0) name = cursor.getString(nameIdx) ?: name
                if (sizeIdx >= 0) size = cursor.getLong(sizeIdx)
            }
        }
    } catch (_: Exception) {}
    return name to size
}

private fun runPipelineForDoc(
    doc: String,
    cClass: AcademicClass,
    board: EducationBoard,
    subj: SubjectCategory,
    lang: StudyLanguage,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onStage: (Int, String, Float) -> Unit,
    onDone: (BookStudyPackage) -> Unit,
    onStart: () -> Unit
) {
    onStart()
    coroutineScope.launch {
        val result = BookIntelligenceService.analyzeDocumentProgressive(
            docTitle = doc,
            academicClass = cClass,
            board = board,
            subject = subj,
            language = lang,
            fileSizeFormatted = "3.8 MB",
            estimatedPages = 24,
            isLargeFile = false,
            onProgressUpdate = { stage, desc, prog, _ ->
                onStage(stage, desc, prog)
            }
        )
        result.onSuccess { pkg ->
            AppStudyRepository.saveFullBookPackage(pkg)
            onDone(pkg)
        }
    }
}
