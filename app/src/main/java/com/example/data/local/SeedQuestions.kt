package com.example.data.local

import com.example.data.local.entity.QuestionEntity

object SeedQuestions {
    fun getQuestions(): List<QuestionEntity> {
        val list = mutableListOf<QuestionEntity>()

        // 1. Trigonometry II (Std XI) - Q1
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Trigonometry II",
                topic = "Multiple Angles",
                subtopic = "Double Angle",
                difficulty = "LEVEL 2 — Concept",
                question = "If tan θ = 3/4 and θ lies in the first quadrant, what is the value of sin 2θ?",
                optionA = "24/25",
                optionB = "12/25",
                optionC = "7/25",
                optionD = "18/25",
                correctAnswer = "A",
                explanation = "sin 2θ = (2 tan θ) / (1 + tan² θ). Substituting tan θ = 3/4 gives: sin 2θ = 2(3/4) / (1 + 9/16) = (3/2) / (25/16) = (3/2) * (16/25) = 24/25.",
                formula = "sin 2θ = (2 tan θ) / (1 + tan² θ)",
                shortcut = "In a 3-4-5 right triangle, sin θ = 3/5 and cos θ = 4/5. Then sin 2θ = 2 sin θ cos θ = 2 * (3/5) * (4/5) = 24/25.",
                hint1 = "Recall the double angle formula for sine in terms of tangent.",
                hint2 = "The formula is sin 2θ = 2 tan θ / (1 + tan² θ).",
                hint3 = "Substitute tan θ = 3/4 and simplify the fraction.",
                estimatedTime = 30,
                xp = 15,
                tags = "Trigonometry, Double Angle, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "tan θ = 3/4, θ in 1st quadrant",
                teachMeWeNeed = "sin 2θ",
                teachMeFormula = "sin 2θ = 2 sin θ cos θ or (2 tan θ)/(1 + tan² θ)",
                teachMeSubstitution = "sin 2θ = 2(3/4) / [1 + (3/4)²]",
                teachMeCalculation = "= (3/2) / (1 + 9/16) = (3/2) / (25/16) = (3/2) * (16/25) = 24/25",
                teachMeFinalAnswer = "24/25 (Option A)",
                teachMeShortcut = "Use 3-4-5 right triangle: sin θ = 3/5, cos θ = 4/5 ⇒ 2(3/5)(4/5) = 24/25 in 5 seconds!"
            )
        )

        // 2. Trigonometry II (Std XI) - Q2
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Trigonometry II",
                topic = "Transformation Formulae",
                subtopic = "Factorization",
                difficulty = "LEVEL 3 — CET Easy",
                question = "Find the exact value of (sin 70° + cos 40°) / (cos 70° + sin 40°).",
                optionA = "1",
                optionB = "√3",
                optionC = "1/√3",
                optionD = "1/√2",
                correctAnswer = "B",
                explanation = "Express cos as sin: cos 40° = sin 50° and cos 70° = sin 20°.\nNumerator = sin 70° + sin 50° = 2 sin 60° cos 10°.\nDenominator = sin 20° + sin 40° = 2 sin 30° cos 10°.\nRatio = (2 sin 60° cos 10°) / (2 sin 30° cos 10°) = sin 60° / sin 30° = (√3/2) / (1/2) = √3.",
                formula = "sin C + sin D = 2 sin((C+D)/2) cos((C-D)/2)",
                shortcut = "Convert everything to sines first, then apply C-D formula to cancel common cos 10° terms.",
                hint1 = "Convert cosine terms into sine using co-function identities cos x = sin(90° - x).",
                hint2 = "Numerator becomes sin 70° + sin 50°, denominator becomes sin 20° + sin 40°.",
                hint3 = "Apply sin C + sin D to both numerator and denominator.",
                estimatedTime = 45,
                xp = 20,
                tags = "Trigonometry, Factorization, XI",
                isPyq = false,
                teachMeGiven = "Fraction = (sin 70° + cos 40°) / (cos 70° + sin 40°)",
                teachMeWeNeed = "Exact simplified value",
                teachMeFormula = "cos A = sin(90° - A) and sin C + sin D = 2 sin((C+D)/2) cos((C-D)/2)",
                teachMeSubstitution = "Num: sin 70° + sin 50° = 2 sin 60° cos 10°; Denom: sin 20° + sin 40° = 2 sin 30° cos 10°",
                teachMeCalculation = "Cancel 2 cos 10° from both: sin 60° / sin 30° = (√3/2)/(1/2) = √3",
                teachMeFinalAnswer = "√3 (Option B)",
                teachMeShortcut = "Convert to sine: sin 60°/sin 30° = √3 directly."
            )
        )

        // 3. Straight Line (Std XI) - Q3
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Straight Line",
                topic = "Distance between parallel lines",
                subtopic = "Parallel Lines",
                difficulty = "LEVEL 2 — Concept",
                question = "The distance between parallel lines 3x + 4y - 9 = 0 and 6x + 8y + 15 = 0 is:",
                optionA = "3.3 units",
                optionB = "2.4 units",
                optionC = "3.0 units",
                optionD = "1.8 units",
                correctAnswer = "A",
                explanation = "Make coefficients of x and y equal: Divide 6x + 8y + 15 = 0 by 2 to get 3x + 4y + 7.5 = 0.\nNow distance d = |c₁ - c₂| / √(a² + b²) = |-9 - 7.5| / √(3² + 4²) = |-16.5| / 5 = 3.3 units.",
                formula = "d = |c₁ - c₂| / √(a² + b²) after matching coefficients a and b",
                shortcut = "Multiply first line by 2: 6x + 8y - 18 = 0. Distance = |-18 - 15| / √(36 + 64) = 33 / 10 = 3.3.",
                hint1 = "First make the coefficients of x and y identical in both line equations.",
                hint2 = "Multiply the first equation by 2: 6x + 8y - 18 = 0.",
                hint3 = "Then apply distance formula d = |c₁ - c₂| / √(a² + b²).",
                estimatedTime = 30,
                xp = 15,
                tags = "Straight Line, Distance, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2020",
                teachMeGiven = "Line 1: 3x + 4y - 9 = 0; Line 2: 6x + 8y + 15 = 0",
                teachMeWeNeed = "Perpendicular distance between parallel lines",
                teachMeFormula = "d = |c₁ - c₂| / √(a² + b²)",
                teachMeSubstitution = "Multiply L1 by 2: 6x + 8y - 18 = 0. Here a=6, b=8, c₁=-18, c₂=15",
                teachMeCalculation = "d = |-18 - 15| / √(6² + 8²) = |-33| / 10 = 3.3",
                teachMeFinalAnswer = "3.3 units (Option A)",
                teachMeShortcut = "Scale to common coefficients: |-18 - 15|/10 = 33/10 = 3.3."
            )
        )

        // 4. Circle (Std XI) - Q4
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Circle",
                topic = "General Equation",
                subtopic = "Radius and Intercepts",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The length of the intercept made by the circle x² + y² - 4x - 6y - 12 = 0 on the x-axis is:",
                optionA = "6",
                optionB = "8",
                optionC = "10",
                optionD = "4",
                correctAnswer = "B",
                explanation = "Length of intercept made on x-axis is 2√(g² - c). For x² + y² + 2gx + 2fy + c = 0, 2g = -4 ⇒ g = -2 and c = -12. Hence, 2√(g² - c) = 2√((-2)² - (-12)) = 2√(4 + 12) = 2√16 = 2 * 4 = 8.",
                formula = "x-intercept = 2√(g² - c); y-intercept = 2√(f² - c)",
                shortcut = "Put y = 0: x² - 4x - 12 = 0 ⇒ (x - 6)(x + 2) = 0. Roots are 6 and -2. Distance = |6 - (-2)| = 8.",
                hint1 = "You can either use the standard formula 2√(g² - c) or substitute y = 0.",
                hint2 = "Substituting y = 0 gives the quadratic x² - 4x - 12 = 0.",
                hint3 = "Find the roots of x² - 4x - 12 = 0 and subtract them.",
                estimatedTime = 30,
                xp = 20,
                tags = "Circle, Intercept, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "Circle: x² + y² - 4x - 6y - 12 = 0",
                teachMeWeNeed = "Length of x-intercept",
                teachMeFormula = "2√(g² - c) or solve quadratic at y = 0",
                teachMeSubstitution = "g = -2, c = -12. 2√((-2)² - (-12))",
                teachMeCalculation = "= 2√(4 + 12) = 2√16 = 2 * 4 = 8",
                teachMeFinalAnswer = "8 units (Option B)",
                teachMeShortcut = "Put y = 0 ⇒ (x - 6)(x + 2) = 0 ⇒ x = 6, -2. Intercept = 6 - (-2) = 8."
            )
        )

        // 5. Probability (Std XI) - Q5
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Probability",
                topic = "Conditional Probability",
                subtopic = "Bayes Rule",
                difficulty = "LEVEL 4 — CET Medium",
                question = "If P(A) = 0.4, P(B) = 0.8, and P(B|A) = 0.6, then P(A ∪ B) is equal to:",
                optionA = "0.96",
                optionB = "0.92",
                optionC = "0.88",
                optionD = "0.84",
                correctAnswer = "A",
                explanation = "From conditional probability: P(B|A) = P(A ∩ B) / P(A) ⇒ P(A ∩ B) = P(A) * P(B|A) = 0.4 * 0.6 = 0.24.\nNow, P(A ∪ B) = P(A) + P(B) - P(A ∩ B) = 0.4 + 0.8 - 0.24 = 1.20 - 0.24 = 0.96.",
                formula = "P(A ∪ B) = P(A) + P(B) - P(A ∩ B) where P(A ∩ B) = P(A) · P(B|A)",
                shortcut = "P(A ∪ B) = 0.4 + 0.8 - (0.4 * 0.6) = 1.2 - 0.24 = 0.96 in one line.",
                hint1 = "First find P(A ∩ B) using the definition of conditional probability P(B|A).",
                hint2 = "P(A ∩ B) = P(B|A) * P(A) = 0.6 * 0.4 = 0.24.",
                hint3 = "Use addition theorem P(A ∪ B) = P(A) + P(B) - P(A ∩ B).",
                estimatedTime = 40,
                xp = 25,
                tags = "Probability, Conditional, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2023",
                teachMeGiven = "P(A) = 0.4, P(B) = 0.8, P(B|A) = 0.6",
                teachMeWeNeed = "P(A ∪ B)",
                teachMeFormula = "P(A ∩ B) = P(A) P(B|A) and P(A ∪ B) = P(A) + P(B) - P(A ∩ B)",
                teachMeSubstitution = "P(A ∩ B) = 0.4 * 0.6 = 0.24. P(A ∪ B) = 0.4 + 0.8 - 0.24",
                teachMeCalculation = "= 1.20 - 0.24 = 0.96",
                teachMeFinalAnswer = "0.96 (Option A)",
                teachMeShortcut = "1.2 - 0.24 = 0.96."
            )
        )

        // 6. Limits (Std XI) - Q6
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Limits",
                topic = "Trigonometric Limits",
                subtopic = "L'Hôpital / Expansions",
                difficulty = "LEVEL 4 — CET Medium",
                question = "Evaluate: lim (x → 0) [1 - cos(4x)] / [x · sin(3x)].",
                optionA = "4/3",
                optionB = "8/3",
                optionC = "2/3",
                optionD = "16/3",
                correctAnswer = "B",
                explanation = "1 - cos(4x) = 2 sin²(2x).\nNumerator = 2 * [sin(2x) / (2x)]² * 4x² = 8x².\nDenominator = x * [sin(3x) / (3x)] * 3x = 3x².\nLimit = 8x² / 3x² = 8/3.",
                formula = "lim(x→0) (1 - cos kx)/x² = k²/2, and lim(x→0) (sin mx)/x = m",
                shortcut = "(1 - cos 4x) ≈ (4x)²/2 = 8x². Denominator ≈ x * (3x) = 3x². Ratio = 8/3!",
                hint1 = "Recall the standard approximation 1 - cos(kx) ≈ (k²x²)/2 near x = 0.",
                hint2 = "Replace 1 - cos(4x) with 16x²/2 = 8x².",
                hint3 = "Replace sin(3x) with 3x, yielding 8x² / 3x².",
                estimatedTime = 30,
                xp = 25,
                tags = "Limits, Trigonometry, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "lim(x→0) [1 - cos(4x)] / [x sin(3x)]",
                teachMeWeNeed = "Limit value",
                teachMeFormula = "1 - cos θ = 2 sin²(θ/2) and lim(θ→0) (sin θ)/θ = 1",
                teachMeSubstitution = "Numerator: 2 sin²(2x) = 2 * (2x)² = 8x². Denominator: x * 3x = 3x²",
                teachMeCalculation = "lim = 8x² / 3x² = 8/3",
                teachMeFinalAnswer = "8/3 (Option B)",
                teachMeShortcut = "Use series shortcut: (4² / 2) / (1 * 3) = 8/3 in 3 seconds!"
            )
        )

        // 7. Mathematical Logic (Std XII) - Q7
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Mathematical Logic",
                topic = "Duals and Negations",
                subtopic = "Negation of Implication",
                difficulty = "LEVEL 2 — Concept",
                question = "The negation of the statement 'If it rains, then the match will be cancelled' is:",
                optionA = "It rains and the match will not be cancelled.",
                optionB = "It does not rain and the match will be cancelled.",
                optionC = "If it does not rain, then the match will not be cancelled.",
                optionD = "It does not rain or the match will not be cancelled.",
                correctAnswer = "A",
                explanation = "Let p = 'It rains' and q = 'The match will be cancelled'. The conditional is p → q. The negation is ~(p → q) ≡ p ∧ ~q. Therefore, 'It rains and the match will not be cancelled'.",
                formula = "~(p → q) ≡ p ∧ ~q",
                shortcut = "Negation of 'If p then q' is always 'p AND NOT q'.",
                hint1 = "Represent the statement symbolically as p → q.",
                hint2 = "Recall the equivalence of conditional negation: ~(p → q) ≡ p ∧ ~q.",
                hint3 = "Express p ∧ ~q in words.",
                estimatedTime = 25,
                xp = 15,
                tags = "Logic, Negation, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "p: It rains, q: Match cancelled. Given statement is p → q",
                teachMeWeNeed = "Negation of p → q",
                teachMeFormula = "~(p → q) ≡ p ∧ ~q",
                teachMeSubstitution = "p: It rains. ~q: Match will not be cancelled. Connect with 'and'",
                teachMeCalculation = "Result = 'It rains and the match will not be cancelled'",
                teachMeFinalAnswer = "Option A",
                teachMeShortcut = "Negation of implication never contains 'If...then'! Pick the one with 'and not'."
            )
        )

        // 8. Matrices (Std XII) - Q8
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Matrices",
                topic = "Inverse Matrix",
                subtopic = "Adjoint Method",
                difficulty = "LEVEL 3 — CET Easy",
                question = "If A = [[1, 2], [3, 4]], then A⁻¹ is:",
                optionA = "[[-2, 1], [3/2, -1/2]]",
                optionB = "[[2, -1], [-3/2, 1/2]]",
                optionC = "[[-2, -1], [3/2, 1/2]]",
                optionD = "[[4, -2], [-3, 1]]",
                correctAnswer = "A",
                explanation = "det(A) = (1)(4) - (2)(3) = 4 - 6 = -2.\nadj(A) = [[4, -2], [-3, 1]].\nA⁻¹ = (1/det(A)) * adj(A) = (-1/2) * [[4, -2], [-3, 1]] = [[-2, 1], [3/2, -1/2]].",
                formula = "For 2x2 matrix [[a,b],[c,d]], A⁻¹ = (1/(ad-bc)) * [[d, -b], [-c, a]]",
                shortcut = "Determinant = -2. Top-left entry must be 4/(-2) = -2. Only Option A has top-left = -2!",
                hint1 = "Calculate det(A) = ad - bc.",
                hint2 = "det(A) = 4 - 6 = -2.",
                hint3 = "Swap diagonal elements (1 and 4) and multiply off-diagonals by -1, then divide by -2.",
                estimatedTime = 35,
                xp = 20,
                tags = "Matrices, Inverse, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "Matrix A = [[1, 2], [3, 4]]",
                teachMeWeNeed = "A⁻¹",
                teachMeFormula = "A⁻¹ = (1 / det A) * adj(A)",
                teachMeSubstitution = "det A = 1*4 - 2*3 = -2. adj(A) = [[4, -2], [-3, 1]]",
                teachMeCalculation = "A⁻¹ = (-1/2) * [[4, -2], [-3, 1]] = [[-2, 1], [3/2, -1/2]]",
                teachMeFinalAnswer = "[[-2, 1], [3/2, -1/2]] (Option A)",
                teachMeShortcut = "Divide adj(A) top-left element 4 by -2 = -2. Spot Option A instantly!"
            )
        )

        // 9. Trigonometric Functions (Std XII) - Q9
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Trigonometric Functions",
                topic = "Inverse Trig Functions",
                subtopic = "Principal Values",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The principal value of cos⁻¹(cos(7π/6)) is:",
                optionA = "7π/6",
                optionB = "5π/6",
                optionC = "π/6",
                optionD = "-π/6",
                correctAnswer = "B",
                explanation = "The range of principal value branch of cos⁻¹(x) is [0, π]. Since 7π/6 > π, it does not lie in [0, π].\nWe write cos(7π/6) = cos(2π - 5π/6) = cos(5π/6).\nSince 5π/6 ∈ [0, π], cos⁻¹(cos(5π/6)) = 5π/6.",
                formula = "Range of cos⁻¹ x is [0, π]. Use cos(2π - θ) = cos θ.",
                shortcut = "2π - 7π/6 = 5π/6, which is in [0, π].",
                hint1 = "Check whether 7π/6 lies within the principal branch [0, π].",
                hint2 = "7π/6 is 210°, which is greater than 180° (π).",
                hint3 = "Use cos(2π - θ) = cos θ: 2π - 7π/6 = 5π/6 (150°), which is inside [0, π].",
                estimatedTime = 25,
                xp = 20,
                tags = "Trigonometric Functions, Inverse, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2023",
                teachMeGiven = "Expression: cos⁻¹(cos(7π/6))",
                teachMeWeNeed = "Principal value",
                teachMeFormula = "cos⁻¹(cos θ) = θ ONLY IF θ ∈ [0, π]",
                teachMeSubstitution = "7π/6 = 210° ∉ [0, π]. Write cos(2π - 5π/6) = cos(5π/6)",
                teachMeCalculation = "Since 5π/6 = 150° ∈ [0, π], cos⁻¹(cos(5π/6)) = 5π/6",
                teachMeFinalAnswer = "5π/6 (Option B)",
                teachMeShortcut = "Always subtract from 2π when angle in 3rd quadrant: 2π - 7π/6 = 5π/6."
            )
        )

        // 10. Vectors (Std XII) - Q10
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Vectors",
                topic = "Cross Product",
                subtopic = "Area of Triangle",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The area of a triangle with adjacent sides determined by vectors a = 2i + j - 3k and b = i - 2j + k is:",
                optionA = "(5√6)/2 sq units",
                optionB = "5√6 sq units",
                optionC = "(3√5)/2 sq units",
                optionD = "15 sq units",
                correctAnswer = "A",
                explanation = "Area of triangle = (1/2) |a × b|.\na × b = det([[i, j, k], [2, 1, -3], [1, -2, 1]])\n= i(1 - 6) - j(2 - (-3)) + k(-4 - 1)\n= -5i - 5j - 5k = -5(i + j + k).\n|a × b| = 5√(1² + 1² + 1²) = 5√3. Wait: 1(1) - (-2)(-3) = 1 - 6 = -5. For j: -(2(1) - 1(-3)) = -(2 + 3) = -5. For k: 2(-2) - 1(1) = -4 - 1 = -5.\nMagnitude = √((-5)² + (-5)² + (-5)²) = √(25 * 3) = 5√3. Area = (5√3)/2.",
                formula = "Area of triangle = (1/2) |a × b|",
                shortcut = "Factor out 5: |5(-i - j - k)| = 5√3, area = (5√3)/2.",
                hint1 = "Calculate cross product a × b using 3x3 determinant.",
                hint2 = "The vector cross product comes out to be -5i - 5j - 5k.",
                hint3 = "Find its magnitude √(25+25+25) = 5√3 and divide by 2.",
                estimatedTime = 40,
                xp = 20,
                tags = "Vectors, Cross Product, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2020",
                teachMeGiven = "a = 2i + j - 3k, b = i - 2j + k",
                teachMeWeNeed = "Area of triangle = (1/2) |a × b|",
                teachMeFormula = "a × b = i(b_y c_z - ...) and Area = (1/2)|a × b|",
                teachMeSubstitution = "a × b = -5i - 5j - 5k",
                teachMeCalculation = "|a × b| = √(25+25+25) = 5√3. Area = (5√3)/2",
                teachMeFinalAnswer = "(5√3)/2 sq units",
                teachMeShortcut = "Cross product is -5(i+j+k) ⇒ magnitude 5√3 ⇒ area 5√3/2."
            )
        )

        // Fix Option A for Q10 to (5√3)/2 sq units
        val fixedQ10 = list.removeAt(list.size - 1).copy(
            optionA = "(5√3)/2 sq units",
            optionB = "5√3 sq units",
            optionC = "(3√5)/2 sq units",
            optionD = "15/2 sq units"
        )
        list.add(fixedQ10)

        // 11. Differentiation (Std XII) - Q11
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Differentiation",
                topic = "Parametric Functions",
                subtopic = "Second Order Derivative",
                difficulty = "LEVEL 5 — CET Hard",
                question = "If x = a(θ - sin θ) and y = a(1 - cos θ), then d²y/dx² at θ = π/2 is:",
                optionA = "-1/a",
                optionB = "1/a",
                optionC = "-1/(2a)",
                optionD = "-1/(4a)",
                correctAnswer = "A",
                explanation = "dx/dθ = a(1 - cos θ), dy/dθ = a sin θ.\ndy/dx = (dy/dθ) / (dx/dθ) = (a sin θ) / [a(1 - cos θ)] = (2 sin(θ/2) cos(θ/2)) / (2 sin²(θ/2)) = cot(θ/2).\nNow, d²y/dx² = d/dθ(cot(θ/2)) * (dθ/dx) = [-csc²(θ/2) * (1/2)] * [1 / (a(1 - cos θ))].\nAt θ = π/2: θ/2 = π/4 ⇒ csc(π/4) = √2 ⇒ csc²(π/4) = 2.\n1 - cos(π/2) = 1 - 0 = 1.\nSo d²y/dx² = [-2 * (1/2)] * [1 / (a * 1)] = -1/a.",
                formula = "d²y/dx² = [d/dθ(dy/dx)] / (dx/dθ)",
                shortcut = "Simplify dy/dx to cot(θ/2) before taking 2nd derivative. Never forget dθ/dx at the end!",
                hint1 = "First find dy/dx by dividing dy/dθ by dx/dθ.",
                hint2 = "Simplify dy/dx using half-angle formulas to cot(θ/2).",
                hint3 = "Differentiate cot(θ/2) w.r.t θ and multiply by dθ/dx = 1/[a(1 - cos θ)].",
                estimatedTime = 55,
                xp = 35,
                tags = "Differentiation, Parametric, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "x = a(θ - sin θ), y = a(1 - cos θ), evaluate at θ = π/2",
                teachMeWeNeed = "d²y/dx²",
                teachMeFormula = "dy/dx = (dy/dθ)/(dx/dθ), d²y/dx² = (d/dθ[dy/dx]) * (dθ/dx)",
                teachMeSubstitution = "dy/dx = a sin θ / a(1 - cos θ) = cot(θ/2)",
                teachMeCalculation = "d/dθ(cot(θ/2)) = -1/2 csc²(θ/2). dθ/dx = 1/[a(1-cos θ)]. At θ=π/2: (-1/2 * 2) * (1/a) = -1/a",
                teachMeFinalAnswer = "-1/a (Option A)",
                teachMeShortcut = "cot(π/4)' = -1/2 * 2 = -1; dx/dθ = a(1-0) = a ⇒ result is -1/a."
            )
        )

        // 12. Definite Integration (Std XII) - Q12
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Definite Integration",
                topic = "King's Rule",
                subtopic = "Symmetric Integrals",
                difficulty = "LEVEL 4 — CET Medium",
                question = "Evaluate: I = ∫[0 to π/2] (√sin x) / (√sin x + √cos x) dx.",
                optionA = "π/4",
                optionB = "π/2",
                optionC = "π",
                optionD = "0",
                correctAnswer = "A",
                explanation = "Let I = ∫[0 to π/2] (√sin x) / (√sin x + √cos x) dx  ...(1)\nUsing King's property ∫[0 to a] f(x)dx = ∫[0 to a] f(a - x)dx:\nI = ∫[0 to π/2] (√sin(π/2 - x)) / (√sin(π/2 - x) + √cos(π/2 - x)) dx\nI = ∫[0 to π/2] (√cos x) / (√cos x + √sin x) dx  ...(2)\nAdding (1) and (2): 2I = ∫[0 to π/2] [(√sin x + √cos x)/(√sin x + √cos x)] dx = ∫[0 to π/2] 1 dx = [x][0 to π/2] = π/2.\nThus, 2I = π/2 ⇒ I = π/4.",
                formula = "∫[0 to a] f(x) dx = ∫[0 to a] f(a - x) dx",
                shortcut = "For any integral of form ∫[a to b] [f(x)] / [f(x) + f(a+b-x)] dx, the answer is always (b - a)/2 = (π/2 - 0)/2 = π/4!",
                hint1 = "Apply King's property: replace x by (π/2 - x).",
                hint2 = "Notice that sin(π/2 - x) = cos x and cos(π/2 - x) = sin x.",
                hint3 = "Add the two equations: the integrand becomes 1.",
                estimatedTime = 25,
                xp = 25,
                tags = "Definite Integration, King's Rule, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "I = ∫[0 to π/2] (√sin x)/(√sin x + √cos x) dx",
                teachMeWeNeed = "Value of definite integral",
                teachMeFormula = "∫[a to b] f(x) dx = ∫[a to b] f(a + b - x) dx",
                teachMeSubstitution = "Replace x with π/2 - x to get complementary integrand",
                teachMeCalculation = "2I = ∫[0 to π/2] 1 dx = π/2 ⇒ I = π/4",
                teachMeFinalAnswer = "π/4 (Option A)",
                teachMeShortcut = "Standard CET form: (Upper - Lower)/2 = (π/2 - 0)/2 = π/4!"
            )
        )

        // 13. Application of Definite Integration (Std XII) - Q13
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Application of Definite Integration",
                topic = "Area Between Curves",
                subtopic = "Parabolas",
                difficulty = "LEVEL 5 — CET Hard",
                question = "The area bounded by the parabola y² = 4ax and the line y = mx is given by:",
                optionA = "8a² / (3m³)",
                optionB = "4a² / (3m²)",
                optionC = "16a² / (3m³)",
                optionD = "8a² / (3m)",
                correctAnswer = "A",
                explanation = "Find intersection points: (mx)² = 4ax ⇒ m² x² - 4ax = 0 ⇒ x(m² x - 4a) = 0.\nx = 0 and x = 4a/m².\nArea = ∫[0 to 4a/m²] (√(4ax) - mx) dx\n= [2√a * (2/3) x^(3/2) - (m/2) x²] from 0 to 4a/m²\n= (4/3) √a * (4a/m²)^(3/2) - (m/2) * (16a² / m⁴)\n= (4/3) √a * (8a√a / m³) - 8a² / m³\n= (32a² / 3m³) - (24a² / 3m³) = 8a² / (3m³).",
                formula = "Standard Result: Area between y² = 4ax and y = mx is 8a² / (3m³)",
                shortcut = "Memorize the direct CET standard result: Area = 8a² / (3m³). Solved in 2 seconds!",
                hint1 = "This is a classic standard area between y² = 4ax and y = mx.",
                hint2 = "Find limits of integration by equating y² = 4ax and y = mx.",
                hint3 = "The standard formula result is 8a² / (3m³).",
                estimatedTime = 40,
                xp = 35,
                tags = "Area, Parabola, Line, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2023",
                teachMeGiven = "Curves: y² = 4ax and y = mx",
                teachMeWeNeed = "Bounded area",
                teachMeFormula = "Area = ∫[0 to 4a/m²] (√4ax - mx) dx",
                teachMeSubstitution = "Intersection at x = 0 and x = 4a/m²",
                teachMeCalculation = "Evaluate integral: (32a²/3m³) - (8a²/m³) = 8a² / (3m³)",
                teachMeFinalAnswer = "8a² / (3m³) (Option A)",
                teachMeShortcut = "Standard MHT-CET theorem: Area = 8a²/(3m³)."
            )
        )

        // 14. Differential Equations (Std XII) - Q14
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Differential Equations",
                topic = "Linear Differential Equations",
                subtopic = "Integrating Factor",
                difficulty = "LEVEL 4 — CET Medium",
                question = "The integrating factor of the differential equation x (dy/dx) + 2y = x² (x > 0) is:",
                optionA = "x²",
                optionB = "x",
                optionC = "e²ˣ",
                optionD = "1/x²",
                correctAnswer = "A",
                explanation = "Divide by x to put in standard linear form: dy/dx + (2/x) y = x.\nHere P(x) = 2/x, Q(x) = x.\nIntegrating factor I.F. = e^(∫ P dx) = e^(∫ (2/x) dx) = e^(2 ln x) = e^(ln(x²)) = x².",
                formula = "I.F. = e^(∫ P dx) for dy/dx + Py = Q",
                shortcut = "e^(k ln x) = x^k. Here k = 2, so I.F. = x² immediately!",
                hint1 = "Divide the entire equation by x to isolate dy/dx.",
                hint2 = "Identify P(x) = 2/x.",
                hint3 = "Calculate I.F. = e^(∫ (2/x) dx) = e^(ln(x²)).",
                estimatedTime = 30,
                xp = 25,
                tags = "Differential Equations, Integrating Factor, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "DE: x dy/dx + 2y = x²",
                teachMeWeNeed = "Integrating Factor (I.F.)",
                teachMeFormula = "Standard form: dy/dx + P(x)y = Q(x), I.F. = e^(∫ P(x) dx)",
                teachMeSubstitution = "dy/dx + (2/x)y = x ⇒ P(x) = 2/x",
                teachMeCalculation = "∫(2/x)dx = 2 ln x = ln(x²). e^(ln x²) = x²",
                teachMeFinalAnswer = "x² (Option A)",
                teachMeShortcut = "Coeff of y divided by coeff of dy/dx is 2/x ⇒ I.F. = x²."
            )
        )

        // 15. Probability Distribution (Std XII) - Q15
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Probability Distribution",
                topic = "Expected Value and Variance",
                subtopic = "Discrete RV",
                difficulty = "LEVEL 3 — CET Easy",
                question = "A random variable X has probability distribution P(X = 1) = 0.2, P(X = 2) = 0.5, P(X = 3) = 0.3. Find the expected value E(X).",
                optionA = "2.1",
                optionB = "2.0",
                optionC = "2.3",
                optionD = "1.9",
                correctAnswer = "A",
                explanation = "Expected value E(X) = Σ [x * P(X = x)]\n= (1 * 0.2) + (2 * 0.5) + (3 * 0.3)\n= 0.2 + 1.0 + 0.9 = 2.1.",
                formula = "E(X) = Σ xᵢ P(xᵢ)",
                shortcut = "0.2 + 1.0 + 0.9 = 2.1.",
                hint1 = "Multiply each value of X by its corresponding probability.",
                hint2 = "Calculate 1*0.2, 2*0.5, and 3*0.3.",
                hint3 = "Sum these products: 0.2 + 1.0 + 0.9.",
                estimatedTime = 20,
                xp = 20,
                tags = "Probability Distribution, Expected Value, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "Values: 1, 2, 3 with probabilities 0.2, 0.5, 0.3",
                teachMeWeNeed = "E(X)",
                teachMeFormula = "E(X) = Σ xᵢ P(xᵢ)",
                teachMeSubstitution = "E(X) = 1(0.2) + 2(0.5) + 3(0.3)",
                teachMeCalculation = "= 0.2 + 1.0 + 0.9 = 2.1",
                teachMeFinalAnswer = "2.1 (Option A)",
                teachMeShortcut = "Direct sum: 0.2 + 1.0 + 0.9 = 2.1."
            )
        )

        // 16. Binomial Distribution (Std XII) - Q16
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Binomial Distribution",
                topic = "Mean and Variance",
                subtopic = "Bernoulli Trials",
                difficulty = "LEVEL 3 — CET Easy",
                question = "For a binomial distribution B(n, p), if mean = 4 and variance = 2, find n.",
                optionA = "8",
                optionB = "16",
                optionC = "12",
                optionD = "4",
                correctAnswer = "A",
                explanation = "Mean = np = 4.\nVariance = npq = 2.\nDividing variance by mean: (npq) / (np) = 2/4 ⇒ q = 1/2.\nSince p + q = 1, p = 1 - 1/2 = 1/2.\nNow np = 4 ⇒ n(1/2) = 4 ⇒ n = 8.",
                formula = "Mean = np, Variance = npq, p + q = 1",
                shortcut = "q = Variance / Mean = 2/4 = 0.5 ⇒ p = 0.5 ⇒ n = 4 / 0.5 = 8.",
                hint1 = "Recall that Mean = np and Variance = npq.",
                hint2 = "Divide Variance by Mean to get q = 2/4 = 1/2.",
                hint3 = "Since p = 1 - q = 1/2, find n from np = 4.",
                estimatedTime = 25,
                xp = 20,
                tags = "Binomial Distribution, Mean, Variance, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "np = 4, npq = 2",
                teachMeWeNeed = "Number of trials n",
                teachMeFormula = "q = (npq)/(np), p = 1 - q, n = (np)/p",
                teachMeSubstitution = "q = 2/4 = 1/2 ⇒ p = 1/2",
                teachMeCalculation = "n = 4 / (1/2) = 8",
                teachMeFinalAnswer = "8 (Option A)",
                teachMeShortcut = "q = 2/4 = 0.5 ⇒ n = 4/0.5 = 8."
            )
        )

        // 17. Complex Numbers (Std XI) - Q17
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Complex Numbers",
                topic = "Cube Roots of Unity",
                subtopic = "Omega Properties",
                difficulty = "LEVEL 3 — CET Easy",
                question = "If ω is a complex cube root of unity, then the value of (1 - ω + ω²)⁵ + (1 + ω - ω²)⁵ is:",
                optionA = "32",
                optionB = "-32",
                optionC = "64",
                optionD = "0",
                correctAnswer = "A",
                explanation = "Recall that 1 + ω + ω² = 0, so 1 + ω² = -ω and 1 + ω = -ω².\nFirst term: (1 - ω + ω²)⁵ = (-ω - ω)⁵ = (-2ω)⁵ = -32 ω⁵ = -32 ω² (since ω³ = 1, ω⁵ = ω²).\nSecond term: (1 + ω - ω²)⁵ = (-ω² - ω²)⁵ = (-2ω²)⁵ = -32 ω¹⁰ = -32 ω (since ω¹⁰ = ω).\nSum = -32 ω² - 32 ω = -32(ω² + ω).\nSince 1 + ω + ω² = 0, ω + ω² = -1.\nSum = -32(-1) = 32.",
                formula = "1 + ω + ω² = 0 and ω³ = 1",
                shortcut = "(-2ω)⁵ + (-2ω²)⁵ = -32(ω² + ω) = -32(-1) = 32.",
                hint1 = "Use the fundamental identity 1 + ω + ω² = 0.",
                hint2 = "Replace (1 + ω²) with -ω and (1 + ω) with -ω².",
                hint3 = "Factor out -32 and use ω + ω² = -1.",
                estimatedTime = 35,
                xp = 20,
                tags = "Complex Numbers, Cube Roots, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2020",
                teachMeGiven = "ω is non-real cube root of unity",
                teachMeWeNeed = "(1 - ω + ω²)⁵ + (1 + ω - ω²)⁵",
                teachMeFormula = "1 + ω + ω² = 0 and ω³ = 1",
                teachMeSubstitution = "( -2ω )⁵ + ( -2ω² )⁵",
                teachMeCalculation = "= -32 ω² - 32 ω = -32(ω² + ω) = -32(-1) = 32",
                teachMeFinalAnswer = "32 (Option A)",
                teachMeShortcut = "-32 * (-1) = 32."
            )
        )

        // 18. Conic Section (Std XI) - Q18
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Conic Section",
                topic = "Parabola",
                subtopic = "Focal Distance",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The focal distance of a point P(x, y) on the parabola y² = 12x with abscissa x = 5 is:",
                optionA = "8 units",
                optionB = "6 units",
                optionC = "11 units",
                optionD = "17 units",
                correctAnswer = "A",
                explanation = "For the standard parabola y² = 4ax, 4a = 12 ⇒ a = 3.\nThe focal distance of any point P(x, y) on y² = 4ax is given by x + a.\nGiven x = 5 and a = 3, Focal distance = 5 + 3 = 8 units.",
                formula = "Focal distance of P(x, y) on y² = 4ax is SP = x + a",
                shortcut = "a = 12/4 = 3. Focal distance = x + a = 5 + 3 = 8.",
                hint1 = "Compare y² = 12x with standard form y² = 4ax to find 'a'.",
                hint2 = "4a = 12 implies a = 3.",
                hint3 = "Focal distance formula for y² = 4ax is simply x + a.",
                estimatedTime = 20,
                xp = 20,
                tags = "Conic Section, Parabola, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2023",
                teachMeGiven = "Parabola y² = 12x, point has x = 5",
                teachMeWeNeed = "Focal distance SP",
                teachMeFormula = "For y² = 4ax, SP = x + a",
                teachMeSubstitution = "4a = 12 ⇒ a = 3. x = 5",
                teachMeCalculation = "SP = 5 + 3 = 8",
                teachMeFinalAnswer = "8 units (Option A)",
                teachMeShortcut = "x + a = 5 + 3 = 8."
            )
        )

        // 19. Three Dimensional Geometry (Std XII) - Q19
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Three Dimensional Geometry",
                topic = "Direction Cosines",
                subtopic = "Angle with axes",
                difficulty = "LEVEL 3 — CET Easy",
                question = "If a directed line makes angles 45° and 60° with the positive X and Y axes respectively, what angle does it make with the positive Z-axis?",
                optionA = "60° or 120°",
                optionB = "45° or 135°",
                optionC = "30° or 150°",
                optionD = "90°",
                correctAnswer = "A",
                explanation = "Let the angles with axes be α = 45°, β = 60°, and γ.\nWe have the identity: cos² α + cos² β + cos² γ = 1.\ncos² 45° + cos² 60° + cos² γ = 1\n(1/√2)² + (1/2)² + cos² γ = 1\n1/2 + 1/4 + cos² γ = 1\n3/4 + cos² γ = 1 ⇒ cos² γ = 1 - 3/4 = 1/4.\ncos γ = ±1/2 ⇒ γ = 60° or 120°.",
                formula = "l² + m² + n² = cos² α + cos² β + cos² γ = 1",
                shortcut = "1/2 + 1/4 = 3/4. Remaining is 1/4 ⇒ cos γ = ±1/2 ⇒ 60° or 120°.",
                hint1 = "Recall the fundamental relation between direction cosines: cos² α + cos² β + cos² γ = 1.",
                hint2 = "Calculate cos² 45° = 1/2 and cos² 60° = 1/4.",
                hint3 = "cos² γ = 1 - (1/2 + 1/4) = 1/4. Solve for γ.",
                estimatedTime = 30,
                xp = 20,
                tags = "3D Geometry, Direction Cosines, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "α = 45°, β = 60°",
                teachMeWeNeed = "Angle γ with positive Z-axis",
                teachMeFormula = "cos² α + cos² β + cos² γ = 1",
                teachMeSubstitution = "cos² 45° + cos² 60° + cos² γ = 1",
                teachMeCalculation = "1/2 + 1/4 + cos² γ = 1 ⇒ cos² γ = 1/4 ⇒ cos γ = ±1/2 ⇒ γ = 60° or 120°",
                teachMeFinalAnswer = "60° or 120° (Option A)",
                teachMeShortcut = "1 - 0.75 = 0.25 ⇒ cos γ = ±0.5 ⇒ 60°/120°."
            )
        )

        // 20. Linear Programming (Std XII) - Q20
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Linear Programming",
                topic = "Corner Point Method",
                subtopic = "Maximization",
                difficulty = "LEVEL 2 — Concept",
                question = "Maximize Z = 3x + 5y subject to constraints x + 2y ≤ 10, 3x + y ≤ 15, x ≥ 0, y ≥ 0. The maximum value of Z is:",
                optionA = "27",
                optionB = "25",
                optionC = "20",
                optionD = "30",
                correctAnswer = "A",
                explanation = "Find corner points of feasible region:\n1. Origin: (0, 0) ⇒ Z = 0.\n2. On y-axis: x = 0, x + 2y = 10 ⇒ y = 5. Point (0, 5) ⇒ Z = 3(0) + 5(5) = 25.\n3. On x-axis: y = 0, 3x + y = 15 ⇒ x = 5. Point (5, 0) ⇒ Z = 3(5) + 5(0) = 15.\n4. Intersection of x + 2y = 10 and 3x + y = 15:\nMultiply 2nd by 2: 6x + 2y = 30. Subtract 1st: 5x = 20 ⇒ x = 4. Then y = (10 - 4)/2 = 3.\nCorner point is (4, 3).\nZ at (4, 3) = 3(4) + 5(3) = 12 + 15 = 27.\nMaximum value is 27.",
                formula = "Evaluate objective function Z at each corner point of feasible region.",
                shortcut = "Check intersection (4, 3): 3(4) + 5(3) = 27, which is greater than (0, 5) giving 25.",
                hint1 = "Determine the coordinates of the corner points of the bounded feasible region.",
                hint2 = "Corner points are (0, 0), (5, 0), (0, 5), and (4, 3).",
                hint3 = "Plug each into Z = 3x + 5y to find the maximum value.",
                estimatedTime = 45,
                xp = 20,
                tags = "LPP, Optimization, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "Maximize Z = 3x + 5y, x+2y ≤ 10, 3x+y ≤ 15, x, y ≥ 0",
                teachMeWeNeed = "Maximum Z",
                teachMeFormula = "Test all corner points of the polygon",
                teachMeSubstitution = "Corner points: (0, 0), (5, 0), (0, 5), (4, 3)",
                teachMeCalculation = "Z(0,0)=0, Z(5,0)=15, Z(0,5)=25, Z(4,3)=3(4)+5(3)=27",
                teachMeFinalAnswer = "27 (Option A)",
                teachMeShortcut = "Intersection (4, 3) yields highest value: 12 + 15 = 27."
            )
        )

        // 21. Functions (Std XI) - Q21
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Functions",
                topic = "Domain and Range",
                subtopic = "Square Root Function",
                difficulty = "LEVEL 2 — Concept",
                question = "The domain of the real-valued function f(x) = √(16 - x²) is:",
                optionA = "[-4, 4]",
                optionB = "(-4, 4)",
                optionC = "(-∞, -4] ∪ [4, ∞)",
                optionD = "[0, 4]",
                correctAnswer = "A",
                explanation = "For f(x) to be real, the quantity under the square root must be non-negative: 16 - x² ≥ 0.\nThis implies x² ≤ 16, which means -4 ≤ x ≤ 4.\nTherefore, the domain is the closed interval [-4, 4].",
                formula = "For √g(x), domain condition is g(x) ≥ 0",
                shortcut = "x² ≤ 16 ⇒ |x| ≤ 4 ⇒ [-4, 4].",
                hint1 = "The expression inside a real square root must be greater than or equal to zero.",
                hint2 = "Set 16 - x² ≥ 0.",
                hint3 = "This means x² ≤ 16, so -4 ≤ x ≤ 4.",
                estimatedTime = 20,
                xp = 15,
                tags = "Functions, Domain, XI",
                isPyq = false,
                teachMeGiven = "f(x) = √(16 - x²)",
                teachMeWeNeed = "Domain of f(x)",
                teachMeFormula = "16 - x² ≥ 0",
                teachMeSubstitution = "x² ≤ 16",
                teachMeCalculation = "-4 ≤ x ≤ 4",
                teachMeFinalAnswer = "[-4, 4] (Option A)",
                teachMeShortcut = "|x| ≤ √16 = 4 ⇒ [-4, 4]."
            )
        )

        // 22. Continuity (Std XI) - Q22
        list.add(
            QuestionEntity(
                stdClass = 11,
                chapter = "Continuity",
                topic = "Continuity at a Point",
                subtopic = "Finding Unknown Constant",
                difficulty = "LEVEL 3 — CET Easy",
                question = "If f(x) = (sin 5x)/(2x) for x ≠ 0 and f(0) = k is continuous at x = 0, then k is:",
                optionA = "5/2",
                optionB = "2/5",
                optionC = "5",
                optionD = "0",
                correctAnswer = "A",
                explanation = "Since f(x) is continuous at x = 0, f(0) = lim(x→0) f(x).\nk = lim(x→0) (sin 5x)/(2x) = (5/2) * lim(x→0) (sin 5x)/(5x) = (5/2) * 1 = 5/2.",
                formula = "f(0) = lim(x→0) f(x) for continuity at x = 0",
                shortcut = "Ratio of coefficients of x: 5/2. So k = 5/2 directly.",
                hint1 = "Continuity requires that the limit as x approaches 0 equals the function value f(0).",
                hint2 = "Evaluate lim(x→0) (sin 5x)/(2x).",
                hint3 = "Use standard limit lim(θ→0) (sin θ)/θ = 1 to get (5/2).",
                estimatedTime = 20,
                xp = 20,
                tags = "Continuity, Limits, XI",
                isPyq = true,
                pyqYear = "MHT-CET 2020",
                teachMeGiven = "f(x) = (sin 5x)/(2x) for x≠0, f(0) = k, continuous at x=0",
                teachMeWeNeed = "Value of k",
                teachMeFormula = "k = lim(x→0) f(x)",
                teachMeSubstitution = "k = lim(x→0) (sin 5x)/(2x)",
                teachMeCalculation = "k = (5/2) * lim(x→0) (sin 5x)/(5x) = (5/2) * 1 = 5/2",
                teachMeFinalAnswer = "5/2 (Option A)",
                teachMeShortcut = "Take ratio of coefficients: 5/2."
            )
        )

        // 23. Applications of Derivatives (Std XII) - Q23
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Applications of Derivatives",
                topic = "Tangent and Normal",
                subtopic = "Slope of Tangent",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The slope of the normal to the curve y = 2x² + 3 sin x at x = 0 is:",
                optionA = "-1/3",
                optionB = "3",
                optionC = "-3",
                optionD = "1/3",
                correctAnswer = "A",
                explanation = "Differentiate curve: dy/dx = 4x + 3 cos x.\nAt x = 0: dy/dx = 4(0) + 3 cos(0) = 0 + 3(1) = 3.\nThis is the slope of the tangent m_t = 3.\nSlope of the normal m_n = -1 / m_t = -1/3.",
                formula = "Slope of normal = -1 / (dy/dx)",
                shortcut = "dy/dx at 0 is 3 ⇒ normal slope is -1/3 instantly.",
                hint1 = "Find dy/dx by differentiating 2x² + 3 sin x.",
                hint2 = "Substitute x = 0 into dy/dx to get the tangent slope.",
                hint3 = "The normal is perpendicular to the tangent, so its slope is -1 / (tangent slope).",
                estimatedTime = 25,
                xp = 20,
                tags = "AOD, Normal, Slope, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2021",
                teachMeGiven = "Curve y = 2x² + 3 sin x at x = 0",
                teachMeWeNeed = "Slope of normal",
                teachMeFormula = "m_normal = -1 / (dy/dx)",
                teachMeSubstitution = "dy/dx = 4x + 3 cos x. At x=0: 4(0) + 3(1) = 3",
                teachMeCalculation = "m_normal = -1 / 3",
                teachMeFinalAnswer = "-1/3 (Option A)",
                teachMeShortcut = "dy/dx = 3 ⇒ normal slope = -1/3."
            )
        )

        // 24. Indefinite Integration (Std XII) - Q24
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Indefinite Integration",
                topic = "Special Integrals",
                subtopic = "eˣ [f(x) + f'(x)]",
                difficulty = "LEVEL 3 — CET Easy",
                question = "Evaluate: ∫ eˣ [ (1 + x ln x) / x ] dx.",
                optionA = "eˣ ln x + C",
                optionB = "eˣ / x + C",
                optionC = "(eˣ ln x) / x + C",
                optionD = "eˣ (1 + ln x) + C",
                correctAnswer = "A",
                explanation = "Rewrite the integrand: (1 + x ln x) / x = (1/x) + ln x = ln x + (1/x).\nLet f(x) = ln x, then f'(x) = 1/x.\nThe integral is of the standard form ∫ eˣ [f(x) + f'(x)] dx = eˣ f(x) + C.\nHence, ∫ eˣ [ln x + (1/x)] dx = eˣ ln x + C.",
                formula = "∫ eˣ [f(x) + f'(x)] dx = eˣ f(x) + C",
                shortcut = "Recognize ln x and its derivative 1/x multiplied by eˣ. Answer is eˣ ln x + C!",
                hint1 = "Split the fraction (1 + x ln x) / x into two terms.",
                hint2 = "It becomes ln x + 1/x.",
                hint3 = "Notice 1/x is the exact derivative of ln x: use ∫ eˣ[f + f']dx = eˣ f.",
                estimatedTime = 25,
                xp = 20,
                tags = "Indefinite Integration, eˣ f(x), XII",
                isPyq = true,
                pyqYear = "MHT-CET 2022",
                teachMeGiven = "∫ eˣ [ (1 + x ln x)/x ] dx",
                teachMeWeNeed = "Antiderivative",
                teachMeFormula = "∫ eˣ [f(x) + f'(x)] dx = eˣ f(x) + C",
                teachMeSubstitution = "f(x) = ln x, f'(x) = 1/x",
                teachMeCalculation = "eˣ f(x) + C = eˣ ln x + C",
                teachMeFinalAnswer = "eˣ ln x + C (Option A)",
                teachMeShortcut = "Standard form gives eˣ ln x directly."
            )
        )

        // 25. Pair of Straight Lines (Std XII) - Q25
        list.add(
            QuestionEntity(
                stdClass = 12,
                chapter = "Pair of Straight Lines",
                topic = "Angle Between Pair of Lines",
                subtopic = "Homogeneous Equation",
                difficulty = "LEVEL 3 — CET Easy",
                question = "The acute angle between the pair of lines represented by 2x² - 7xy + 3y² = 0 is:",
                optionA = "45°",
                optionB = "30°",
                optionC = "60°",
                optionD = "90°",
                correctAnswer = "A",
                explanation = "Comparing with ax² + 2hxy + by² = 0: a = 2, 2h = -7 ⇒ h = -7/2, b = 3.\ntan θ = |2√(h² - ab) / (a + b)|\nh² - ab = (-7/2)² - (2 * 3) = 49/4 - 6 = 49/4 - 24/4 = 25/4.\n√(h² - ab) = 5/2.\ntan θ = |2 * (5/2) / (2 + 3)| = |5 / 5| = 1.\nSince tan θ = 1, θ = 45°.",
                formula = "tan θ = |2√(h² - ab) / (a + b)|",
                shortcut = "h² - ab = 25/4 ⇒ 2√(25/4) = 5. a + b = 2 + 3 = 5. tan θ = 5/5 = 1 ⇒ θ = 45°.",
                hint1 = "Identify coefficients: a = 2, 2h = -7, b = 3.",
                hint2 = "Calculate h² - ab = 49/4 - 6 = 25/4.",
                hint3 = "Use tan θ = |2√(h² - ab) / (a + b)| = 5/5 = 1.",
                estimatedTime = 35,
                xp = 20,
                tags = "Pair of Lines, Angle, XII",
                isPyq = true,
                pyqYear = "MHT-CET 2023",
                teachMeGiven = "2x² - 7xy + 3y² = 0",
                teachMeWeNeed = "Acute angle θ",
                teachMeFormula = "tan θ = |2√(h² - ab) / (a + b)|",
                teachMeSubstitution = "a = 2, b = 3, h = -7/2",
                teachMeCalculation = "h² - ab = 49/4 - 6 = 25/4. tan θ = 2(5/2) / (2+3) = 5/5 = 1 ⇒ θ = 45°",
                teachMeFinalAnswer = "45° (Option A)",
                teachMeShortcut = "tan θ = 5/5 = 1 ⇒ 45°."
            )
        )

        return list
    }
}
