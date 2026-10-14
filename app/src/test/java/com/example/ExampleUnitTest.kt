package com.example

import com.example.data.repository.AppStudyRepository
import com.example.data.repository.CurriculumRepository
import com.example.data.repository.FormulaSolvers
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testQuadraticSolver_twoDistinctRoots() {
        val result = FormulaSolvers.solveQuadratic(2.0, -7.0, 3.0)
        assertEquals(25.0, result.discriminant, 0.001)
        assertTrue(result.isReal)
        assertEquals(3.0, result.root1 ?: 0.0, 0.001)
        assertEquals(0.5, result.root2 ?: 0.0, 0.001)
    }

    @Test
    fun testQuadraticSolver_equalRoots() {
        val result = FormulaSolvers.solveQuadratic(1.0, -4.0, 4.0)
        assertEquals(0.0, result.discriminant, 0.001)
        assertTrue(result.isReal)
        assertEquals(2.0, result.root1 ?: 0.0, 0.001)
        assertEquals(2.0, result.root2 ?: 0.0, 0.001)
    }

    @Test
    fun testQuadraticSolver_noRealRoots() {
        val result = FormulaSolvers.solveQuadratic(1.0, 2.0, 5.0)
        assertTrue(result.discriminant < 0)
        assertFalse(result.isReal)
        assertNull(result.root1)
        assertNull(result.root2)
    }

    @Test
    fun testOhmSolver_calculation() {
        val result = FormulaSolvers.solveOhm(220.0, null, 44.0)
        assertEquals(5.0, result.current, 0.01)
        assertEquals(1100.0, result.powerWatts, 0.01)
    }

    @Test
    fun testLensSolver_realInvertedImage() {
        val result = FormulaSolvers.solveLens(15.0, -30.0)
        assertEquals(30.0, result.imageDistance, 0.01)
        assertEquals(-1.0, result.magnification, 0.01)
        assertTrue(result.isReal)
        assertTrue(result.isInverted)
    }

    @Test
    fun testLensSolver_virtualErectImage() {
        val result = FormulaSolvers.solveLens(15.0, -10.0)
        assertEquals(-30.0, result.imageDistance, 0.01)
        assertEquals(3.0, result.magnification, 0.01)
        assertFalse(result.isReal)
        assertFalse(result.isInverted)
    }

    @Test
    fun testCurriculumRepository_multipleSmartNotes() {
        val notes = CurriculumRepository.allSmartNotes
        assertTrue(notes.size >= 3)
        val lightNote = CurriculumRepository.getSmartNoteForChapter("sci_10_ch10")
        assertEquals("Light: Reflection & Refraction — Comprehensive Guide", lightNote.title)
    }

    @Test
    fun testAppStudyRepository_stateActions() {
        AppStudyRepository.toggleFormulaBookmark("f_lens_formula")
        assertTrue(AppStudyRepository.bookmarkedFormulaIds.value.contains("f_lens_formula"))

        AppStudyRepository.recordTestScore("Diagnostic Test", 5, 5)
        assertEquals(1, AppStudyRepository.testScores.value.filter { it.testTitle == "Diagnostic Test" }.size)
    }
}
