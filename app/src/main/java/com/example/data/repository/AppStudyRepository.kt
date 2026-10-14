package com.example.data.repository

import com.example.data.model.BookStudyPackage
import com.example.data.model.FormulaItem
import com.example.data.model.SmartNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RevisionRecord(
    val id: String,
    val topic: String,
    val chapter: String,
    val dueDate: String,
    val isDone: Boolean = false,
    val priority: String = "Normal" // "High", "Normal", "Low"
)

data class TestScoreRecord(
    val testTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val timestamp: Long = System.currentTimeMillis()
)

object AppStudyRepository {

    private val _bookmarkedFormulaIds = MutableStateFlow<Set<String>>(setOf("f_quad_formula", "f_ohms_law"))
    val bookmarkedFormulaIds: StateFlow<Set<String>> = _bookmarkedFormulaIds.asStateFlow()

    private val _savedNoteIds = MutableStateFlow<Set<String>>(setOf("note_quad_10"))
    val savedNoteIds: StateFlow<Set<String>> = _savedNoteIds.asStateFlow()

    private val _savedPackIds = MutableStateFlow<Set<String>>(setOf("NCERT_Class10_Math_Ch4.pdf"))
    val savedPackIds: StateFlow<Set<String>> = _savedPackIds.asStateFlow()

    private val _savedBookPackages = MutableStateFlow<List<BookStudyPackage>>(emptyList())
    val savedBookPackages: StateFlow<List<BookStudyPackage>> = _savedBookPackages.asStateFlow()

    private val _revisionItems = MutableStateFlow<List<RevisionRecord>>(
        listOf(
            RevisionRecord("rev_1", "Quadratic Equations — Discriminant Test", "Maths Ch 4", "Due Today 🔥", false, "High"),
            RevisionRecord("rev_2", "Ohm's Law & Resistors in Parallel", "Science Ch 12", "Due in 2 Days", false, "Normal"),
            RevisionRecord("rev_3", "Convex Lens Ray Diagrams & Signs", "Science Ch 10", "Due in 5 Days", false, "Normal"),
            RevisionRecord("rev_4", "Balancing Chemical Redox Reactions", "Science Ch 1", "Scheduled next week", false, "Low")
        )
    )
    val revisionItems: StateFlow<List<RevisionRecord>> = _revisionItems.asStateFlow()

    private val _testScores = MutableStateFlow<List<TestScoreRecord>>(
        listOf(
            TestScoreRecord("Quadratic Equations Chapter Test", 4, 5, 80)
        )
    )
    val testScores: StateFlow<List<TestScoreRecord>> = _testScores.asStateFlow()

    fun toggleFormulaBookmark(formulaId: String) {
        val current = _bookmarkedFormulaIds.value.toMutableSet()
        if (current.contains(formulaId)) {
            current.remove(formulaId)
        } else {
            current.add(formulaId)
        }
        _bookmarkedFormulaIds.value = current
    }

    fun toggleNoteSaved(noteId: String) {
        val current = _savedNoteIds.value.toMutableSet()
        if (current.contains(noteId)) {
            current.remove(noteId)
        } else {
            current.add(noteId)
        }
        _savedNoteIds.value = current
    }

    fun saveStudyPack(packId: String) {
        val current = _savedPackIds.value.toMutableSet()
        current.add(packId)
        _savedPackIds.value = current
    }

    fun saveFullBookPackage(pkg: BookStudyPackage) {
        val currentList = _savedBookPackages.value.filter { it.id != pkg.id }
        _savedBookPackages.value = listOf(pkg) + currentList
        saveStudyPack(pkg.id)
    }

    fun removeBookPackage(id: String) {
        _savedBookPackages.value = _savedBookPackages.value.filter { it.id != id }
        val currentPacks = _savedPackIds.value.toMutableSet()
        currentPacks.remove(id)
        _savedPackIds.value = currentPacks
    }


    fun recordTestScore(testTitle: String, score: Int, total: Int) {
        val pct = if (total > 0) (score * 100) / total else 0
        val record = TestScoreRecord(testTitle, score, total, pct)
        _testScores.value = listOf(record) + _testScores.value

        // If score is low, add automated revision reminder
        if (pct < 75) {
            addRevisionItem(
                topic = "$testTitle — Review Weak Points",
                chapter = "Remedial Practice",
                dueDate = "Due Tomorrow ⚡",
                priority = "High"
            )
        }
    }

    fun addRevisionItem(topic: String, chapter: String, dueDate: String, priority: String = "Normal") {
        val newRecord = RevisionRecord("rev_${System.currentTimeMillis()}", topic, chapter, dueDate, false, priority)
        _revisionItems.value = listOf(newRecord) + _revisionItems.value
    }

    fun toggleRevisionDone(id: String) {
        _revisionItems.value = _revisionItems.value.map {
            if (it.id == id) it.copy(isDone = !it.isDone) else it
        }
    }
}
