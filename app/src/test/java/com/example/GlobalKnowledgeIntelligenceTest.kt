package com.example

import com.example.data.model.DisciplineGroup
import com.example.data.model.EducationTier
import com.example.data.repository.GlobalKnowledgeRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GlobalKnowledgeIntelligenceTest {

    @Test
    fun testUniversalTaxonomyTiers_coversSchoolToPhD() {
        val tiers = GlobalKnowledgeRepository.allTiers

        assertEquals("Must cover exactly 10 global education tiers", 10, tiers.size)

        // Verify key progression levels
        assertTrue("Must include Primary (1-5)", tiers.contains(EducationTier.PRIMARY))
        assertTrue("Must include Secondary (9-10)", tiers.contains(EducationTier.SECONDARY))
        assertTrue("Must include Senior Secondary (11-12)", tiers.contains(EducationTier.SENIOR_SECONDARY))
        assertTrue("Must include Vocational & Polytechnic", tiers.contains(EducationTier.VOCATIONAL_DIPLOMA))
        assertTrue("Must include Undergraduate (B.Tech/B.Sc)", tiers.contains(EducationTier.UNDERGRADUATE))
        assertTrue("Must include Postgraduate (M.Tech/M.Sc)", tiers.contains(EducationTier.POSTGRADUATE))
        assertTrue("Must include Doctoral & PhD", tiers.contains(EducationTier.DOCTORAL_PHD))
        assertTrue("Must include Competitive Examinations", tiers.contains(EducationTier.COMPETITIVE_EXAMS))
    }

    @Test
    fun testUniversalDisciplineGroups_coversAll11Groups() {
        val disciplines = GlobalKnowledgeRepository.allDisciplines

        assertEquals("Must cover 11 universal discipline groups", 11, disciplines.size)

        val codes = disciplines.map { it.code }
        assertTrue(codes.contains("MATH"))
        assertTrue(codes.contains("SCI"))
        assertTrue(codes.contains("HUM"))
        assertTrue(codes.contains("LANG"))
        assertTrue(codes.contains("COMM"))
        assertTrue(codes.contains("CS"))
        assertTrue(codes.contains("ENG"))
        assertTrue(codes.contains("HEALTH"))
        assertTrue(codes.contains("LAW"))
        assertTrue(codes.contains("VOC"))
        assertTrue(codes.contains("RES"))

        disciplines.forEach { d ->
            assertTrue("Discipline ${d.displayName} must have representative fields", d.representativeFields.isNotEmpty())
        }
    }

    @Test
    fun testUniversityAndEngineeringCourses() {
        val courses = GlobalKnowledgeRepository.universityCourses

        assertTrue("Must have verified university courses", courses.isNotEmpty())

        val cs301 = courses.find { it.courseCode == "CS-301" }
        assertNotNull("B.Tech CS-301 must exist", cs301)
        assertEquals("B.Tech Computer Science & Engineering", cs301!!.programme)
        assertEquals(4, cs301.credits)
        assertTrue("Must have semester units", cs301.units.isNotEmpty())
        assertTrue("Must have prerequisites", cs301.prerequisites.isNotEmpty())
        assertTrue("Must have standard textbooks", cs301.textbooks.isNotEmpty())

        // Verify worked derivation in Unit 1
        val unit1 = cs301.units.first()
        assertTrue("Unit must contain derivations", unit1.workedDerivations.isNotEmpty())
        assertTrue("Unit must contain lab experiments", unit1.labActivities.isNotEmpty())

        // Verify Thermodynamics & Quantum Mechanics
        assertNotNull(courses.find { it.courseCode == "ME-401" })
        assertNotNull(courses.find { it.courseCode == "PHY-301" })
    }

    @Test
    fun testCompetitiveExamProfiles() {
        val exams = GlobalKnowledgeRepository.competitiveExams

        assertTrue("Must contain competitive exams", exams.isNotEmpty())

        val upsc = exams.find { it.shortCode == "UPSC CSE" }
        assertNotNull("UPSC Civil Services must exist", upsc)
        assertTrue("Must detail stages (Prelims, Mains, Interview)", upsc!!.stages.size >= 3)
        assertTrue("Must have weightage analysis", upsc.weightageAnalysis.isNotEmpty())
        assertTrue("Must provide cutoff analysis", upsc.previousYearCutoffs.isNotBlank())

        // Verify JEE, NEET, GATE
        assertNotNull(exams.find { it.shortCode == "JEE Advanced" })
        assertNotNull(exams.find { it.shortCode == "NEET UG" })
        assertNotNull(exams.find { it.shortCode == "GATE (CS)" })
    }

    @Test
    fun testPhDResearchWorkspace() {
        val projects = GlobalKnowledgeRepository.sampleResearchProjects

        assertTrue("Must contain doctoral research projects", projects.isNotEmpty())

        val proj = projects.first()
        assertTrue("Research Question must be formulated", proj.researchQuestion.isNotBlank())
        assertTrue("Hypothesis must be explicitly specified", proj.primaryHypothesis.isNotBlank())
        assertTrue("Literature items must have DOI/Ref and citation key", proj.literatureReviewOutlines.all { it.doiOrRef.isNotBlank() && it.citationKey.isNotBlank() })
        assertTrue("Methodology steps must be structured", proj.methodologySteps.isNotEmpty())
        assertTrue("Reproducibility checklist must be defined", proj.reproducibilityChecklist.isNotEmpty())
        assertTrue("Dissertation chapters must be outlined", proj.thesisChapters.size >= 5)
        assertTrue("Open gaps must be identified", proj.openGapsIdentified.isNotEmpty())
    }
}
