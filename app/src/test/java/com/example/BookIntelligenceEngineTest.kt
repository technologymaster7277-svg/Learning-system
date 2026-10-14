package com.example

import com.example.data.ai.BookIntelligenceService
import com.example.data.ai.PdfValidationResult
import com.example.data.model.*
import com.example.data.repository.AppStudyRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BookIntelligenceEngineTest {

    @Test
    fun testPdfValidation_validPdfHeader() {
        val validPdfBytes = "%PDF-1.4 sample content".toByteArray()
        val stream = ByteArrayInputStream(validPdfBytes)
        val result = BookIntelligenceService.validatePdf(
            fileName = "NCERT_Class10_Math_Ch4.pdf",
            fileSize = validPdfBytes.size.toLong(),
            stream = stream
        )

        assertTrue("Result should be valid for genuine PDF", result is PdfValidationResult.Valid)
        val valid = result as PdfValidationResult.Valid
        assertEquals("NCERT_Class10_Math_Ch4.pdf", valid.fileName)
        assertEquals(validPdfBytes.size.toLong(), valid.sizeBytes)
        assertFalse(valid.isLargeFile)
    }

    @Test
    fun testPdfValidation_rejectsInvalidExtension() {
        val result = BookIntelligenceService.validatePdf(
            fileName = "notes.docx",
            fileSize = 1024,
            stream = null
        )
        assertTrue("Non-PDF should be invalid", result is PdfValidationResult.Invalid)
        assertTrue((result as PdfValidationResult.Invalid).reason.contains("Only authorized PDF"))
    }

    @Test
    fun testPdfValidation_rejectsEmptyFile() {
        val result = BookIntelligenceService.validatePdf(
            fileName = "empty.pdf",
            fileSize = 0,
            stream = null
        )
        assertTrue("0-byte file should be invalid", result is PdfValidationResult.Invalid)
        assertTrue((result as PdfValidationResult.Invalid).reason.contains("0 bytes"))
    }

    @Test
    fun testPdfValidation_rejectsExcessiveFileSize() {
        val oversizeBytes = 40 * 1024 * 1024L // 40 MB
        val result = BookIntelligenceService.validatePdf(
            fileName = "giant_book.pdf",
            fileSize = oversizeBytes,
            stream = null
        )
        assertTrue("Over 35MB should be invalid", result is PdfValidationResult.Invalid)
        assertTrue((result as PdfValidationResult.Invalid).reason.contains("too large"))
    }

    @Test
    fun testAnalyzeDocumentProgressive_generatesCompletePackage() = runBlocking {
        var finalStage = 0
        var finalProg = 0f

        val result = BookIntelligenceService.analyzeDocumentProgressive(
            docTitle = "NCERT_Class10_Math_Ch4.pdf",
            academicClass = AcademicClass.CLASS_10,
            board = EducationBoard.CBSE,
            subject = SubjectCategory.MATHEMATICS,
            language = StudyLanguage.ENGLISH,
            fileSizeFormatted = "3.8 MB",
            estimatedPages = 24,
            isLargeFile = false,
            onProgressUpdate = { stage, _, prog, _ ->
                finalStage = stage
                finalProg = prog
            }
        )

        assertTrue("Analysis should succeed", result.isSuccess)
        val pkg = result.getOrThrow()

        // Verify package structure and content
        assertEquals(5, finalStage)
        assertEquals(1.0f, finalProg)
        assertEquals("NCERT_Class10_Math_Ch4.pdf", pkg.documentTitle)
        assertTrue("Must extract chapter structure", pkg.chaptersIdentified.isNotEmpty())
        assertTrue("Must generate formulas", pkg.extractedFormulas.isNotEmpty())
        assertTrue("Must generate practice questions", pkg.generatedQuestions.isNotEmpty())
        assertTrue("Must have short notes", pkg.shortNotes.isNotBlank())
        assertTrue("Must have detailed notes", pkg.detailedNotes.isNotBlank())
        assertTrue("Must have definitions", pkg.importantDefinitions.isNotEmpty())
        assertTrue("Must have revision checklist", pkg.revisionChecklist.isNotEmpty())
        assertTrue("Must have chapter tests", pkg.chapterTests.isNotEmpty())

        // Verify difficulty categories exist in practice questions
        val difficulties = pkg.generatedQuestions.map { it.difficulty }
        assertTrue("Should contain Medium questions", difficulties.contains("Medium"))
        assertTrue("Questions must have source page references", pkg.generatedQuestions.any { it.sourcePageRef.isNotBlank() })

        // Verify verification badges
        assertTrue(pkg.verificationLabel.isNotBlank())
        assertTrue(pkg.aiEngineNotice.contains("Gemini"))
    }

    @Test
    fun testSaveAndRetrieveBookPackage() {
        val testPkg = BookStudyPackage(
            id = "test_pkg_unique_123",
            documentTitle = "Custom_Chapter_Notes.pdf",
            academicClass = AcademicClass.CLASS_10,
            board = EducationBoard.CBSE,
            subject = SubjectCategory.SCIENCE,
            language = StudyLanguage.ENGLISH,
            fileSizeFormatted = "2.1 MB",
            totalPages = 18,
            chaptersIdentified = emptyList(),
            extractedFormulas = emptyList(),
            generatedQuestions = emptyList(),
            shortNotes = "Short notes test",
            detailedNotes = "Detailed notes test",
            importantDefinitions = emptyList(),
            revisionChecklist = emptyList()
        )

        AppStudyRepository.saveFullBookPackage(testPkg)

        val savedList = AppStudyRepository.savedBookPackages.value
        val savedIds = AppStudyRepository.savedPackIds.value

        assertTrue("Saved packages must contain testPkg", savedList.any { it.id == "test_pkg_unique_123" })
        assertTrue("Saved IDs set must contain ID", savedIds.contains("test_pkg_unique_123"))

        // Test removal
        AppStudyRepository.removeBookPackage("test_pkg_unique_123")
        assertFalse("Package should be removed", AppStudyRepository.savedBookPackages.value.any { it.id == "test_pkg_unique_123" })
        assertFalse("ID should be removed from set", AppStudyRepository.savedPackIds.value.contains("test_pkg_unique_123"))
    }
}
