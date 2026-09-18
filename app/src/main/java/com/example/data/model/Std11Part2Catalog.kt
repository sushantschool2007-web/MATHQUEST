package com.example.data.model

object Std11Part2Catalog {
    val chapters: List<ComprehensiveChapter> = listOf(
        // Chapter 12
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH12",
            name = "Sets, Relations and Functions",
            standard = 11,
            part = 2,
            chapterNumber = 12,
            description = "Sets, subsets, Venn diagrams, Cartesian product, relation types, classification of functions, composition and inverse functions.",
            subtopics = listOf(
                "Sets", "Subsets", "Proper subsets", "Universal set", "Empty set", "Union",
                "Intersection", "Difference", "Complement", "Venn diagrams", "Cartesian product",
                "Relations", "Domain", "Codomain", "Range", "Types of relations", "Functions",
                "One-one", "Many-one", "Into", "Onto", "Even functions", "Odd functions",
                "Polynomial functions", "Rational functions", "Modulus function", "Signum function",
                "Greatest integer function", "Exponential function", "Logarithmic function",
                "Composite functions", "Inverse functions", "Binary operations", "Graphs"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH12_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_12_01",
                    chapterId = "STD11_P2_CH12",
                    topicId = "Inclusion-Exclusion",
                    formulaName = "Cardinality of Union of Two & Three Sets",
                    formula = "n(A \\cup B) = n(A) + n(B) - n(A \\cap B), \\quad n(A \\cup B \\cup C) = \\sum n(A) - \\sum n(A \\cap B) + n(A \\cap B \\cap C)",
                    description = "Inclusion-exclusion principle for finite sets."
                ),
                FormulaItem(
                    formulaId = "FOR_11_12_02",
                    chapterId = "STD11_P2_CH12",
                    topicId = "Function Composition",
                    formulaName = "Composite Function",
                    formula = "(f \\circ g)(x) = f(g(x)), \\quad (g \\circ f)(x) = g(f(x))",
                    description = "Composition of functions valid when Range(g) ⊆ Domain(f)."
                )
            ),
            importantConcepts = listOf(
                "Injective (One-one): f(x₁) = f(x₂) implies x₁ = x₂.",
                "Surjective (Onto): Range of f equals Codomain of f.",
                "Bijective (Invertible): Both injective and surjective."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_12_01",
                    title = "Domain of Rational Function",
                    problem = "Find domain of f(x) = 1 / √(x² - 9).",
                    solution = "The expression under square root in denominator must be strictly positive: x² - 9 > 0 \\implies (x-3)(x+3) > 0.\nWavy curve method: x ∈ (-∞, -3) ∪ (3, ∞).",
                    keyConcept = "Domain conditions for radicals in denominators"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 2-4 marks in Std 11 CET; foundational for all calculus.",
            jeeMainRelevance = "High importance; domain/range and composite questions appear frequently.",
            cetWeightageMarks = 4
        ),

        // Chapter 13
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH13",
            name = "Logarithms",
            standard = 11,
            part = 2,
            chapterNumber = 13,
            description = "Definition of logarithm, fundamental laws, change of base formula, characteristic, mantissa, logarithmic equations.",
            subtopics = listOf(
                "Definition", "Laws", "Properties", "Change of base",
                "Characteristic", "Mantissa", "Antilogarithm", "Logarithmic equations"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH13_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_13_01",
                    chapterId = "STD11_P2_CH13",
                    topicId = "Log Properties",
                    formulaName = "Laws of Logarithms",
                    formula = "\\log_b(mn) = \\log_b m + \\log_b n, \\quad \\log_b(m/n) = \\log_b m - \\log_b n, \\quad \\log_b(m^k) = k \\log_b m",
                    description = "Fundamental algebraic laws of logarithms for m, n, b > 0, b ≠ 1."
                ),
                FormulaItem(
                    formulaId = "FOR_11_13_02",
                    chapterId = "STD11_P2_CH13",
                    topicId = "Change of Base",
                    formulaName = "Change of Base Formula",
                    formula = "\\log_b a = \\frac{\\log_c a}{\\log_c b} = \\frac{1}{\\log_a b}",
                    description = "Converts any logarithmic base to a convenient base (such as base e or 10)."
                )
            ),
            importantConcepts = listOf(
                "For log_b(a) to exist: a > 0, b > 0, and b ≠ 1.",
                "b^(log_b a) = a, and a^(log_b c) = c^(log_b a)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_13_01",
                    title = "Logarithmic Equation",
                    problem = "Solve for x: \\log_2(x - 1) + \\log_2(x + 1) = 3.",
                    solution = "\\log_2((x - 1)(x + 1)) = 3 \\implies x^2 - 1 = 2^3 = 8 \\implies x^2 = 9 \\implies x = 3 (since x > 1 for log domain).",
                    keyConcept = "Domain checking in log equations"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "Directly used in Std 12 Logarithmic Differentiation.",
            jeeMainRelevance = "Tool used across algebra, calculus, and physical chemistry.",
            cetWeightageMarks = 2
        ),

        // Chapter 14
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH14",
            name = "Complex Numbers",
            standard = 11,
            part = 2,
            chapterNumber = 14,
            description = "Algebra of complex numbers, modulus and argument, Argand plane, polar form, De Moivre's theorem, cube roots of unity.",
            subtopics = listOf(
                "Real numbers", "Imaginary numbers", "Complex numbers", "Real part",
                "Imaginary part", "Algebra of complex numbers", "Conjugate", "Modulus",
                "Argument", "Argand plane", "Polar form", "Powers of i", "De Moivre's theorem",
                "Roots", "Cube roots of unity", "Quadratic equations with complex roots"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH14_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_14_01",
                    chapterId = "STD11_P2_CH14",
                    topicId = "Polar Form",
                    formulaName = "Polar and Euler Form",
                    formula = "z = r(\\cos\\theta + i\\sin\\theta) = re^{i\\theta}, \\quad r = |z| = \\sqrt{a^2 + b^2}, \\quad \\theta = \\arg(z)",
                    description = "Trigonometric and exponential polar representation."
                ),
                FormulaItem(
                    formulaId = "FOR_11_14_02",
                    chapterId = "STD11_P2_CH14",
                    topicId = "Cube Roots of Unity",
                    formulaName = "Cube Roots of Unity (1, ω, ω²)",
                    formula = "1 + \\omega + \\omega^2 = 0, \\quad \\omega^3 = 1, \\quad \\omega = \\frac{-1 + i\\sqrt{3}}{2}",
                    description = "Properties of non-real cube roots of unity."
                )
            ),
            importantConcepts = listOf(
                "Powers of i: i = √(-1), i² = -1, i³ = -i, i⁴ = 1. i^(4k+r) = i^r.",
                "|z₁ z₂| = |z₁| |z₂|, and arg(z₁ z₂) = arg(z₁) + arg(z₂)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_14_01",
                    title = "Cube Roots of Unity Simplification",
                    problem = "Find the value of (1 + ω - ω²)³.",
                    solution = "Since 1 + \\omega = -\\omega^2:\n(1 + \\omega - \\omega^2)^3 = (-\\omega^2 - \\omega^2)^3 = (-2\\omega^2)^3 = -8\\omega^6 = -8(1) = -8.",
                    keyConcept = "1 + ω = -ω² substitution"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 2-4 marks in Std 11 CET.",
            jeeMainRelevance = "Heavyweight topic in JEE Main with guaranteed questions.",
            cetWeightageMarks = 4
        ),

        // Chapter 15
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH15",
            name = "Sequences and Series",
            standard = 11,
            part = 2,
            chapterNumber = 15,
            description = "AP, GP, HP, AM-GM-HM relationship, AGP, sum of special series (natural numbers, squares, cubes), exponential and logarithmic series.",
            subtopics = listOf(
                "Sequence", "Arithmetic progression", "Geometric progression", "Harmonic progression",
                "General term", "Sum of AP", "Sum of GP", "Infinite GP", "AM", "GM", "HM",
                "AM-GM-HM relationship", "Arithmetico-geometric progression", "Special series",
                "Sum of natural numbers", "Sum of squares", "Sum of cubes", "Sum of odd numbers",
                "Exponential series", "Logarithmic series"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH15_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_15_01",
                    chapterId = "STD11_P2_CH15",
                    topicId = "Progression Sums",
                    formulaName = "Sums of AP & GP",
                    formula = "S_n(AP) = \\frac{n}{2}[2a + (n-1)d], \\quad S_n(GP) = \\frac{a(r^n - 1)}{r - 1}, \\quad S_\\infty(GP) = \\frac{a}{1 - r} (|r| < 1)",
                    description = "Sum of first n terms of arithmetic and geometric series, and sum of infinite GP."
                ),
                FormulaItem(
                    formulaId = "FOR_11_15_02",
                    chapterId = "STD11_P2_CH15",
                    topicId = "Special Sums",
                    formulaName = "Sum of Powers of Natural Numbers",
                    formula = "\\sum n = \\frac{n(n+1)}{2}, \\quad \\sum n^2 = \\frac{n(n+1)(2n+1)}{6}, \\quad \\sum n^3 = \\left[\\frac{n(n+1)}{2}\\right]^2",
                    description = "Standard sigma summation formulas for natural numbers."
                ),
                FormulaItem(
                    formulaId = "FOR_11_15_03",
                    chapterId = "STD11_P2_CH15",
                    topicId = "Inequality",
                    formulaName = "AM-GM-HM Inequality",
                    formula = "AM \\ge GM \\ge HM, \\quad AM = \\frac{a+b}{2}, \\, GM = \\sqrt{ab}, \\, HM = \\frac{2ab}{a+b}",
                    description = "Valid for positive real numbers a and b, equality holds iff a = b."
                )
            ),
            importantConcepts = listOf(
                "For infinite GP: Sum exists only if |r| < 1.",
                "In an AP, the sum of terms equidistant from the beginning and end is constant and equal to a + l."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_15_01",
                    title = "Sum to Infinity of GP",
                    problem = "Find the sum to infinity of 6 + 2 + 2/3 + 2/9 + ...",
                    solution = "a = 6, r = 2/6 = 1/3. Since |r| < 1:\nS_\\infty = \\frac{a}{1 - r} = \\frac{6}{1 - 1/3} = \\frac{6}{2/3} = 9.",
                    keyConcept = "S = a / (1 - r)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct 2-4 marks in CET.",
            jeeMainRelevance = "Frequently tested in JEE Main with AGP and telescoping series.",
            cetWeightageMarks = 4
        ),

        // Chapter 16
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH16",
            name = "Permutations and Combinations",
            standard = 11,
            part = 2,
            chapterNumber = 16,
            description = "Fundamental counting principles, factorial notation, linear and circular permutations, combinations, nPr and nCr properties.",
            subtopics = listOf(
                "Fundamental counting principle", "Factorial", "Permutations",
                "Permutations with repetition", "Circular permutations", "Combinations",
                "Properties of nCr", "Applications"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH16_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_16_01",
                    chapterId = "STD11_P2_CH16",
                    topicId = "Permutations & Combinations",
                    formulaName = "Permutations and Combinations Formulae",
                    formula = "^nP_r = \\frac{n!}{(n-r)!}, \\quad ^nC_r = \\frac{n!}{r!(n-r)!}, \\quad ^nC_r = ^nC_{n-r}",
                    description = "Standard arrangement (ordered) and selection (unordered) formulas."
                ),
                FormulaItem(
                    formulaId = "FOR_11_16_02",
                    chapterId = "STD11_P2_CH16",
                    topicId = "Pascal's Identity",
                    formulaName = "Pascal's Addition Formula",
                    formula = "^nC_r + ^nC_{r-1} = ^{n+1}C_r",
                    description = "Core combinatorial recurrence identity."
                )
            ),
            importantConcepts = listOf(
                "Circular permutations: (n - 1)! for distinct objects, (n - 1)! / 2 for necklaces/garlands.",
                "Permutations with identical items: n! / (p! q! r!)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_16_01",
                    title = "Committee Selection",
                    problem = "In how many ways can a committee of 3 men and 2 women be chosen from 5 men and 4 women?",
                    solution = "Ways = ^5C_3 \\times ^4C_2 = \\frac{5 \\times 4}{2} \\times \\frac{4 \\times 3}{2} = 10 \\times 6 = 60.",
                    keyConcept = "Multiplication rule of independent selections"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 4 marks (2 questions) in Std 11 CET.",
            jeeMainRelevance = "One of the key chapters in JEE Main; essential for Probability.",
            cetWeightageMarks = 4
        ),

        // Chapter 17
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH17",
            name = "Mathematical Induction and Binomial Theorem",
            standard = 11,
            part = 2,
            chapterNumber = 17,
            description = "Principle of mathematical induction, binomial theorem for positive integral index, general term, middle term, properties of binomial coefficients.",
            subtopics = listOf(
                "Principle of mathematical induction", "Base case", "Inductive hypothesis",
                "Inductive step", "Applications", "Binomial theorem", "General term",
                "Middle term", "Binomial coefficients", "Properties", "Positive integral index",
                "Any index", "Applications"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH17_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_17_01",
                    chapterId = "STD11_P2_CH17",
                    topicId = "Binomial Expansion",
                    formulaName = "Binomial Theorem for Positive Integral Index",
                    formula = "(a + b)^n = \\sum_{r=0}^n {^nC_r} a^{n-r} b^r, \\quad T_{r+1} = {^nC_r} a^{n-r} b^r",
                    description = "Expansion formula and general term (r+1)-th term."
                ),
                FormulaItem(
                    formulaId = "FOR_11_17_02",
                    chapterId = "STD11_P2_CH17",
                    topicId = "Binomial Coefficients",
                    formulaName = "Sum of Binomial Coefficients",
                    formula = "C_0 + C_1 + C_2 + \\dots + C_n = 2^n, \\quad C_0 + C_2 + C_4 + \\dots = 2^{n-1}",
                    description = "Total sum of coefficients in (1 + x)^n evaluated at x = 1."
                )
            ),
            importantConcepts = listOf(
                "Total number of terms in expansion of (a + b)^n is (n + 1).",
                "Middle term: If n is even, single middle term is T_(n/2 + 1). If n is odd, two middle terms: T_((n+1)/2) and T_((n+3)/2)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_17_01",
                    title = "Term Independent of x",
                    problem = "Find term independent of x in (x + 1/x)⁶.",
                    solution = "General term T_{r+1} = ^6C_r x^{6-r} (x^{-1})^r = ^6C_r x^{6-2r}.\nFor independent term: 6 - 2r = 0 \\implies r = 3.\nT_4 = ^6C_3 = \\frac{6 \\times 5 \\times 4}{3 \\times 2 \\times 1} = 20.",
                    keyConcept = "Set power of x to 0 for independent term"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct 2 marks in MHT-CET.",
            jeeMainRelevance = "Guaranteed 1 question on binomial coefficients and remainder theorem.",
            cetWeightageMarks = 2
        ),

        // Chapter 18
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH18",
            name = "Limits",
            standard = 11,
            part = 2,
            chapterNumber = 18,
            description = "Concept of limit, LHL and RHL, standard algebraic, trigonometric, exponential and logarithmic limits, limits at infinity.",
            subtopics = listOf(
                "Concept of limit", "Left-hand limit", "Right-hand limit", "Algebra of limits",
                "Standard limits", "Limits at infinity", "Important trigonometric limits",
                "Exponential limits", "Logarithmic limits"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH18_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_18_01",
                    chapterId = "STD11_P2_CH18",
                    topicId = "Standard Limits",
                    formulaName = "Fundamental Limit Identities",
                    formula = "\\lim_{x \\to 0}\\frac{\\sin x}{x} = 1, \\quad \\lim_{x \\to a}\\frac{x^n - a^n}{x - a} = n a^{n-1}, \\quad \\lim_{x \\to 0}\\frac{e^x - 1}{x} = 1, \\quad \\lim_{x \\to 0}\\frac{\\ln(1+x)}{x} = 1",
                    description = "Core standard limit evaluations with angle in radians."
                ),
                FormulaItem(
                    formulaId = "FOR_11_18_02",
                    chapterId = "STD11_P2_CH18",
                    topicId = "Power Limit",
                    formulaName = "Exponential Base e Limit",
                    formula = "\\lim_{x \\to 0} (1 + x)^{1/x} = e, \\quad \\lim_{x \\to \\infty} \\left(1 + \\frac{1}{x}\\right)^x = e",
                    description = "Foundation of compound growth and Euler's constant."
                )
            ),
            importantConcepts = listOf(
                "Limit exists iff LHL = RHL = finite real value.",
                "Indeterminate forms: 0/0, ∞/∞, 0 × ∞, ∞ - ∞, 1^∞, 0^0, ∞^0."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_18_01",
                    title = "Standard Trigonometric Limit",
                    problem = "Evaluate \\lim_{x \\to 0} \\frac{1 - \\cos 2x}{x^2}.",
                    solution = "1 - \\cos 2x = 2\\sin^2 x.\n\\lim_{x \\to 0} \\frac{2\\sin^2 x}{x^2} = 2 \\left(\\lim_{x \\to 0}\\frac{\\sin x}{x}\\right)^2 = 2(1)^2 = 2.",
                    keyConcept = "Power reduction 1 - cos 2x = 2 sin²x"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 2-4 marks in Std 11 CET; foundational for Std 12 Continuity & Diff.",
            jeeMainRelevance = "Heavily tested in JEE Main with 1^∞ form and expansion series.",
            cetWeightageMarks = 4
        ),

        // Chapter 19
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH19",
            name = "Differentiation",
            standard = 11,
            part = 2,
            chapterNumber = 19,
            description = "Definition of derivative from first principles, geometrical and physical interpretations, sum, difference, product and quotient rules.",
            subtopics = listOf(
                "Derivative", "First principle", "Derivative at a point",
                "Geometrical interpretation", "Physical interpretation", "Algebraic functions",
                "Trigonometric functions", "Exponential functions", "Logarithmic functions",
                "Sum rule", "Difference rule", "Product rule", "Quotient rule", "Higher-order derivatives"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH19_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_19_01",
                    chapterId = "STD11_P2_CH19",
                    topicId = "First Principle",
                    formulaName = "Definition of Derivative",
                    formula = "f'(x) = \\lim_{h \\to 0} \\frac{f(x+h) - f(x)}{h}",
                    description = "First principle derivative defining the instantaneous rate of change."
                ),
                FormulaItem(
                    formulaId = "FOR_11_19_02",
                    chapterId = "STD11_P2_CH19",
                    topicId = "Product & Quotient",
                    formulaName = "Product and Quotient Rules",
                    formula = "(uv)' = u'v + uv', \\quad \\left(\\frac{u}{v}\\right)' = \\frac{u'v - uv'}{v^2}",
                    description = "Leibniz product rule and quotient rule for differentiable functions."
                )
            ),
            importantConcepts = listOf(
                "d/dx(x^n) = n x^(n-1), d/dx(sin x) = cos x, d/dx(cos x) = -sin x, d/dx(e^x) = e^x, d/dx(ln x) = 1/x.",
                "Geometrically, f'(a) represents the slope of the tangent to the curve y = f(x) at x = a."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_19_01",
                    title = "Quotient Rule Application",
                    problem = "Find derivative of y = (x + 1) / (x - 1).",
                    solution = "u = x+1, v = x-1.\n\\frac{dy}{dx} = \\frac{(1)(x-1) - (x+1)(1)}{(x-1)^2} = \\frac{x-1 - x - 1}{(x-1)^2} = \\frac{-2}{(x-1)^2}.",
                    keyConcept = "Quotient rule (u/v)' = (u'v - uv')/v²"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct basis for Std 12 Differentiation and AOD (16 marks total in CET).",
            jeeMainRelevance = "Foundational for all of Differential Calculus in JEE.",
            cetWeightageMarks = 4
        ),

        // Chapter 20
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH20",
            name = "Integration",
            standard = 11,
            part = 2,
            chapterNumber = 20,
            description = "Antiderivative concept, indefinite integral, algebra of integration, standard elementary integrals, method of substitution.",
            subtopics = listOf(
                "Antiderivative", "Indefinite integral", "Standard integrals",
                "Integration rules", "Algebra of integration", "Substitution", "Basic integration problems"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH20_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_20_01",
                    chapterId = "STD11_P2_CH20",
                    topicId = "Standard Integrals",
                    formulaName = "Elementary Standard Integrals",
                    formula = "\\int x^n dx = \\frac{x^{n+1}}{n+1} + C (n \\ne -1), \\quad \\int \\frac{1}{x} dx = \\ln|x| + C, \\quad \\int e^x dx = e^x + C",
                    description = "Fundamental algebraic and exponential antiderivatives."
                ),
                FormulaItem(
                    formulaId = "FOR_11_20_02",
                    chapterId = "STD11_P2_CH20",
                    topicId = "Trig Integrals",
                    formulaName = "Basic Trigonometric Antiderivatives",
                    formula = "\\int \\sin x dx = -\\cos x + C, \\quad \\int \\cos x dx = \\sin x + C, \\quad \\int \\sec^2 x dx = \\tan x + C",
                    description = "Standard trigonometric antiderivatives."
                )
            ),
            importantConcepts = listOf(
                "Integration is the reverse process of differentiation: d/dx(F(x)) = f(x) implies ∫ f(x) dx = F(x) + C.",
                "Substitution: ∫ f(g(x)) g'(x) dx = ∫ f(t) dt where t = g(x)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_20_01",
                    title = "Basic Substitution",
                    problem = "Evaluate \\int 2x \\cos(x^2) dx.",
                    solution = "Let t = x^2 \\implies dt = 2x dx.\n\\int \\cos(t) dt = \\sin(t) + C = \\sin(x^2) + C.",
                    keyConcept = "Substitution method dt = g'(x)dx"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Foundation for Std 12 Indefinite and Definite Integration (worth 14-16 marks).",
            jeeMainRelevance = "Foundational for Integral Calculus.",
            cetWeightageMarks = 4
        ),

        // Chapter 21
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH21",
            name = "Statistics",
            standard = 11,
            part = 2,
            chapterNumber = 21,
            description = "Measures of dispersion, range, quartile deviation, mean deviation about mean/median, variance, standard deviation, coefficient of variation.",
            subtopics = listOf(
                "Range", "Quartile", "Quartile deviation", "Mean deviation", "Variance",
                "Standard deviation", "Change of origin", "Change of scale", "Combined variance",
                "Combined standard deviation", "Coefficient of variation"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH21_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_21_01",
                    chapterId = "STD11_P2_CH21",
                    topicId = "Variance",
                    formulaName = "Variance and Standard Deviation",
                    formula = "\\sigma^2 = \\frac{1}{n}\\sum (x_i - \\bar{x})^2 = \\frac{1}{n}\\sum x_i^2 - (\\bar{x})^2, \\quad \\sigma = \\sqrt{\\sigma^2}",
                    description = "Measure of spread around arithmetic mean."
                ),
                FormulaItem(
                    formulaId = "FOR_11_21_02",
                    chapterId = "STD11_P2_CH21",
                    topicId = "CV",
                    formulaName = "Coefficient of Variation (C.V.)",
                    formula = "C.V. = \\frac{\\sigma}{\\bar{x}} \\times 100",
                    description = "Relative measure of variability; series with greater C.V. is more variable and less consistent."
                )
            ),
            importantConcepts = listOf(
                "Change of origin (adding/subtracting constant k) DOES NOT change variance or standard deviation.",
                "Change of scale (multiplying by constant c): Var(cx) = c² Var(x), and SD(cx) = |c| SD(x)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_21_01",
                    title = "Effect of Scale on Variance",
                    problem = "If the variance of a dataset is 9 and each observation is multiplied by 3, what is new variance?",
                    solution = "New variance = c² \\times old variance = 3² \\times 9 = 9 \\times 9 = 81.\n(New standard deviation = 3 \\times 3 = 9).",
                    keyConcept = "Var(cx) = c² Var(x)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 2 marks in MHT-CET.",
            jeeMainRelevance = "Guaranteed 1 question in almost every JEE Main session.",
            cetWeightageMarks = 2
        ),

        // Chapter 22
        ComprehensiveChapter(
            chapterId = "STD11_P2_CH22",
            name = "Probability",
            standard = 11,
            part = 2,
            chapterNumber = 22,
            description = "Sample space, algebra of events, addition theorem, conditional probability, multiplication theorem, independent events, Bayes' theorem, odds.",
            subtopics = listOf(
                "Random experiments", "Sample space", "Events", "Algebra of events",
                "Mutually exclusive events", "Exhaustive events", "Axiomatic probability",
                "Addition theorem", "Complementary events", "Conditional probability",
                "Multiplication theorem", "Independent events", "Bayes theorem", "Odds"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P2_CH22_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_22_01",
                    chapterId = "STD11_P2_CH22",
                    topicId = "Conditional",
                    formulaName = "Conditional Probability & Multiplication Rule",
                    formula = "P(A|B) = \\frac{P(A \\cap B)}{P(B)} \\quad (P(B) > 0), \\quad P(A \\cap B) = P(A)P(B) \\text{ (if independent)}",
                    description = "Probability of event A occurring given that event B has already occurred."
                ),
                FormulaItem(
                    formulaId = "FOR_11_22_02",
                    chapterId = "STD11_P2_CH22",
                    topicId = "Bayes Theorem",
                    formulaName = "Bayes' Theorem",
                    formula = "P(E_i|A) = \\frac{P(E_i)P(A|E_i)}{\\sum_{j=1}^n P(E_j)P(A|E_j)}",
                    description = "Reverse conditional probability of cause E_i given effect A."
                )
            ),
            importantConcepts = listOf(
                "Mutually exclusive events cannot occur simultaneously: P(A ∩ B) = 0.",
                "Independent events do not affect each other: P(A|B) = P(A)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_22_01",
                    title = "Independent Events Probability",
                    problem = "If P(A) = 0.4 and P(B) = 0.5 and events are independent, find P(A ∪ B).",
                    solution = "Since independent, P(A ∩ B) = P(A) P(B) = 0.4 \\times 0.5 = 0.20.\nP(A ∪ B) = P(A) + P(B) - P(A ∩ B) = 0.4 + 0.5 - 0.2 = 0.70.",
                    keyConcept = "P(A ∪ B) = P(A) + P(B) - P(A)P(B)"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 4 marks in Std 11 CET; prerequisite for Std 12 Probability Distribution.",
            jeeMainRelevance = "High-weightage topic in JEE Main.",
            cetWeightageMarks = 4
        )
    )
}
