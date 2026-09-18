package com.example.data.model

object Std12Part2Catalog {
    val chapters: List<ComprehensiveChapter> = listOf(
        // Chapter 12
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH12",
            name = "Continuity",
            standard = 12,
            part = 2,
            chapterNumber = 12,
            description = "Continuity at a point, in an open/closed interval, removable and jump discontinuities, continuity of composite and trigonometric functions.",
            subtopics = listOf(
                "Continuity at a point", "Left-hand continuity", "Right-hand continuity",
                "Continuity on an interval", "Types of discontinuity", "Removable discontinuity",
                "Jump discontinuity", "Algebra of continuous functions", "Continuity of composite functions"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH12_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_12_01",
                    chapterId = "STD12_P2_CH12",
                    topicId = "Continuity Condition",
                    formulaName = "Definition of Continuity at x = a",
                    formula = "\\lim_{x \\to a^-} f(x) = \\lim_{x \\to a^+} f(x) = f(a)",
                    description = "Left-hand limit equals right-hand limit and both equal the exact function value at x = a."
                )
            ),
            importantConcepts = listOf(
                "Polynomial, rational, trigonometric, exponential, and logarithmic functions are continuous everywhere in their domains.",
                "If f and g are continuous at x = a, then f ± g, f · g, and f / g (g(a) ≠ 0) are also continuous at a."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_12_01",
                    title = "Find k for Continuity",
                    problem = "If f(x) = (sin 3x)/x for x ≠ 0 and f(0) = k is continuous at x = 0, find k.",
                    solution = "\\lim_{x \\to 0} f(x) = \\lim_{x \\to 0}\\frac{\\sin 3x}{3x} \\times 3 = 1 \\times 3 = 3.\nFor continuity, f(0) = \\lim_{x \\to 0} f(x) \\implies k = 3.",
                    keyConcept = "f(a) = lim x->a f(x)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 2-4 marks in MHT-CET.",
            jeeMainRelevance = "Regularly combined with differentiability in JEE Main.",
            cetWeightageMarks = 4
        ),

        // Chapter 13
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH13",
            name = "Differentiation (Std 12)",
            standard = 12,
            part = 2,
            chapterNumber = 13,
            description = "Chain rule, derivatives of inverse trigonometric functions, logarithmic differentiation, implicit functions, parametric functions, second-order derivatives.",
            subtopics = listOf(
                "Derivative of composite functions", "Chain rule", "Inverse trigonometric derivatives",
                "Logarithmic differentiation", "Implicit functions", "Parametric functions",
                "Second-order derivative", "Higher-order derivatives"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH13_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_13_01",
                    chapterId = "STD12_P2_CH13",
                    topicId = "Chain Rule",
                    formulaName = "Chain Rule & Inverse Trig Derivatives",
                    formula = "\\frac{dy}{dx} = \\frac{dy}{du} \\cdot \\frac{du}{dx}, \\quad \\frac{d}{dx}(\\sin^{-1} x) = \\frac{1}{\\sqrt{1 - x^2}}, \\quad \\frac{d}{dx}(\\tan^{-1} x) = \\frac{1}{1 + x^2}",
                    description = "Composite differentiation and inverse circular function derivatives."
                ),
                FormulaItem(
                    formulaId = "FOR_12_13_02",
                    chapterId = "STD12_P2_CH13",
                    topicId = "Parametric Derivative",
                    formulaName = "Parametric First & Second Derivative",
                    formula = "\\frac{dy}{dx} = \\frac{dy/dt}{dx/dt}, \\quad \\frac{d^2y}{dx^2} = \\frac{d}{dt}\\left(\\frac{dy}{dx}\\right) \\cdot \\frac{1}{dx/dt}",
                    description = "Derivatives when x and y are given in terms of parameter t."
                )
            ),
            importantConcepts = listOf(
                "Logarithmic differentiation is indispensable for f(x)^g(x) and products of multiple functions.",
                "d/dx(a^x) = a^x ln a; d/dx(log_a x) = 1 / (x ln a)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_13_01",
                    title = "Logarithmic Differentiation of x^x",
                    problem = "Find dy/dx if y = x^x.",
                    solution = "Take ln on both sides: \\ln y = x \\ln x.\nDifferentiate: \\frac{1}{y}\\frac{dy}{dx} = 1 \\cdot \\ln x + x \\cdot \\frac{1}{x} = \\ln x + 1.\n\\frac{dy}{dx} = y(1 + \\ln x) = x^x(1 + \\ln x).",
                    keyConcept = "d/dx(x^x) = x^x(1 + ln x)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 8 marks (4 questions) in MHT-CET.",
            jeeMainRelevance = "Core fundamental across calculus.",
            cetWeightageMarks = 8
        ),

        // Chapter 14
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH14",
            name = "Applications of Derivatives",
            standard = 12,
            part = 2,
            chapterNumber = 14,
            description = "Rate of change, tangents and normals, approximations, Rolle's and LMVT, increasing and decreasing functions, maxima and minima.",
            subtopics = listOf(
                "Rate of change", "Velocity", "Acceleration", "Tangents and normals",
                "Approximations", "Rolle's theorem", "Lagrange's Mean Value Theorem",
                "Increasing functions", "Decreasing functions", "Maxima and minima", "Concavity"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH14_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_14_01",
                    chapterId = "STD12_P2_CH14",
                    topicId = "Tangents & Normals",
                    formulaName = "Slope of Tangent and Normal",
                    formula = "m_T = \\left.\\frac{dy}{dx}\\right|_{(x_1, y_1)}, \\quad m_N = -\\frac{1}{m_T}, \\quad y - y_1 = m_T(x - x_1)",
                    description = "Tangent and perpendicular normal to curve at given point."
                ),
                FormulaItem(
                    formulaId = "FOR_12_14_02",
                    chapterId = "STD12_P2_CH14",
                    topicId = "Mean Value Theorems",
                    formulaName = "Lagrange's Mean Value Theorem (LMVT)",
                    formula = "f'(c) = \\frac{f(b) - f(a)}{b - a} \\quad \\text{for some } c \\in (a, b)",
                    description = "Average rate of change equals instantaneous rate of change for differentiable functions."
                )
            ),
            importantConcepts = listOf(
                "Increasing: f'(x) > 0; Decreasing: f'(x) < 0.",
                "First derivative test: if f'(x) changes from + to -, local maximum; if - to +, local minimum.",
                "Second derivative test: f'(c) = 0 and f''(c) < 0 => local maximum; f''(c) > 0 => local minimum."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_14_01",
                    title = "Slope of Tangent",
                    problem = "Find slope of tangent to y = x³ - x at x = 2.",
                    solution = "dy/dx = 3x² - 1. At x = 2: m_T = 3(2)² - 1 = 12 - 1 = 11.",
                    keyConcept = "m_T = dy/dx evaluated at point"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 8 marks (4 questions) in MHT-CET.",
            jeeMainRelevance = "Extremely high yield in JEE Main (2-3 questions per paper).",
            cetWeightageMarks = 8
        ),

        // Chapter 15
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH15",
            name = "Indefinite Integration",
            standard = 12,
            part = 2,
            chapterNumber = 15,
            description = "Standard integrals, integration by substitution, integration by parts (ILATE), partial fractions, special algebraic and trigonometric integrals.",
            subtopics = listOf(
                "Standard integrals", "Substitution", "Integration by parts", "ILATE rule",
                "Partial fractions", "Special integrals", "Rational functions", "Trigonometric integrals"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH15_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_15_01",
                    chapterId = "STD12_P2_CH15",
                    topicId = "Integration by Parts",
                    formulaName = "Integration by Parts (ILATE Rule)",
                    formula = "\\int u v dx = u \\int v dx - \\int \\left(u' \\int v dx\\right) dx",
                    description = "Order of priority: Inverse, Logarithmic, Algebraic, Trigonometric, Exponential."
                ),
                FormulaItem(
                    formulaId = "FOR_12_15_02",
                    chapterId = "STD12_P2_CH15",
                    topicId = "Special Exponential Form",
                    formulaName = "e^x [f(x) + f'(x)] Integral",
                    formula = "\\int e^x [f(x) + f'(x)] dx = e^x f(x) + C",
                    description = "One of the most frequently tested shortcut formulas in CET & JEE."
                )
            ),
            importantConcepts = listOf(
                "Special forms: 1/(x² + a²) gives (1/a) tan⁻¹(x/a) + C.",
                "1/√(a² - x²) gives sin⁻¹(x/a) + C.",
                "1/√(x² ± a²) gives ln|x + √(x² ± a²)| + C."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_15_01",
                    title = "e^x Shortcut",
                    problem = "Evaluate \\int e^x (\\tan x + \\sec^2 x) dx.",
                    solution = "Let f(x) = \\tan x \\implies f'(x) = \\sec^2 x.\nUsing \\int e^x [f(x) + f'(x)] dx = e^x f(x) + C:\nAnswer = e^x \\tan x + C.",
                    keyConcept = "∫ e^x(f + f') dx = e^x f(x) + C"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 8 marks (4 questions) in MHT-CET.",
            jeeMainRelevance = "Heavyweight topic in JEE Main calculus.",
            cetWeightageMarks = 8
        ),

        // Chapter 16
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH16",
            name = "Definite Integration",
            standard = 12,
            part = 2,
            chapterNumber = 16,
            description = "Fundamental theorem of calculus, properties of definite integrals, King's rule, symmetric intervals (even/odd), periodic integrals.",
            subtopics = listOf(
                "Definite integral as limit of sum", "Fundamental theorem", "Properties of definite integrals",
                "King's property", "Even and odd functions", "Periodic properties", "Reduction formulas"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH16_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_16_01",
                    chapterId = "STD12_P2_CH16",
                    topicId = "King's Rule",
                    formulaName = "King's Property of Definite Integrals",
                    formula = "\\int_a^b f(x) dx = \\int_a^b f(a + b - x) dx, \\quad \\int_0^a f(x) dx = \\int_0^a f(a - x) dx",
                    description = "Fundamental symmetry property solving 80% of definite integral questions."
                ),
                FormulaItem(
                    formulaId = "FOR_12_16_02",
                    chapterId = "STD12_P2_CH16",
                    topicId = "Even-Odd Integral",
                    formulaName = "Symmetric Interval Integral [-a, a]",
                    formula = "\\int_{-a}^a f(x) dx = \\begin{cases} 2\\int_0^a f(x) dx & \\text{if } f(-x) = f(x) \\\\ 0 & \\text{if } f(-x) = -f(x) \\end{cases}",
                    description = "Vanishes completely for odd integrand."
                )
            ),
            importantConcepts = listOf(
                "Leibniz Rule for differentiation under the integral sign.",
                "If f(a + b - x) = f(x), then ∫_a^b x f(x) dx = ((a + b)/2) ∫_a^b f(x) dx."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_16_01",
                    title = "King's Rule Evaluation",
                    problem = "Evaluate I = \\int_0^{\\pi/2} \\frac{\\sin x}{\\sin x + \\cos x} dx.",
                    solution = "Apply King's property: I = \\int_0^{\\pi/2} \\frac{\\cos x}{\\cos x + \\sin x} dx.\nAdd both equations: 2I = \\int_0^{\\pi/2} 1 dx = \\frac{\\pi}{2} \\implies I = \\frac{\\pi}{4}.",
                    keyConcept = "King's rule 2I addition technique"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET.",
            jeeMainRelevance = "Guaranteed 1-2 questions in every JEE session.",
            cetWeightageMarks = 6
        ),

        // Chapter 17
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH17",
            name = "Application of Definite Integration",
            standard = 12,
            part = 2,
            chapterNumber = 17,
            description = "Area bounded by standard curves, area between line and parabola, area between two parabolas or circle and line.",
            subtopics = listOf(
                "Area under curve", "Area between two curves", "Area of circle",
                "Area of ellipse", "Area bounded by parabola and line", "Area bounded by two parabolas"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH17_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_17_01",
                    chapterId = "STD12_P2_CH17",
                    topicId = "Area Between Curves",
                    formulaName = "Area Between Two Curves",
                    formula = "A = \\int_a^b |f(x) - g(x)| dx = \\int_a^b (y_{\\text{upper}} - y_{\\text{lower}}) dx",
                    description = "Area between two curves intersecting at x = a and x = b."
                ),
                FormulaItem(
                    formulaId = "FOR_12_17_02",
                    chapterId = "STD12_P2_CH17",
                    topicId = "Shortcut Parabola Area",
                    formulaName = "Area Between Parabola y² = 4ax and Line y = mx",
                    formula = "A = \\frac{8 a^2}{3 m^3}",
                    description = "Direct shortcut formula for area between parabola and secant line through origin."
                )
            ),
            importantConcepts = listOf(
                "Area between two parabolas y² = 4ax and x² = 4by is A = 16ab / 3.",
                "Area of full ellipse x²/a² + y²/b² = 1 is πab."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_17_01",
                    title = "Parabola and Line Area",
                    problem = "Find area bounded by y² = 4x and y = x.",
                    solution = "4a = 4 \\implies a = 1. Line slope m = 1.\nShortcut: A = \\frac{8a^2}{3m^3} = \\frac{8(1)^2}{3(1)^3} = \\frac{8}{3} \\text{ sq units}.",
                    keyConcept = "A = 8a² / (3m³)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Guaranteed 4 marks (2 questions) in MHT-CET.",
            jeeMainRelevance = "Regularly tested in JEE Main.",
            cetWeightageMarks = 4
        ),

        // Chapter 18
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH18",
            name = "Differential Equations",
            standard = 12,
            part = 2,
            chapterNumber = 18,
            description = "Order and degree, formation of differential equations, variable separable, homogeneous, linear first-order differential equations (integrating factor), applications to growth and decay.",
            subtopics = listOf(
                "Order", "Degree", "Formation of differential equations", "Variable separable",
                "Substitutions", "Homogeneous differential equations", "First-order linear differential equations",
                "Integrating factor", "Applications to growth and decay", "Newton's law of cooling"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH18_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_18_01",
                    chapterId = "STD12_P2_CH18",
                    topicId = "Linear DE",
                    formulaName = "Linear Differential Equation dy/dx + Py = Q",
                    formula = "IF = e^{\\int P dx}, \\quad y \\cdot IF = \\int (Q \\cdot IF) dx + C",
                    description = "General solution of first-order linear differential equation."
                ),
                FormulaItem(
                    formulaId = "FOR_12_18_02",
                    chapterId = "STD12_P2_CH18",
                    topicId = "Homogeneous DE",
                    formulaName = "Homogeneous Substitution y = vx",
                    formula = "\\frac{dy}{dx} = v + x\\frac{dv}{dx}",
                    description = "Transforms homogeneous DE dy/dx = f(y/x) into variable separable form."
                )
            ),
            importantConcepts = listOf(
                "Order is the highest derivative present. Degree is the power of the highest derivative after clearing radicals/fractions.",
                "Degree is NOT defined if the equation contains transcendental terms like sin(dy/dx), e^(dy/dx), etc."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_18_01",
                    title = "Linear DE Solution",
                    problem = "Solve dy/dx + y/x = x².",
                    solution = "P = 1/x, Q = x².\nIF = e^{\\int (1/x) dx} = e^{\\ln x} = x.\ny \\cdot x = \\int (x^2 \\cdot x) dx = \\int x^3 dx = \\frac{x^4}{4} + C \\implies y = \\frac{x^3}{4} + \\frac{C}{x}.",
                    keyConcept = "Integrating Factor IF = e^(∫P dx)"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 8 marks (4 questions) in MHT-CET.",
            jeeMainRelevance = "High-weightage topic in JEE Main.",
            cetWeightageMarks = 8
        ),

        // Chapter 19
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH19",
            name = "Probability Distribution",
            standard = 12,
            part = 2,
            chapterNumber = 19,
            description = "Discrete and continuous random variables, probability mass function (PMF), cumulative distribution function (CDF), expected value E(X), variance Var(X), standard deviation.",
            subtopics = listOf(
                "Random variable", "Discrete random variable", "Continuous random variable",
                "Probability mass function", "Cumulative distribution function",
                "Expected value", "Mean", "Variance", "Standard deviation"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH19_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_19_01",
                    chapterId = "STD12_P2_CH19",
                    topicId = "Expected Value",
                    formulaName = "Expectation and Variance of Discrete Random Variable",
                    formula = "E(X) = \\sum x_i P(x_i), \\quad \\operatorname{Var}(X) = E(X^2) - [E(X)]^2 = \\sum x_i^2 P(x_i) - \\mu^2",
                    description = "First and second central moments."
                ),
                FormulaItem(
                    formulaId = "FOR_12_19_02",
                    chapterId = "STD12_P2_CH19",
                    topicId = "PMF Condition",
                    formulaName = "Conditions for Valid PMF",
                    formula = "P(x_i) \\ge 0 \\quad \\forall i, \\quad \\sum_{i} P(x_i) = 1",
                    description = "Non-negativity and total probability axiom."
                )
            ),
            importantConcepts = listOf(
                "E(aX + b) = a E(X) + b.",
                "Var(aX + b) = a² Var(X) (variance is invariant under translation of origin)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_19_01",
                    title = "Find Expected Value",
                    problem = "X takes values 1, 2, 3 with probabilities 0.2, 0.5, 0.3. Find E(X).",
                    solution = "E(X) = 1(0.2) + 2(0.5) + 3(0.3) = 0.2 + 1.0 + 0.9 = 2.1.",
                    keyConcept = "E(X) = Σ x · P(x)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET.",
            jeeMainRelevance = "Tested in JEE Main probability sections.",
            cetWeightageMarks = 6
        ),

        // Chapter 20
        ComprehensiveChapter(
            chapterId = "STD12_P2_CH20",
            name = "Binomial Distribution",
            standard = 12,
            part = 2,
            chapterNumber = 20,
            description = "Bernoulli trials, definition of binomial distribution X ~ B(n, p), probability function P(X = r) = nCr p^r q^(n-r), mean and variance.",
            subtopics = listOf(
                "Bernoulli trials", "Binomial distribution", "Parameters n and p",
                "Probability function", "Mean of binomial distribution", "Variance of binomial distribution"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P2_CH20_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_20_01",
                    chapterId = "STD12_P2_CH20",
                    topicId = "Binomial Probability",
                    formulaName = "Binomial Probability Mass Function",
                    formula = "P(X = r) = {^nC_r}\\, p^r q^{n-r}, \\quad q = 1 - p, \\quad r = 0, 1, 2, \\dots, n",
                    description = "Probability of r successes in n independent Bernoulli trials."
                ),
                FormulaItem(
                    formulaId = "FOR_12_20_02",
                    chapterId = "STD12_P2_CH20",
                    topicId = "Mean & Variance",
                    formulaName = "Mean and Variance of Binomial Distribution",
                    formula = "\\text{Mean } \\mu = np, \\quad \\text{Variance } \\sigma^2 = npq, \\quad \\text{Var}(X) < E(X)",
                    description = "Parameters and properties for X ~ B(n, p)."
                )
            ),
            importantConcepts = listOf(
                "For binomial distribution, variance is always strictly less than the mean because q < 1.",
                "Trials must be finite, independent, and with constant success probability p."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_20_01",
                    title = "Fair Coin Tosses",
                    problem = "A fair coin is tossed 6 times. Find probability of getting exactly 4 heads.",
                    solution = "n = 6, p = 1/2, q = 1/2, r = 4.\nP(X = 4) = ^6C_4 (1/2)^4 (1/2)^2 = 15 \\times (1/2)^6 = 15 / 64.",
                    keyConcept = "P(X = r) = ^nC_r p^r q^(n-r)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 4 marks in MHT-CET.",
            jeeMainRelevance = "Tested regularly in JEE Main.",
            cetWeightageMarks = 4
        )
    )
}
