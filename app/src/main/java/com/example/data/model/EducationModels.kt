package com.example.data.model

enum class AcademicClass(val displayName: String, val code: String) {
    CLASS_9("Class 9", "9"),
    CLASS_10("Class 10", "10"),
    CLASS_11("Class 11", "11"),
    CLASS_12("Class 12", "12")
}

enum class EducationBoard(val displayName: String, val code: String) {
    CBSE("CBSE (Central Board)", "CBSE"),
    BIHAR_BOARD("Bihar Board (BSEB)", "BSEB"),
    ICSE_ISC("ICSE / ISC", "ICSE"),
    STATE_BOARDS("Other State Boards", "STATE")
}

enum class StudyLanguage(val displayName: String) {
    ENGLISH("English"),
    HINDI("हिंदी (Hindi)")
}

enum class LearningObjective(val displayName: String, val description: String) {
    UNDERSTANDING("Deep Understanding", "Master fundamental concepts from scratch with analogies"),
    REVISION("Rapid Revision", "Recap key formulas, mind maps, and high-yield facts"),
    HOMEWORK("Homework & Practice", "Solve NCERT and exercise problems step-by-step"),
    EXAM_PREP("Board Exam Mastery", "Focus on previous year questions and marking schemes")
}

enum class SubjectCategory(val displayName: String, val iconName: String) {
    MATHEMATICS("Mathematics", "functions"),
    SCIENCE("Science", "science"),
    PHYSICS("Physics", "speed"),
    CHEMISTRY("Chemistry", "biotech"),
    BIOLOGY("Biology", "eco"),
    ENGLISH("English", "menu_book"),
    SOCIAL_SCIENCE("Social Science", "public"),
    HINDI("Hindi", "translate")
}

data class Chapter(
    val id: String,
    val subjectId: String,
    val classCode: String,
    val number: Int,
    val title: String,
    val hindiTitle: String = "",
    val description: String,
    val topicCount: Int,
    val estimatedMinutes: Int,
    val weightageMarks: Int = 8,
    val isDemoChapter: Boolean = false
)

data class Topic(
    val id: String,
    val chapterId: String,
    val title: String,
    val hindiTitle: String = "",
    val summary: String,
    val keyPoints: List<String>
)

data class SmartNote(
    val id: String,
    val chapterId: String,
    val title: String,
    val oneMinuteRevision: String,
    val fiveMinuteRevision: String,
    val onePageSummary: String,
    val conceptualNotes: String,
    val importantDefinitions: List<Pair<String, String>>,
    val commonMistakes: List<String>,
    val memoryAids: List<String>,
    val checklist: List<String>,
    val hindiSummary: String = ""
)

data class FormulaItem(
    val id: String,
    val chapterId: String,
    val subject: SubjectCategory,
    val name: String,
    val expression: String,
    val symbolsMeaning: List<Pair<String, String>>,
    val unitAndDimension: String,
    val conditions: String,
    val derivationSummary: String,
    val workedExample: String,
    val practiceQuestion: String,
    val isBookmarked: Boolean = false,
    val verificationStatus: String = "Verified by ASI Computational Engine",
    val solverType: String? = null // e.g., "QUADRATIC", "OHM", "LENS"
)

enum class QuestionType(val displayName: String) {
    MCQ("Multiple Choice"),
    ASSERTION_REASON("Assertion - Reason"),
    FILL_IN_BLANKS("Fill in Blanks"),
    NUMERICAL("Numerical Problem"),
    SHORT_ANSWER("Short Answer"),
    CASE_BASED("Case-Based / Competency")
}

data class QuestionItem(
    val id: String,
    val chapterId: String,
    val question: String,
    val questionHindi: String = "",
    val type: QuestionType,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val stepByStepSolution: String,
    val conceptExplanation: String,
    val difficulty: String, // "Easy", "Medium", "Difficult", "Tricky"
    val marks: Int,
    val commonMisconceptions: String = "",
    val sourcePageRef: String = ""
)

data class ExamPaper(
    val id: String,
    val title: String,
    val examType: String, // "Half-Yearly Mock", "Final Pre-Board", "Chapter Mastery Test"
    val classCode: String,
    val board: String,
    val durationMinutes: Int,
    val totalMarks: Int,
    val questions: List<QuestionItem>
)

enum class LabSimulationType(val displayName: String, val subject: SubjectCategory, val subtitle: String) {
    PROJECTILE_MOTION("Projectile Mechanics Lab", SubjectCategory.PHYSICS, "Vary angle & velocity to observe parabolic trajectory"),
    RAY_OPTICS("Convex Lens & Ray Optics", SubjectCategory.PHYSICS, "Real vs virtual image formation and magnification"),
    OHMS_LAW("Electric Circuit & Ohm's Law", SubjectCategory.PHYSICS, "Adjust Voltage and Resistance; observe Current & Bulb Glow"),
    BOHR_MODEL("Bohr Atomic Orbitals", SubjectCategory.CHEMISTRY, "Select element; visualize electron distributions in K, L, M shells"),
    QUADRATIC_GRAPH("Quadratic Function Grapher", SubjectCategory.MATHEMATICS, "Explore y = ax² + bx + c, roots, vertex, and discriminant D")
}

data class UserProfile(
    val studentName: String = "Arjun Sharma",
    val selectedClass: AcademicClass = AcademicClass.CLASS_10,
    val selectedBoard: EducationBoard = EducationBoard.CBSE,
    val preferredLanguage: StudyLanguage = StudyLanguage.ENGLISH,
    val currentObjective: LearningObjective = LearningObjective.UNDERSTANDING,
    val studyStreakDays: Int = 14,
    val totalQuestionsAttempted: Int = 186,
    val overallAccuracyPercent: Int = 84,
    val studyHoursTotal: Double = 32.5,
    val selectedTier: EducationTier = EducationTier.SECONDARY,
    val selectedDiscipline: DisciplineGroup = DisciplineGroup.MATHEMATICS,
    val degreeProgramme: String = "Class 10 CBSE",
    val researchSpecialization: String = "Algebraic Geometry & STEM"
)


enum class TutorMode(val title: String, val tagLine: String) {
    EXPLAIN_SIMPLY("Explain Simply", "Clear intuition, everyday analogies, plain language"),
    STEP_BY_STEP("Step-by-Step Solution", "Structured mathematical & logical steps"),
    TEACH_THROUGH_QUESTIONS("Teach Me (Socratic)", "Guides you with hints instead of giving answers away"),
    EXAM_ANSWER_MODE("Exam Answer Mode", "Topper-style point-wise answers to maximize marks"),
    CHALLENGE_ME("Challenge Me", "Olympiad and higher-order thinking problems"),
    REVISE_TOPIC("Revise This Topic", "Rapid bullet-point revision and recall checks")
}

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: TutorMode = TutorMode.EXPLAIN_SIMPLY
)

data class ExtractedChapter(
    val number: Int,
    val title: String,
    val pageRange: String,
    val topics: List<String>,
    val coreConceptSummary: String
)

data class BookStudyPackage(
    val id: String,
    val documentTitle: String,
    val academicClass: AcademicClass,
    val board: EducationBoard,
    val subject: SubjectCategory,
    val language: StudyLanguage,
    val fileSizeFormatted: String,
    val totalPages: Int,
    val chaptersIdentified: List<ExtractedChapter>,
    val extractedFormulas: List<FormulaItem>,
    val generatedQuestions: List<QuestionItem>,
    val shortNotes: String,
    val detailedNotes: String,
    val importantDefinitions: List<Pair<String, String>>,
    val revisionChecklist: List<String>,
    val isVerified: Boolean = true,
    val verificationLabel: String = "ASI Verified Standard",
    val timestamp: Long = System.currentTimeMillis(),
    val chapterTests: List<QuestionItem> = emptyList(),
    val sourceType: String = "Authorized Textbook (PDF)",
    val aiEngineNotice: String = "✦ Synthesized by Gemini 3.5 Flash Cognitive Document Pipeline",
    val verificationNotice: String = "✓ Formulas & Solutions Verified by ASI Pedagogical Engine",
    val estimatedReadTimeMinutes: Int = 25,
    val isAiGenerated: Boolean = true
)

data class DocumentProcessingState(
    val stage: Int = 0,
    val stageDescription: String = "",
    val progressFraction: Float = 0f,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val currentPackage: BookStudyPackage? = null,
    val isWarningLargeFile: Boolean = false
)

// ==========================================
// UNIVERSAL KNOWLEDGE TAXONOMY (SCHOOL TO PHD)
// ==========================================

enum class EducationTier(
    val code: String,
    val displayName: String,
    val subtitle: String,
    val typicalDegree: String
) {
    PRIMARY("PRIMARY", "Primary (1–5)", "Foundational literacy, numeracy, intuitive world exploration", "Primary Certificate"),
    MIDDLE("MIDDLE", "Middle School (6–8)", "General sciences, history, geography, pre-algebra", "Middle School Certificate"),
    SECONDARY("SECONDARY", "Secondary (9–10)", "CBSE/ICSE/State Board syllabus, fundamental STEM & Social Sciences", "Secondary School Certificate (Class 10)"),
    SENIOR_SECONDARY("SENIOR_SEC", "Senior Secondary (11–12)", "PCM, PCB, Commerce, Humanities, Entrance foundations", "Higher Secondary Certificate (Class 12)"),
    VOCATIONAL_DIPLOMA("VOCATIONAL", "Vocational & Polytechnic", "Applied engineering, electronics, trade skills, industrial training", "Polytechnic Diploma"),
    UNDERGRADUATE("UG", "Undergraduate (UG)", "B.Tech, B.E., B.Sc., B.A., B.Com., BCA, BBA, MBBS, LL.B.", "Bachelor's Degree"),
    POSTGRADUATE("PG", "Postgraduate (PG)", "M.Tech, M.Sc., M.A., M.Com., MCA, MBA, M.Phil.", "Master's Degree"),
    DOCTORAL_PHD("PHD", "Doctoral & PhD", "Original academic research, dissertations, peer-reviewed publications", "Doctor of Philosophy (PhD)"),
    COMPETITIVE_EXAMS("COMP_EXAMS", "Competitive Exams", "UPSC, BPSC, NEET, JEE Main/Adv, GATE, CUET, UGC NET, SSC", "National/State Rank Eligibility"),
    LIFELONG_PROFESSIONAL("LIFELONG", "Professional & Lifelong", "Continuing education, industry certifications, executive leadership", "Professional Credential")
}

enum class DisciplineGroup(
    val code: String,
    val displayName: String,
    val description: String,
    val representativeFields: List<String>
) {
    MATHEMATICS(
        "MATH",
        "A. Mathematics",
        "Arithmetic, Number Theory, Algebra, Analysis, Geometry, Topology, Numerical Optimization, & Mathematical Physics",
        listOf("Calculus", "Linear Algebra", "Discrete Math", "Differential Equations", "Topology")
    ),
    NATURAL_SCIENCES(
        "SCI",
        "B. Natural Sciences",
        "Physics, Chemistry, Biology, Astronomy, Earth Sciences, Oceanography & Quantum Mechanics",
        listOf("Classical Mechanics", "Electromagnetism", "Organic Chemistry", "Cell Biology", "Geophysics")
    ),
    HUMANITIES(
        "HUM",
        "C. Humanities & Social Sciences",
        "History, Philosophy, Political Science, Economics, Sociology, Psychology & Cultural Studies",
        listOf("Macroeconomics", "World History", "Political Theory", "Cognitive Psychology", "Sociology")
    ),
    LANGUAGES(
        "LANG",
        "D. Languages & Linguistics",
        "English, Hindi, Sanskrit, Regional Indian Languages, Global Languages, & Computational Linguistics",
        listOf("Grammar", "Phonetics", "Literature", "Semantics", "Machine Translation")
    ),
    COMMERCE_MANAGEMENT(
        "COMM",
        "E. Commerce & Management",
        "Accountancy, Financial Economics, Corporate Strategy, Marketing, Entrepreneurship & Operations",
        listOf("Financial Accounting", "Corporate Finance", "Operations Research", "Strategic Management", "Supply Chain")
    ),
    COMPUTING_TECHNOLOGY(
        "CS",
        "F. Computing & Technology",
        "Computer Science, Algorithms, Operating Systems, Databases, Cybersecurity, AI/ML, Distributed Systems",
        listOf("Data Structures & Algorithms", "OS Architecture", "Computer Networks", "Deep Learning", "Compiler Design")
    ),
    ENGINEERING(
        "ENG",
        "G. Engineering Disciplines",
        "Mechanical, Electrical, Electronics, Civil, Chemical, Aerospace, Materials & Robotics Engineering",
        listOf("Thermodynamics", "VLSI Design", "Structural Analysis", "Fluid Dynamics", "Control Systems")
    ),
    HEALTH_LIFE_SCIENCES(
        "HEALTH",
        "H. Health & Life Sciences",
        "Biotechnology, Molecular Genetics, Anatomy, Physiology, Pharmacology, Immunology & Public Health",
        listOf("Molecular Biology", "Biochemistry", "Pathology", "Clinical Pharmacology", "Epidemiology")
    ),
    LAW_PUBLIC_ADMIN(
        "LAW",
        "I. Law & Public Administration",
        "Constitutional Law, Jurisprudence, International Law, Administrative Governance & Public Policy",
        listOf("Constitutional Law", "Administrative Law", "Criminal Law", "Public Policy Analysis", "Corporate Governance")
    ),
    VOCATIONAL_APPLIED(
        "VOC",
        "J. Vocational & Applied Learning",
        "Applied Instrumentation, Laboratory Techniques, Industrial Design, Manufacturing & Electronics Repair",
        listOf("PCB Fabrication", "CNC Machining", "Renewable Energy Systems", "Industrial Automation", "Lab Diagnostics")
    ),
    RESEARCH_ADVANCED(
        "RES",
        "K. Research & Advanced Study",
        "Epistemology, Quantitative/Qualitative Methodologies, Literature Reviews, Experimental Design, Academic Publishing",
        listOf("Research Methodology", "Statistical Inference", "Literature Synthesis", "Ethics & Reproducibility", "Dissertation Writing")
    )
}

data class CourseUnit(
    val id: String,
    val unitNumber: Int,
    val title: String,
    val topics: List<String>,
    val learningOutcomes: List<String>,
    val workedDerivations: List<String> = emptyList(),
    val labActivities: List<String> = emptyList(),
    val sampleExamQuestions: List<QuestionItem> = emptyList(),
    val pageReferences: String = ""
)

data class UniversityCourse(
    val id: String,
    val courseCode: String,
    val title: String,
    val tier: EducationTier,
    val discipline: DisciplineGroup,
    val programme: String,
    val semester: Int,
    val credits: Int,
    val units: List<CourseUnit>,
    val prerequisites: List<String> = emptyList(),
    val textbooks: List<String> = emptyList(),
    val syllabusOverview: String = "",
    val isVerified: Boolean = true,
    val verificationLabel: String = "ASI Curriculum Verified"
)

data class LiteratureItem(
    val id: String,
    val title: String,
    val authors: String,
    val year: Int,
    val venueOrJournal: String,
    val doiOrRef: String,
    val coreFindings: String,
    val limitationOrGap: String,
    val citationKey: String
)

data class ResearchProject(
    val id: String,
    val title: String,
    val fieldOfStudy: String,
    val researchQuestion: String,
    val primaryHypothesis: String,
    val literatureReviewOutlines: List<LiteratureItem>,
    val methodologySteps: List<String>,
    val reproducibilityChecklist: List<Pair<String, Boolean>>,
    val thesisChapters: List<String>,
    val openGapsIdentified: List<String>,
    val verificationStatus: String = "Hypothesis Formulated (Requires Peer-Review)",
    val timestamp: Long = System.currentTimeMillis()
)

data class CompetitiveExam(
    val id: String,
    val name: String,
    val shortCode: String,
    val category: String,
    val conductingBody: String,
    val eligibility: String,
    val stages: List<String>,
    val syllabusUnits: List<String>,
    val weightageAnalysis: List<Pair<String, Int>>,
    val previousYearCutoffs: String,
    val mockDurationMinutes: Int = 180,
    val totalMarks: Int = 300,
    val practiceQuestions: List<QuestionItem> = emptyList()
)

data class SourceConnector(
    val id: String,
    val name: String,
    val category: String,
    val sourceUrl: String,
    val licenseStatus: String,
    val lastSyncTimestamp: Long = System.currentTimeMillis(),
    val totalItemsIngested: Int = 0,
    val syncStatus: String = "ACTIVE_MONITORING"
)

data class BackgroundJobRecord(
    val id: String,
    val jobName: String,
    val targetDiscipline: String,
    val status: String, // "QUEUED", "RUNNING", "COMPLETED", "RETRYING"
    val progressPercent: Int,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val retryCount: Int = 0,
    val logSummary: String = ""
)


