package com.example.data.repository

import com.example.data.model.*

object CurriculumRepository {

    // --- Chapters Catalogue for Classes 9-12 ---
    val allChapters: List<Chapter> = listOf(
        // Class 10 Mathematics
        Chapter(
            id = "math_10_ch04",
            subjectId = "math_10",
            classCode = "10",
            number = 4,
            title = "Quadratic Equations",
            hindiTitle = "द्विघात समीकरण",
            description = "Master standard form, factorisation, quadratic formula, nature of roots & word problems.",
            topicCount = 5,
            estimatedMinutes = 45,
            weightageMarks = 8,
            isDemoChapter = true
        ),
        Chapter(
            id = "math_10_ch01",
            subjectId = "math_10",
            classCode = "10",
            number = 1,
            title = "Real Numbers",
            hindiTitle = "वास्तविक संख्याएँ",
            description = "Fundamental Theorem of Arithmetic, irrationality proofs, and HCF-LCM algorithms.",
            topicCount = 4,
            estimatedMinutes = 35,
            weightageMarks = 6
        ),
        Chapter(
            id = "math_10_ch02",
            subjectId = "math_10",
            classCode = "10",
            number = 2,
            title = "Polynomials",
            hindiTitle = "बहुपद",
            description = "Zeroes of quadratic and cubic polynomials, relationship with coefficients.",
            topicCount = 3,
            estimatedMinutes = 30,
            weightageMarks = 5
        ),
        Chapter(
            id = "math_10_ch03",
            subjectId = "math_10",
            classCode = "10",
            number = 3,
            title = "Pair of Linear Equations in Two Variables",
            hindiTitle = "दो चरों वाले रैखिक समीकरण युग्म",
            description = "Graphical and algebraic methods: substitution, elimination, consistency checks.",
            topicCount = 5,
            estimatedMinutes = 40,
            weightageMarks = 7
        ),
        Chapter(
            id = "math_10_ch08",
            subjectId = "math_10",
            classCode = "10",
            number = 8,
            title = "Introduction to Trigonometry",
            hindiTitle = "त्रिकोणमिति का परिचय",
            description = "Trigonometric ratios, values of specific angles (0°, 30°, 45°, 60°, 90°) & identities.",
            topicCount = 4,
            estimatedMinutes = 50,
            weightageMarks = 8
        ),
        // Class 10 Science
        Chapter(
            id = "sci_10_ch10",
            subjectId = "sci_10",
            classCode = "10",
            number = 10,
            title = "Light – Reflection and Refraction",
            hindiTitle = "प्रकाश – परावर्तन तथा अपवर्तन",
            description = "Spherical mirrors, mirror formula, Snell's law, refractive index, lens formula & power.",
            topicCount = 6,
            estimatedMinutes = 55,
            weightageMarks = 9
        ),
        Chapter(
            id = "sci_10_ch12",
            subjectId = "sci_10",
            classCode = "10",
            number = 12,
            title = "Electricity",
            hindiTitle = "विद्युत",
            description = "Ohm's law, resistance factors, resistors in series and parallel, Joule's heating effect.",
            topicCount = 5,
            estimatedMinutes = 50,
            weightageMarks = 8
        ),
        Chapter(
            id = "sci_10_ch01",
            subjectId = "sci_10",
            classCode = "10",
            number = 1,
            title = "Chemical Reactions and Equations",
            hindiTitle = "रासायनिक अभिक्रियाएँ एवं समीकरण",
            description = "Balancing equations, types of reactions: combination, decomposition, displacement, redox.",
            topicCount = 4,
            estimatedMinutes = 40,
            weightageMarks = 6
        ),
        // Class 9 Science & Math
        Chapter(
            id = "sci_09_ch08",
            subjectId = "sci_09",
            classCode = "9",
            number = 8,
            title = "Motion",
            hindiTitle = "गति",
            description = "Distance, displacement, velocity, acceleration, and equations of uniformly accelerated motion.",
            topicCount = 4,
            estimatedMinutes = 45,
            weightageMarks = 7
        ),
        Chapter(
            id = "math_09_ch02",
            subjectId = "math_09",
            classCode = "9",
            number = 2,
            title = "Polynomials",
            hindiTitle = "बहुपद",
            description = "Factor theorem, remainder theorem, algebraic identities & expansions.",
            topicCount = 4,
            estimatedMinutes = 40,
            weightageMarks = 7
        ),
        // Class 11 Physics
        Chapter(
            id = "phy_11_ch04",
            subjectId = "phy_11",
            classCode = "11",
            number = 4,
            title = "Motion in a Plane (Vectors & Projectile)",
            hindiTitle = "समतल में गति (प्रक्षेप्य गति)",
            description = "Vector operations, relative velocity, projectile motion trajectory, range and max height.",
            topicCount = 5,
            estimatedMinutes = 60,
            weightageMarks = 8
        ),
        // Class 12 Physics
        Chapter(
            id = "phy_12_ch03",
            subjectId = "phy_12",
            classCode = "12",
            number = 3,
            title = "Current Electricity",
            hindiTitle = "विद्युत धारा",
            description = "Drift velocity, Ohm's law, Kirchhoff's laws, Wheatstone bridge, internal resistance.",
            topicCount = 6,
            estimatedMinutes = 60,
            weightageMarks = 9
        )
    )

    // --- Smart Note 1: Class 10 Quadratic Equations ---
    val quadraticSmartNote = SmartNote(
        id = "note_quad_10",
        chapterId = "math_10_ch04",
        title = "Quadratic Equations — Master Concept Pack",
        oneMinuteRevision = "A quadratic equation in variable x is an equation of the form ax² + bx + c = 0, where a, b, c are real numbers and a ≠ 0. Roots are given by x = (-b ± √(b² - 4ac)) / (2a). Discriminant D = b² - 4ac: D > 0 gives 2 distinct real roots, D = 0 gives 2 equal real roots, D < 0 gives no real roots.",
        fiveMinuteRevision = """
            1. Standard Form: ax² + bx + c = 0 (always write in descending powers of x before solving).
            2. Methods of Solution:
               - Splitting the Middle Term: Find two numbers whose product is a·c and sum is b.
               - Quadratic Formula (Sridharacharya's Rule): x = (-b ± √D) / 2a, where D = b² - 4ac.
            3. Nature of Roots (Discriminant Test):
               - If D > 0: Two distinct real roots. The parabola crosses the x-axis at two points.
               - If D = 0: Two real and equal roots (-b/2a). Parabola touches x-axis at vertex.
               - If D < 0: No real roots (roots are imaginary/complex). Parabola does not touch x-axis.
            4. Vieta's Relations: Sum of roots (α + β) = -b/a, Product of roots (α·β) = c/a.
            5. Constructing equation from roots: x² - (Sum)x + (Product) = 0.
        """.trimIndent(),
        onePageSummary = """
            QUADRATIC EQUATIONS (NCERT CHAPTER 4) - COMPLETE OVERVIEW
            
            • DEFINITION: Degree 2 polynomial equation: ax² + bx + c = 0, a ≠ 0.
            
            • DISCRIMINANT (D): D = b² - 4ac
              - D > 0, perfect square → Rational & unequal roots.
              - D > 0, not perfect square → Irrational & conjugate roots (p ± √q).
              - D = 0 → Rational & equal roots: x = -b / 2a.
              - D < 0 → Complex conjugate roots (no real solution in ℝ).
              
            • COMMON REAL-WORLD APPLICATIONS:
              1. Projectile motion: h(t) = -½gt² + v₀t + h₀
              2. Speed-Time-Distance problems: Speed of stream (u) and boat in still water (v).
                 Upstream speed = (v - u) km/h; Downstream speed = (v + u) km/h.
              3. Consecutive integer / Area geometry problems.
        """.trimIndent(),
        conceptualNotes = """
            Detailed Conceptual Explanation:
            Why must 'a' not equal 0?
            If a = 0, the x² term vanishes, leaving bx + c = 0, which is a linear equation (degree 1) having at most one solution, not two.
            
            The Origin of the Quadratic Formula:
            Starting from ax² + bx + c = 0:
            1. Divide by a: x² + (b/a)x + (c/a) = 0
            2. Complete square: [x + b/(2a)]² - b²/(4a²) + c/a = 0
            3. [x + b/(2a)]² = (b² - 4ac) / (4a²)
            4. Taking square root: x + b/(2a) = ± √(b² - 4ac) / (2a)
            5. Therefore: x = (-b ± √(b² - 4ac)) / (2a).
        """.trimIndent(),
        importantDefinitions = listOf(
            "Quadratic Equation" to "An algebraic equation of second degree in one variable: ax² + bx + c = 0, with a ≠ 0.",
            "Discriminant (D)" to "The algebraic expression b² - 4ac that determines the existence and nature of real roots.",
            "Root / Zero" to "A real number α such that substituting x = α satisfies aα² + bα + c = 0.",
            "Upstream & Downstream" to "Upstream speed = Boat speed - Stream speed; Downstream speed = Boat speed + Stream speed."
        ),
        commonMistakes = listOf(
            "Forgetting that ± comes with the square root when applying the quadratic formula.",
            "Missing the minus sign in -b when b itself is negative (e.g. if b = -6, then -b = +6).",
            "Applying the quadratic formula before rearranging the equation into standard ax² + bx + c = 0 format.",
            "Assuming D < 0 means 'no answer at all' instead of precisely 'no REAL roots'."
        ),
        memoryAids = listOf(
            "D = b² - 4ac: 'Brave 2 boys minus 4 Awesome Champions'",
            "Roots sum: S = -b/a (Sign changes), Product: P = c/a (Sign stays)",
            "Equation formation: x² - Sx + P = 0"
        ),
        checklist = listOf(
            "Write the equation in standard form ax² + bx + c = 0.",
            "Check if 'a' is positive for easier factorisation.",
            "Calculate D = b² - 4ac first to verify if real roots exist.",
            "Double-check roots by substituting them back into the original equation."
        ),
        hindiSummary = "द्विघात समीकरण ax² + bx + c = 0 का मानक रूप है (a ≠ 0)। मूल सूत्र: x = (-b ± √D) / 2a, जहाँ विविक्तकर D = b² - 4ac। यदि D > 0 है तो दो भिन्न वास्तविक मूल, D = 0 पर दो बराबर वास्तविक मूल, और D < 0 पर कोई वास्तविक मूल नहीं होता।"
    )

    // --- Smart Note 2: Class 10 Light (Reflection & Refraction) ---
    val lightSmartNote = SmartNote(
        id = "note_light_10",
        chapterId = "sci_10_ch10",
        title = "Light: Reflection & Refraction — Comprehensive Guide",
        oneMinuteRevision = "Light travels in straight lines. Mirror formula: 1/f = 1/v + 1/u. Lens formula: 1/f = 1/v - 1/u. Snell's Law: sin i / sin r = n₂ / n₁. Optical power P = 1 / f(in meters) in Dioptres (D). Convex lens is converging (+f); Concave lens is diverging (-f).",
        fiveMinuteRevision = """
            1. Laws of Reflection: Angle of incidence = Angle of reflection (∠i = ∠r); Incident ray, reflected ray, and normal lie in the same plane.
            2. Spherical Mirrors: Concave (converging, forms real/virtual images); Convex (diverging, always forms virtual, erect, diminished images).
            3. Refraction: Bending of light when passing between optical media due to speed variation: n = c / v.
            4. Lens Formula & Magnification: 1/f = 1/v - 1/u; m = v / u = h' / h.
            5. Power of Lens: P = 1 / f (in m). Convex lens power is positive; concave lens power is negative.
        """.trimIndent(),
        onePageSummary = """
            LIGHT - REFLECTION & REFRACTION (NCERT CHAPTER 10)
            
            • NEW CARTESIAN SIGN CONVENTION:
              - Optical center / pole is origin (0, 0).
              - Object distance (u) is ALWAYS negative.
              - Real image (v > 0 for lenses, v < 0 for mirrors).
              - Virtual image (v < 0 for lenses, v > 0 for mirrors).
              - Convex lens focal length (f) > 0; Concave lens (f) < 0.
              
            • MAGNIFICATION (m):
              - m < 0: Real and inverted.
              - m > 0: Virtual and erect.
              - |m| > 1: Enlarged; |m| < 1: Diminished; |m| = 1: Same size.
        """.trimIndent(),
        conceptualNotes = """
            Why Does Light Refract?
            Light propagates as an electromagnetic wave with speed c = 3 × 10⁸ m/s in vacuum. In an optically denser medium (like glass or water), light interacts with atoms, slowing down to v = c / n. By Fermat's Principle of Least Time, bending minimizes total travel time between two points across different media.
        """.trimIndent(),
        importantDefinitions = listOf(
            "Refractive Index (n)" to "Ratio of speed of light in vacuum to speed of light in the given medium: n = c / v.",
            "Optical Center" to "Central point of a lens through which light rays pass without deviation.",
            "Dioptre (D)" to "SI unit of power of a lens having focal length of 1 meter."
        ),
        commonMistakes = listOf(
            "Forgetting to convert focal length from cm to meters before calculating power P = 1/f.",
            "Mixing up mirror formula (1/f = 1/v + 1/u) with lens formula (1/f = 1/v - 1/u).",
            "Missing Cartesian negative sign for object distance u."
        ),
        memoryAids = listOf(
            "Mirror has PLUS (1/v + 1/u), Lens has MINUS (1/v - 1/u)",
            "Convex is converging (+), Concave is caves in / diverging (-)"
        ),
        checklist = listOf(
            "Always state Cartesian sign convention clearly in exam answers.",
            "Draw arrows on light rays to avoid mark deductions.",
            "State whether image is real/inverted or virtual/erect."
        ),
        hindiSummary = "प्रकाश परावर्तन एवं अपवर्तन: दर्पण सूत्र 1/f = 1/v + 1/u और लेंस सूत्र 1/f = 1/v - 1/u। लेंस की क्षमता P = 1/f (मीटर में) डायोप्टर (D) में मापी जाती है।"
    )

    // --- Smart Note 3: Class 10 Electricity ---
    val electricitySmartNote = SmartNote(
        id = "note_elec_10",
        chapterId = "sci_10_ch12",
        title = "Electricity & Circuits — Board Revision",
        oneMinuteRevision = "Electric Current I = Q / t (Ampere). Potential Difference V = W / Q (Volt). Ohm's Law: V = I × R at constant temperature. Resistance R = ρ(L / A). Series: Rs = R₁ + R₂ + ...; Parallel: 1/Rp = 1/R₁ + 1/R₂ + ... Joule Heating H = I²Rt. Power P = VI = I²R = V²/R.",
        fiveMinuteRevision = """
            1. Current & Charge: 1 Coulomb = charge of 6.25 × 10¹⁸ electrons. Direction of conventional current is opposite to electron drift.
            2. Ohm's Law: V ∝ I. Constant of proportionality is Resistance (R, measured in Ohms Ω).
            3. Factors affecting Resistance:
               - Length (L): R ∝ L
               - Area of Cross-section (A): R ∝ 1/A
               - Material Nature: Resistivity (ρ in Ω·m)
               - Temperature: Increases with temp for metallic conductors.
            4. Resistor Combinations:
               - Series: Current remains same across all resistors; V = V₁ + V₂. Rs = R₁ + R₂ + R₃.
               - Parallel: Voltage remains same across all branches; I = I₁ + I₂. 1/Rp = 1/R₁ + 1/R₂ + 1/R₃.
            5. Electric Power & Commercial Unit: 1 kWh = 3.6 × 10⁶ Joules = 1 'unit' of electricity.
        """.trimIndent(),
        onePageSummary = """
            ELECTRICITY (NCERT CHAPTER 12) - FORMULA CHEAT SHEET
            
            • OHM'S LAW: V = IR => I = V/R => R = V/I
            • RESISTIVITY: R = ρ (L / A)
            • SERIES EQUIVALENT: R_eq = R₁ + R₂ + ... + Rₙ (Equivalent is larger than largest individual)
            • PARALLEL EQUIVALENT: 1/R_eq = 1/R₁ + 1/R₂ + ... (Equivalent is smaller than smallest individual)
            • JOULE'S LAW OF HEATING: H = I² R t
            • ELECTRICAL POWER: P = V I = I² R = V² / R
        """.trimIndent(),
        conceptualNotes = """
            Microscopic Origin of Resistance:
            When an electric field E is established in a metallic conductor, free conduction electrons accelerate towards the positive terminal. However, they repeatedly collide with vibrating positive metal ions, transferring kinetic energy to the lattice as thermal heat (Joule's heating). This ionic drag constitutes electrical resistance.
        """.trimIndent(),
        importantDefinitions = listOf(
            "Electric Current" to "The rate of flow of electric charges across any cross-section of a conductor: I = Q / t.",
            "1 Volt" to "The potential difference between two points when 1 Joule of work is done in moving 1 Coulomb of positive charge between them.",
            "Resistivity (ρ)" to "The characteristic resistance of a conductor having unit length and unit cross-sectional area."
        ),
        commonMistakes = listOf(
            "Using H = V²t / R instead of H = I²Rt in series circuits where current is constant.",
            "Calculating parallel resistance as Rp = R₁ + R₂ instead of taking reciprocal.",
            "Assuming current decreases as it flows through a circuit (current is conserved)."
        ),
        memoryAids = listOf(
            "VIP: V = I × P ? No, V = I × R; P = V × I",
            "Parallel = Paths: Current divides, Voltage stays constant"
        ),
        checklist = listOf(
            "Check that Ammeter is always in series and Voltmeter in parallel.",
            "Remember that Voltmeter has very high resistance, Ammeter has very low resistance.",
            "Convert time to seconds when calculating energy in Joules."
        ),
        hindiSummary = "विद्युत: ओम का नियम V = I × R। प्रतिरोध R = ρ (L / A)। श्रेणीक्रम में Rs = R₁ + R₂ तथा समानांतर क्रम में 1/Rp = 1/R₁ + 1/R₂। जूल का तापीय नियम H = I²Rt।"
    )

    val allSmartNotes: List<SmartNote> = listOf(
        quadraticSmartNote,
        lightSmartNote,
        electricitySmartNote
    )

    fun getSmartNoteForChapter(chapterId: String): SmartNote {
        return allSmartNotes.find { it.chapterId == chapterId } ?: quadraticSmartNote
    }

    // --- Demonstration Formulas ---
    val allFormulas: List<FormulaItem> = listOf(
        FormulaItem(
            id = "f_quad_formula",
            chapterId = "math_10_ch04",
            subject = SubjectCategory.MATHEMATICS,
            name = "Quadratic Formula (Sridharacharya's Law)",
            expression = "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}",
            symbolsMeaning = listOf(
                "x" to "Roots of the quadratic equation",
                "a" to "Coefficient of x² (a ≠ 0)",
                "b" to "Coefficient of x",
                "c" to "Constant term",
                "D = b² - 4ac" to "Discriminant"
            ),
            unitAndDimension = "Dimensionless (pure real numbers)",
            conditions = "Valid for all quadratic equations ax² + bx + c = 0 where a, b, c ∈ ℝ and a ≠ 0.",
            derivationSummary = "Derived by dividing ax² + bx + c = 0 by 'a' and completing the square for (x + b/2a)².",
            workedExample = "Solve 2x² - 7x + 3 = 0: Here a=2, b=-7, c=3. D = (-7)² - 4(2)(3) = 49 - 24 = 25. x = (7 ± √25) / (2×2) = (7 ± 5)/4. Roots are x = 3 and x = 1/2.",
            practiceQuestion = "Find the roots of 3x² - 5x + 2 = 0 using the quadratic formula.",
            solverType = "QUADRATIC"
        ),
        FormulaItem(
            id = "f_discriminant",
            chapterId = "math_10_ch04",
            subject = SubjectCategory.MATHEMATICS,
            name = "Nature of Roots & Discriminant",
            expression = "D = b^2 - 4ac",
            symbolsMeaning = listOf(
                "D" to "Discriminant value",
                "D > 0" to "Two distinct real roots",
                "D = 0" to "Two equal real roots: x = -b/(2a)",
                "D < 0" to "No real roots"
            ),
            unitAndDimension = "Dimensionless",
            conditions = "Real coefficients a, b, c.",
            derivationSummary = "Term under the square root in the quadratic formula determines the reality of roots.",
            workedExample = "Find k if 2x² + kx + 3 = 0 has two equal roots. For equal roots D = 0 => k² - 4(2)(3) = 0 => k² = 24 => k = ±2√6.",
            practiceQuestion = "Find values of k for which x² - 4kx + k = 0 has equal real roots.",
            solverType = "QUADRATIC"
        ),
        FormulaItem(
            id = "f_ohms_law",
            chapterId = "sci_10_ch12",
            subject = SubjectCategory.PHYSICS,
            name = "Ohm's Law & Power",
            expression = "V = I \\times R",
            symbolsMeaning = listOf(
                "V" to "Electric potential difference across conductor (Volts, V)",
                "I" to "Electric current flowing through conductor (Amperes, A)",
                "R" to "Electrical resistance of conductor (Ohms, Ω)"
            ),
            unitAndDimension = "V: [M L² T⁻³ I⁻¹], R: [M L² T⁻³ I⁻²]",
            conditions = "Valid for metallic conductors maintained at constant temperature.",
            derivationSummary = "Microscopic relation J = σE leads to macroscopic V = IR across conductor of length L and area A.",
            workedExample = "A toaster connected to a 220 V line draws a current of 5 A. What is its resistance? R = V / I = 220 / 5 = 44 Ω.",
            practiceQuestion = "An electric iron draws 4 A when connected to 230 V. Calculate its internal resistance and power.",
            solverType = "OHM"
        ),
        FormulaItem(
            id = "f_lens_formula",
            chapterId = "sci_10_ch10",
            subject = SubjectCategory.PHYSICS,
            name = "Lens Formula & Magnification",
            expression = "\\frac{1}{f} = \\frac{1}{v} - \\frac{1}{u}",
            symbolsMeaning = listOf(
                "f" to "Focal length of lens (meters or cm)",
                "v" to "Image distance from optical center",
                "u" to "Object distance (always negative by Cartesian sign convention)",
                "m = v/u" to "Linear magnification"
            ),
            unitAndDimension = "Distance: [L] in meters (m)",
            conditions = "Thin spherical lenses with small aperture.",
            derivationSummary = "Derived from geometry of similar triangles formed by principal axis rays through optical center and focus.",
            workedExample = "A convex lens has focal length f = +15 cm. An object is placed at u = -30 cm. 1/v = 1/f + 1/u = 1/15 - 1/30 = 1/30 => v = +30 cm (Real, inverted image).",
            practiceQuestion = "Find the image position and nature when an object is placed 10 cm in front of a concave lens of focal length 15 cm.",
            solverType = "LENS"
        ),
        FormulaItem(
            id = "f_projectile_range",
            chapterId = "phy_11_ch04",
            subject = SubjectCategory.PHYSICS,
            name = "Horizontal Range of a Projectile",
            expression = "R = \\frac{u^2 \\sin(2\\theta)}{g}",
            symbolsMeaning = listOf(
                "R" to "Horizontal range (m)",
                "u" to "Initial projection velocity (m/s)",
                "θ" to "Angle of projection with horizontal",
                "g" to "Acceleration due to gravity (~9.8 m/s²)"
            ),
            unitAndDimension = "Length: [L] in meters",
            conditions = "Neglecting air resistance, flat horizontal ground.",
            derivationSummary = "Range = Horizontal velocity × Time of flight = (u cosθ) × (2u sinθ / g) = u² sin(2θ)/g.",
            workedExample = "A ball thrown at u = 20 m/s at 45°: R = (20² × sin 90°) / 9.8 = 400 / 9.8 ≈ 40.82 m.",
            practiceQuestion = "At what two complementary angles does a projectile have the same horizontal range?"
        )
    )

    // --- Practice Questions & Tricky Bank ---
    val allQuestions: List<QuestionItem> = listOf(
        // Quadratic Equations
        QuestionItem(
            id = "q_quad_01",
            chapterId = "math_10_ch04",
            question = "Which of the following equations is a quadratic equation?",
            questionHindi = "निम्नलिखित में से कौन सा एक द्विघात समीकरण है?",
            type = QuestionType.MCQ,
            options = listOf(
                "x² + 2x + 1 = (4 - x)² + 3",
                "-2x² = (5 - x)(2x - 2/5)",
                "(k + 1)x² + 3/2 x = 7 (where k = -1)",
                "x³ - x² = (x - 1)³"
            ),
            correctAnswer = "x³ - x² = (x - 1)³",
            stepByStepSolution = """
                Step 1: Expand (x - 1)³ = x³ - 3x² + 3x - 1.
                Step 2: Substitute into equation: x³ - x² = x³ - 3x² + 3x - 1.
                Step 3: Cancel x³ on both sides: -x² = -3x² + 3x - 1.
                Step 4: Bring all terms to one side: 2x² - 3x + 1 = 0.
                Since the highest power of x is 2 and coefficient of x² (2) ≠ 0, this is a quadratic equation!
            """.trimIndent(),
            conceptExplanation = "A quadratic equation must reduce to ax² + bx + c = 0 with a ≠ 0. Always simplify both sides before deciding degree.",
            difficulty = "Intermediate",
            marks = 1,
            commonMisconceptions = "Students mistakenly see x³ and immediately assume it's cubic without realizing x³ cancels out."
        ),
        QuestionItem(
            id = "q_quad_02",
            chapterId = "math_10_ch04",
            question = "If one root of the quadratic equation 2x² + kx - 6 = 0 is 2, find the value of k.",
            questionHindi = "यदि द्विघात समीकरण 2x² + kx - 6 = 0 का एक मूल 2 है, तो k का मान ज्ञात कीजिए।",
            type = QuestionType.NUMERICAL,
            options = listOf("k = -1", "k = 1", "k = 2", "k = -2"),
            correctAnswer = "k = -1",
            stepByStepSolution = """
                Step 1: Since x = 2 is a root, it must satisfy the equation: 2(2)² + k(2) - 6 = 0.
                Step 2: Calculate: 2(4) + 2k - 6 = 0.
                Step 3: 8 + 2k - 6 = 0 => 2k + 2 = 0.
                Step 4: 2k = -2 => k = -1.
            """.trimIndent(),
            conceptExplanation = "By definition, a root makes the equation evaluate to zero when substituted for the variable.",
            difficulty = "Basic",
            marks = 1,
            commonMisconceptions = "Students sometimes try using the discriminant instead of direct root substitution."
        ),
        QuestionItem(
            id = "q_quad_03",
            chapterId = "math_10_ch04",
            question = "Assertion (A): The equation 4x² - 12x + 9 = 0 has repeated real roots.\nReason (R): The discriminant of a quadratic equation ax² + bx + c = 0 is zero when roots are equal.",
            questionHindi = "अभिकथन (A): समीकरण 4x² - 12x + 9 = 0 के बराबर वास्तविक मूल हैं।\nकारण (R): जब मूल बराबर होते हैं तो विविक्तकर D = 0 होता है।",
            type = QuestionType.ASSERTION_REASON,
            options = listOf(
                "Both (A) and (R) are true, and (R) is the correct explanation of (A).",
                "Both (A) and (R) are true, but (R) is NOT the correct explanation of (A).",
                "(A) is true, but (R) is false.",
                "(A) is false, but (R) is true."
            ),
            correctAnswer = "Both (A) and (R) are true, and (R) is the correct explanation of (A).",
            stepByStepSolution = """
                1. Calculate D for 4x² - 12x + 9 = 0: a = 4, b = -12, c = 9.
                2. D = b² - 4ac = (-12)² - 4(4)(9) = 144 - 144 = 0.
                3. Since D = 0, the equation has two equal real roots. Thus Assertion (A) is true.
                4. Reason (R) states the general mathematical theorem for equal roots (D = 0), which explains (A) accurately.
            """.trimIndent(),
            conceptExplanation = "Equal roots occur precisely when the discriminant D vanishes.",
            difficulty = "Intermediate",
            marks = 1,
            commonMisconceptions = "Forgetting (-12)² is +144, not -144."
        ),
        QuestionItem(
            id = "q_quad_04",
            chapterId = "math_10_ch04",
            question = "Fill in the blank: If the discriminant D = b² - 4ac < 0, the quadratic equation has _____ real roots.",
            questionHindi = "रिक्त स्थान भरें: यदि विविक्तकर D < 0 है, तो द्विघात समीकरण के _____ वास्तविक मूल होते हैं।",
            type = QuestionType.FILL_IN_BLANKS,
            options = listOf("no", "two equal", "two distinct", "infinite"),
            correctAnswer = "no",
            stepByStepSolution = "When D < 0, √D is the square root of a negative number, which is not a real number. Therefore, there are no real roots in the set of real numbers ℝ.",
            conceptExplanation = "In the real number system, square roots of negative numbers do not exist.",
            difficulty = "Basic",
            marks = 1,
            commonMisconceptions = "Saying 'zero roots' meaning root value is 0 instead of 'no real roots exist'."
        ),
        QuestionItem(
            id = "q_quad_05",
            chapterId = "math_10_ch04",
            question = "A motor boat whose speed is 18 km/h in still water takes 1 hour more to go 24 km upstream than to return downstream to the same spot. Find the speed of the stream.",
            questionHindi = "एक मोटर बोट जिसकी स्थिर जल में चाल 18 km/h है, 24 km धारा के प्रतिकूल जाने में वही दूरी धारा के अनुकूल जाने की अपेक्षा 1 घंटा अधिक लेती है। धारा की चाल ज्ञात कीजिए।",
            type = QuestionType.CASE_BASED,
            options = listOf("6 km/h", "5 km/h", "4 km/h", "8 km/h"),
            correctAnswer = "6 km/h",
            stepByStepSolution = """
                Step 1: Let the speed of the stream be x km/h (where x < 18).
                Step 2: Speed upstream = (18 - x) km/h; Speed downstream = (18 + x) km/h.
                Step 3: Distance = 24 km.
                Time upstream = 24 / (18 - x); Time downstream = 24 / (18 + x).
                Step 4: Given: [24 / (18 - x)] - [24 / (18 + x)] = 1.
                Step 5: 24 [ (18 + x) - (18 - x) ] / [ (18 - x)(18 + x) ] = 1.
                Step 6: 24 [ 2x ] / (324 - x²) = 1 => 48x = 324 - x².
                Step 7: x² + 48x - 324 = 0.
                Step 8: Factorise: (x + 54)(x - 6) = 0.
                Step 9: Since speed cannot be negative, x = 6 km/h.
            """.trimIndent(),
            conceptExplanation = "Speed of stream reduces net upstream velocity and increases net downstream velocity.",
            difficulty = "Challenging",
            marks = 4,
            commonMisconceptions = "Writing upstream speed as (x - 18) which would make speed negative since stream is slower than boat."
        ),
        // Science: Light
        QuestionItem(
            id = "q_light_01",
            chapterId = "sci_10_ch10",
            question = "A convex lens forms a real and inverted image of a needle at a distance of 50 cm from it. Where is the needle placed in front of the convex lens if the image is equal to the size of the object? Also find power.",
            questionHindi = "कोई उत्तल लेंस किसी सुई का वास्तविक तथा उल्टा प्रतिबिम्ब उससे 50 cm दूर बनाता है। यह सुई उत्तल लेंस के सामने कहाँ रखी है यदि इसका प्रतिबिम्ब उसी आकार का बन रहा है?",
            type = QuestionType.NUMERICAL,
            options = listOf("-50 cm, P = +4 D", "-25 cm, P = +2 D", "-50 cm, P = -4 D", "-100 cm, P = +1 D"),
            correctAnswer = "-50 cm, P = +4 D",
            stepByStepSolution = """
                Step 1: For a real and inverted image of same size as object, m = -1.
                Step 2: m = v / u => -1 = 50 / u => u = -50 cm.
                Step 3: Object is placed at 2F₁ (u = -50 cm), so 2f = 50 cm => f = +25 cm = +0.25 m.
                Step 4: Power P = 1 / f(in m) = 1 / (+0.25) = +4 D.
            """.trimIndent(),
            conceptExplanation = "When object is at 2F of a convex lens, image forms at 2F on the other side with same size.",
            difficulty = "Intermediate",
            marks = 3,
            commonMisconceptions = "Forgetting to convert focal length from cm to meters before calculating power."
        ),
        // Science: Electricity
        QuestionItem(
            id = "q_elec_01",
            chapterId = "sci_10_ch12",
            question = "A piece of wire of resistance R is cut into five equal parts. These parts are then connected in parallel. If the equivalent resistance of this combination is R', then the ratio R/R' is:",
            questionHindi = "R प्रतिरोध के किसी तार के टुकड़े को पाँच बराबर भागों में काटा जाता है। इन टुकड़ों को फिर पार्श्वक्रम (समांतर क्रम) में संयोजित कर देते हैं। संयोजन का तुल्य प्रतिरोध R' है, तो R/R' अनुपात क्या है?",
            type = QuestionType.MCQ,
            options = listOf("1/25", "1/5", "5", "25"),
            correctAnswer = "25",
            stepByStepSolution = """
                Step 1: When wire is cut into 5 equal parts, resistance of each piece = R / 5 (since R ∝ L).
                Step 2: Connect five resistors of resistance (R/5) in parallel:
                1 / R' = 1/(R/5) + 1/(R/5) + 1/(R/5) + 1/(R/5) + 1/(R/5) = 5/(R/5) = 25 / R.
                Step 3: Therefore R' = R / 25 => R / R' = 25.
            """.trimIndent(),
            conceptExplanation = "Cutting length divides individual resistance; connecting in parallel further divides equivalent resistance.",
            difficulty = "Challenging",
            marks = 2,
            commonMisconceptions = "Confusing R/R' with R'/R and selecting 1/25 instead of 25."
        )
    )

    val quadraticQuestions = allQuestions.filter { it.chapterId == "math_10_ch04" }

    fun getQuestionsForChapter(chapterId: String): List<QuestionItem> {
        val filtered = allQuestions.filter { it.chapterId == chapterId }
        return if (filtered.isNotEmpty()) filtered else quadraticQuestions
    }

    // --- Mock Exam Paper ---
    val demoExamPaper = ExamPaper(
        id = "mock_quad_10",
        title = "CBSE Class 10 Board Simulation: Quadratic Equations",
        examType = "Chapter Mastery & Board Mock",
        classCode = "10",
        board = "CBSE",
        durationMinutes = 20,
        totalMarks = 8,
        questions = quadraticQuestions
    )

    fun getExamForType(examType: String, chapterId: String = "math_10_ch04"): ExamPaper {
        val qList = if (examType.contains("Board") || examType.contains("Final")) {
            allQuestions
        } else {
            getQuestionsForChapter(chapterId)
        }
        val duration = when {
            examType.contains("Board") -> 30
            examType.contains("Final") -> 25
            else -> 20
        }
        val totalMarks = qList.sumOf { it.marks }
        return ExamPaper(
            id = "exam_${examType.replace(" ", "_")}",
            title = "$examType — CBSE Class 10 Intelligence Test",
            examType = examType,
            classCode = "10",
            board = "CBSE",
            durationMinutes = duration,
            totalMarks = totalMarks,
            questions = qList
        )
    }
}
