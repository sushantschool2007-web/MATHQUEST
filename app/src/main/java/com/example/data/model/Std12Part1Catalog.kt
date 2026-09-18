package com.example.data.model

object Std12Part1Catalog {
    val chapters: List<ComprehensiveChapter> = listOf(
        // Chapter 1
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH01",
            name = "Mathematical Logic",
            standard = 12,
            part = 1,
            chapterNumber = 1,
            description = "Statements, truth values, logical connectives, truth tables, tautology, contradiction, converse/inverse/contrapositive, switching circuits.",
            subtopics = listOf(
                "Statements", "Truth values", "Open sentences", "Compound statements", "Conjunction",
                "Disjunction", "Negation", "Implication", "Biconditional", "Truth tables",
                "Logical equivalence", "Tautology", "Contradiction", "Contingency", "Converse",
                "Inverse", "Contrapositive", "De Morgan's laws", "Algebra of statements",
                "Quantifiers", "Switching circuits"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH01_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_1_01",
                    chapterId = "STD12_P1_CH01",
                    topicId = "Equivalence",
                    formulaName = "Conditional and De Morgan Laws",
                    formula = "p \\to q \\equiv \\sim p \\lor q, \\quad \\sim(p \\land q) \\equiv \\sim p \\lor \\sim q, \\quad \\sim(p \\lor q) \\equiv \\sim p \\land \\sim q",
                    description = "Fundamental propositional equivalences and negations."
                ),
                FormulaItem(
                    formulaId = "FOR_12_1_02",
                    chapterId = "STD12_P1_CH01",
                    topicId = "Contrapositive",
                    formulaName = "Converse, Inverse, Contrapositive",
                    formula = "\\text{Converse: } q \\to p, \\quad \\text{Inverse: } \\sim p \\to \\sim q, \\quad \\text{Contrapositive: } \\sim q \\to \\sim p \\equiv p \\to q",
                    description = "Variants of conditional statement p -> q."
                )
            ),
            importantConcepts = listOf(
                "A conditional statement p -> q is logically equivalent to its contrapositive ~q -> ~p.",
                "Tautology: Statement pattern whose truth value is always T in all cases.",
                "In switching circuits: Switches in series correspond to Conjunction (p ∧ q); in parallel to Disjunction (p ∨ q)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_1_01",
                    title = "Negation of Implication",
                    problem = "Find the negation of 'If a triangle is equilateral, then it is equiangular'.",
                    solution = "Let p = triangle is equilateral, q = triangle is equiangular.\nStatement is p \\to q. Negation is \\sim(p \\to q) \\equiv p \\land \\sim q.\nAnswer: 'A triangle is equilateral and it is not equiangular'.",
                    keyConcept = "~(p -> q) ≡ p ∧ ~q"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "Guaranteed 4-6 marks (2-3 questions) in every MHT-CET paper.",
            jeeMainRelevance = "Guaranteed 4 marks in JEE Main with direct truth table or circuit questions.",
            cetWeightageMarks = 6
        ),

        // Chapter 2
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH02",
            name = "Matrices (Std 12)",
            standard = 12,
            part = 1,
            chapterNumber = 2,
            description = "Elementary row and column transformations, minors, cofactors, adjoint of matrix, inverse by transformation and adjoint method, solving linear equations.",
            subtopics = listOf(
                "Elementary transformations", "Row transformations", "Column transformations",
                "Minors", "Cofactors", "Adjoint", "Inverse", "Singular/non-singular matrices",
                "Inverse by elementary transformation", "Inverse using adjoint",
                "Solving simultaneous equations", "Matrix method", "Inverse method"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH02_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_2_01",
                    chapterId = "STD12_P1_CH02",
                    topicId = "Adjoint Inverse",
                    formulaName = "Inverse of Matrix via Adjoint",
                    formula = "A^{-1} = \\frac{1}{|A|}\\operatorname{adj}(A) \\quad (|A| \\ne 0)",
                    description = "Standard formula for inverse of non-singular square matrix."
                ),
                FormulaItem(
                    formulaId = "FOR_12_2_02",
                    chapterId = "STD12_P1_CH02",
                    topicId = "2x2 Inverse Shortcut",
                    formulaName = "Quick 2x2 Matrix Inverse",
                    formula = "A = \\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} \\implies A^{-1} = \\frac{1}{ad - bc}\\begin{pmatrix} d & -b \\\\ -c & a \\end{pmatrix}",
                    description = "Swap diagonal elements, negate non-diagonal elements."
                )
            ),
            importantConcepts = listOf(
                "A · adj(A) = adj(A) · A = |A| I.",
                "|adj(A)| = |A|^(n - 1) for an n × n matrix.",
                "A square matrix is invertible iff |A| ≠ 0 (non-singular)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_2_01",
                    title = "Inverse of 2x2 Matrix",
                    problem = "Find the inverse of matrix A = [[2, 5], [1, 3]].",
                    solution = "|A| = (2)(3) - (5)(1) = 6 - 5 = 1.\nUsing shortcut swap and negate:\nA^{-1} = 1/1 * [[3, -5], [-1, 2]] = [[3, -5], [-1, 2]].",
                    keyConcept = "A⁻¹ = adj(A)/|A|"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET (elementary row operations & inversion).",
            jeeMainRelevance = "Heavyweight topic in JEE Main testing adjoint determinant properties.",
            cetWeightageMarks = 6
        ),

        // Chapter 3
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH03",
            name = "Trigonometric Functions (Std 12)",
            standard = 12,
            part = 1,
            chapterNumber = 3,
            description = "General solutions of trigonometric equations, inverse trigonometric functions, principal values, sine rule, cosine rule, projection rule, triangle applications.",
            subtopics = listOf(
                "General solutions", "Trigonometric equations", "sin θ = sin α", "cos θ = cos α",
                "tan θ = tan α", "Multiple-angle equations", "a cos θ + b sin θ = c",
                "Inverse trigonometric functions", "Principal values", "Domain", "Range",
                "Graphs", "Polar coordinates", "Sine rule", "Cosine rule", "Projection rule",
                "Area of triangle", "Heron's formula", "Napier's analogies"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH03_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_3_01",
                    chapterId = "STD12_P1_CH03",
                    topicId = "General Solutions",
                    formulaName = "General Solution Formulae",
                    formula = "\\sin\\theta = \\sin\\alpha \\implies \\theta = n\\pi + (-1)^n\\alpha, \\quad \\cos\\theta = \\cos\\alpha \\implies \\theta = 2n\\pi \\pm \\alpha, \\quad \\tan\\theta = \\tan\\alpha \\implies \\theta = n\\pi + \\alpha",
                    description = "General solutions with integer n."
                ),
                FormulaItem(
                    formulaId = "FOR_12_3_02",
                    chapterId = "STD12_P1_CH03",
                    topicId = "Sine & Cosine Rules",
                    formulaName = "Sine and Cosine Rules",
                    formula = "\\frac{a}{\\sin A} = \\frac{b}{\\sin B} = \\frac{c}{\\sin C} = 2R, \\quad \\cos A = \\frac{b^2 + c^2 - a^2}{2bc}",
                    description = "Triangular properties linking sides and angles to circumradius R."
                ),
                FormulaItem(
                    formulaId = "FOR_12_3_03",
                    chapterId = "STD12_P1_CH03",
                    topicId = "Inverse Trig",
                    formulaName = "Inverse Trigonometric Addition",
                    formula = "\\tan^{-1} x + \\tan^{-1} y = \\tan^{-1}\\left(\\frac{x + y}{1 - xy}\\right) \\quad (xy < 1)",
                    description = "Composition of inverse tangents."
                )
            ),
            importantConcepts = listOf(
                "Principal branch of sin⁻¹x is [-π/2, π/2], cos⁻¹x is [0, π], tan⁻¹x is (-π/2, π/2).",
                "Projection Rule: a = b cos C + c cos B; b = c cos A + a cos C; c = a cos B + b cos A."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_3_01",
                    title = "Inverse Trig Simplification",
                    problem = "Evaluate \\tan^{-1}(1/2) + \\tan^{-1}(1/3).",
                    solution = "xy = (1/2)(1/3) = 1/6 < 1.\n\\tan^{-1}\\left(\\frac{1/2 + 1/3}{1 - 1/6}\\right) = \\tan^{-1}\\left(\\frac{5/6}{5/6}\\right) = \\tan^{-1}(1) = \\frac{\\pi}{4}.",
                    keyConcept = "tan⁻¹x + tan⁻¹y formula"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 6-8 marks in MHT-CET.",
            jeeMainRelevance = "Tested regularly in JEE Main.",
            cetWeightageMarks = 8
        ),

        // Chapter 4
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH04",
            name = "Pair of Straight Lines",
            standard = 12,
            part = 1,
            chapterNumber = 4,
            description = "Homogeneous second-degree equation ax² + 2hxy + by² = 0, acute angle between lines, parallel/perpendicular conditions, general second-degree equation.",
            subtopics = listOf(
                "Pair of lines through origin", "Homogeneous second-degree equation", "Combined equation",
                "Angle between lines", "Parallel condition", "Perpendicular condition",
                "Pair of lines not through origin", "Point of intersection", "General second-degree equation"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH04_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_4_01",
                    chapterId = "STD12_P1_CH04",
                    topicId = "Homogeneous Equation",
                    formulaName = "Angle and Conditions for ax² + 2hxy + by² = 0",
                    formula = "\\tan\\theta = \\left|\\frac{2\\sqrt{h^2 - ab}}{a + b}\\right|, \\quad \\text{Perpendicular: } a + b = 0, \\quad \\text{Coincident: } h^2 - ab = 0",
                    description = "Acute angle between lines represented by homogeneous equation."
                ),
                FormulaItem(
                    formulaId = "FOR_12_4_02",
                    chapterId = "STD12_P1_CH04",
                    topicId = "General 2nd Degree",
                    formulaName = "Condition for Pair of Lines",
                    formula = "abc + 2fgh - af^2 - bg^2 - ch^2 = 0 \\iff \\det\\begin{pmatrix} a & h & g \\\\ h & b & f \\\\ g & f & c \\end{pmatrix} = 0",
                    description = "Discriminant condition for ax² + 2hxy + by² + 2gx + 2fy + c = 0 to represent two straight lines."
                )
            ),
            importantConcepts = listOf(
                "Sum of slopes: m₁ + m₂ = -2h/b; Product of slopes: m₁ · m₂ = a/b.",
                "Joint equation of angle bisectors: (x² - y²) / (a - b) = xy / h."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_4_01",
                    title = "Perpendicular Pair of Lines",
                    problem = "Find k if 3x² + 8xy + ky² = 0 represents two perpendicular lines.",
                    solution = "Condition for perpendicularity is a + b = 0.\nHere a = 3, b = k. So 3 + k = 0 \\implies k = -3.",
                    keyConcept = "a + b = 0 for orthogonal pair"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 4-6 marks (2-3 questions) in MHT-CET.",
            jeeMainRelevance = "Appears in JEE Coordinate Geometry combinations.",
            cetWeightageMarks = 6
        ),

        // Chapter 5
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH05",
            name = "Circle (Std 12)",
            standard = 12,
            part = 1,
            chapterNumber = 5,
            description = "Standard & general equations, tangent at a point, condition of tangency c² = a²(1+m²), length of tangent, director circle.",
            subtopics = listOf(
                "Standard equation", "General equation", "Tangent", "Tangent at a point",
                "Tangent equation", "Condition of tangency", "Length of tangent",
                "Tangents from external point", "Normal", "Director circle"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH05_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_5_01",
                    chapterId = "STD12_P1_CH05",
                    topicId = "Tangency Condition",
                    formulaName = "Condition of Tangency to x² + y² = a²",
                    formula = "y = mx \\pm a\\sqrt{1 + m^2} \\quad \\implies c^2 = a^2(1 + m^2)",
                    description = "Line y = mx + c touches the circle x² + y² = a²."
                ),
                FormulaItem(
                    formulaId = "FOR_12_5_02",
                    chapterId = "STD12_P1_CH05",
                    topicId = "Tangent at Point",
                    formulaName = "Equation of Tangent at (x₁, y₁)",
                    formula = "xx_1 + yy_1 = a^2 \\quad \\text{(Standard)}, \\quad xx_1 + yy_1 + g(x + x_1) + f(y + y_1) + c = 0 \\quad \\text{(General)}",
                    description = "Tangent equation using Cartesian coordinate replacement T = 0."
                )
            ),
            importantConcepts = listOf(
                "Length of tangent from external point (x₁, y₁): L = √S₁ = √(x₁² + y₁² + 2gx₁ + 2fy₁ + c).",
                "Director circle of x² + y² = a² is x² + y² = 2a² (locus of perpendicular tangents)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_5_01",
                    title = "Length of Tangent",
                    problem = "Find length of tangent from (5, 4) to x² + y² = 9.",
                    solution = "L = \\sqrt{S_1} = \\sqrt{5^2 + 4^2 - 9} = \\sqrt{25 + 16 - 9} = \\sqrt{32} = 4\\sqrt{2} \\text{ units}.",
                    keyConcept = "L = sqrt(S₁)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 4 marks in MHT-CET.",
            jeeMainRelevance = "Heavily tested in JEE Main with chord of contact and director circle.",
            cetWeightageMarks = 4
        ),

        // Chapter 6
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH06",
            name = "Conics (Std 12)",
            standard = 12,
            part = 1,
            chapterNumber = 6,
            description = "Advanced conic sections: parabola, ellipse, hyperbola with tangents, normals, eccentricities, asymptotes, and conditions of tangency.",
            subtopics = listOf(
                "Parabola", "Standard parabola", "Focus", "Directrix", "Vertex", "Axis",
                "Latus rectum", "Tangent", "Normal", "Ellipse", "Focus", "Eccentricity",
                "Major/minor axis", "Tangent", "Normal", "Hyperbola", "Focus", "Directrix",
                "Eccentricity", "Asymptotes", "Tangent", "Normal"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH06_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_6_01",
                    chapterId = "STD12_P1_CH06",
                    topicId = "Tangency Parabola",
                    formulaName = "Tangency to Parabola y² = 4ax",
                    formula = "y = mx + \\frac{a}{m} \\quad (m \\ne 0), \\quad \\text{Point of contact} = \\left(\\frac{a}{m^2}, \\frac{2a}{m}\\right)",
                    description = "Condition of tangency c = a/m for parabola."
                ),
                FormulaItem(
                    formulaId = "FOR_12_6_02",
                    chapterId = "STD12_P1_CH06",
                    topicId = "Tangency Ellipse & Hyperbola",
                    formulaName = "Conditions of Tangency for Central Conics",
                    formula = "c^2 = a^2 m^2 + b^2 \\quad \\text{(Ellipse)}, \\quad c^2 = a^2 m^2 - b^2 \\quad \\text{(Hyperbola)}",
                    description = "Tangent lines y = mx ± c to central conics."
                )
            ),
            importantConcepts = listOf(
                "Rectangular hyperbola: xy = c² or x² - y² = a² has eccentricity e = √2.",
                "For ellipse: b² = a²(1 - e²); For hyperbola: b² = a²(e² - 1)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_6_01",
                    title = "Parabola Tangent Condition",
                    problem = "Find k such that y = 2x + k is tangent to y² = 8x.",
                    solution = "4a = 8 \\implies a = 2. Slope m = 2.\nCondition for parabola tangency is c = a / m \\implies k = 2 / 2 = 1.",
                    keyConcept = "c = a/m for y² = 4ax"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 4-6 marks in MHT-CET.",
            jeeMainRelevance = "High-yield chapter in JEE Coordinate Geometry.",
            cetWeightageMarks = 6
        ),

        // Chapter 7
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH07",
            name = "Vectors (Std 12)",
            standard = 12,
            part = 1,
            chapterNumber = 7,
            description = "Collinearity and coplanarity, internal/external section formula, centroid, scalar triple product, geometrical volume interpretation.",
            subtopics = listOf(
                "Linear combination", "Collinearity", "Coplanarity", "Section formula",
                "Internal division", "External division", "Midpoint", "Centroid",
                "Scalar triple product", "Geometrical interpretation", "Applications"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH07_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_7_01",
                    chapterId = "STD12_P1_CH07",
                    topicId = "Section Formula",
                    formulaName = "Vector Section Formula & Centroid",
                    formula = "\\vec{r} = \\frac{m\\vec{b} + n\\vec{a}}{m + n} \\quad (\\text{Internal}), \\quad \\vec{g} = \\frac{\\vec{a} + \\vec{b} + \\vec{c}}{3} \\quad (\\text{Centroid})",
                    description = "Position vector of point dividing segment in ratio m:n."
                ),
                FormulaItem(
                    formulaId = "FOR_12_7_02",
                    chapterId = "STD12_P1_CH07",
                    topicId = "Box Product",
                    formulaName = "Scalar Triple Product [a b c]",
                    formula = "[\\vec{a}\\,\\vec{b}\\,\\vec{c}] = \\vec{a} \\cdot (\\vec{b} \\times \\vec{c}) = \\det\\begin{pmatrix} a_1 & a_2 & a_3 \\\\ b_1 & b_2 & b_3 \\\\ c_1 & c_2 & c_3 \\end{pmatrix}",
                    description = "Volume of parallelepiped with coterminous edges a, b, c. Zero volume indicates coplanarity."
                )
            ),
            importantConcepts = listOf(
                "Three vectors are coplanar iff their scalar triple product [a b c] = 0.",
                "Volume of tetrahedron with coterminous edges a, b, c is 1/6 |[a b c]|."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_7_01",
                    title = "Coplanarity Verification",
                    problem = "Show that i - 2j + 3k, -2i + 3j - 4k, and i - 3j + 5k are coplanar.",
                    solution = "[a b c] = det |1 -2 3; -2 3 -4; 1 -3 5| = 1(15-12) - (-2)(-10+4) + 3(6-3) = 3 + 2(-6) + 3(3) = 3 - 12 + 9 = 0.\nSince [a b c] = 0, vectors are coplanar.",
                    keyConcept = "[a b c] = 0 for coplanar vectors"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 6 marks (3 questions) in MHT-CET.",
            jeeMainRelevance = "Extremely high weightage in JEE Main.",
            cetWeightageMarks = 6
        ),

        // Chapter 8
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH08",
            name = "Three-Dimensional Geometry",
            standard = 12,
            part = 1,
            chapterNumber = 8,
            description = "Direction angles, direction cosines (l, m, n), direction ratios (a, b, c), angle between lines, perpendicular and parallel conditions.",
            subtopics = listOf(
                "Direction ratios", "Direction cosines", "Direction angles",
                "Relationship between DRs and DCs", "Angle between lines", "Perpendicular lines"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH08_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_8_01",
                    chapterId = "STD12_P1_CH08",
                    topicId = "DCs and DRs",
                    formulaName = "Fundamental DC Identity & Relation to DRs",
                    formula = "l^2 + m^2 + n^2 = 1, \\quad \\cos^2\\alpha + \\cos^2\\beta + \\cos^2\\gamma = 1, \\quad l = \\frac{a}{\\sqrt{a^2 + b^2 + c^2}}",
                    description = "Direction cosines from direction angles α, β, γ and direction ratios a, b, c."
                ),
                FormulaItem(
                    formulaId = "FOR_12_8_02",
                    chapterId = "STD12_P1_CH08",
                    topicId = "Angle Between 3D Lines",
                    formulaName = "Angle and Orthogonality in 3D",
                    formula = "\\cos\\theta = \\frac{a_1 a_2 + b_1 b_2 + c_1 c_2}{\\sqrt{a_1^2 + b_1^2 + c_1^2}\\sqrt{a_2^2 + b_2^2 + c_2^2}}, \\quad \\text{Perpendicular: } a_1 a_2 + b_1 b_2 + c_1 c_2 = 0",
                    description = "Acute angle between lines with direction ratios (a₁, b₁, c₁) and (a₂, b₂, c₂)."
                )
            ),
            importantConcepts = listOf(
                "sin²α + sin²β + sin²γ = 3 - (cos²α + cos²β + cos²γ) = 3 - 1 = 2.",
                "Two lines are parallel iff a₁/a₂ = b₁/b₂ = c₁/c₂."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_8_01",
                    title = "Direction Cosines from Ratios",
                    problem = "If a line has direction ratios 2, -1, -2, find its direction cosines.",
                    solution = "Magnitude = \\sqrt{2^2 + (-1)^2 + (-2)^2} = \\sqrt{4 + 1 + 4} = \\sqrt{9} = 3.\nDirection cosines (l, m, n) = (2/3, -1/3, -2/3).",
                    keyConcept = "l = a / sqrt(a² + b² + c²)"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET.",
            jeeMainRelevance = "Key chapter in JEE Main with high scoring potential.",
            cetWeightageMarks = 6
        ),

        // Chapter 9
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH09",
            name = "Line in 3D",
            standard = 12,
            part = 1,
            chapterNumber = 9,
            description = "Vector and Cartesian equations of lines, line through point/two points, distance of point from line, shortest distance between skew lines.",
            subtopics = listOf(
                "Vector equation", "Cartesian equation", "Line through point", "Line through two points",
                "Direction ratios", "Direction cosines", "Distance of point from line",
                "Distance between parallel lines", "Distance between skew lines", "Angle between lines"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH09_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_9_01",
                    chapterId = "STD12_P1_CH09",
                    topicId = "Line Equations",
                    formulaName = "Vector & Cartesian Line Equation",
                    formula = "\\vec{r} = \\vec{a} + \\lambda\\vec{b}, \\quad \\frac{x - x_1}{a} = \\frac{y - y_1}{b} = \\frac{z - z_1}{c} = \\lambda",
                    description = "Line passing through point a with direction vector b."
                ),
                FormulaItem(
                    formulaId = "FOR_12_9_02",
                    chapterId = "STD12_P1_CH09",
                    topicId = "Skew Lines",
                    formulaName = "Shortest Distance Between Skew Lines",
                    formula = "d = \\frac{|(\\vec{a}_2 - \\vec{a}_1) \\cdot (\\vec{b}_1 \\times \\vec{b}_2)|}{|\\vec{b}_1 \\times \\vec{b}_2|}",
                    description = "Shortest distance between non-parallel, non-intersecting lines r = a₁ + λb₁ and r = a₂ + μb₂."
                )
            ),
            importantConcepts = listOf(
                "Two lines intersect iff the shortest distance between them is zero: (a₂ - a₁) · (b₁ × b₂) = 0.",
                "Skew lines are lines in 3D space that are neither parallel nor intersecting."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_9_01",
                    title = "Cartesian Line from Points",
                    problem = "Find equation of line passing through (1, 2, 3) and (4, 5, 6).",
                    solution = "Direction ratios: a = 4-1 = 3, b = 5-2 = 3, c = 6-3 = 3 (or 1, 1, 1).\nCartesian equation: (x - 1)/1 = (y - 2)/1 = (z - 3)/1.",
                    keyConcept = "(x - x₁)/(x₂ - x₁) = (y - y₁)/(y₂ - y₁) = (z - z₁)/(z₂ - z₁)"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET.",
            jeeMainRelevance = "Shortest distance between skew lines is tested in almost every JEE paper.",
            cetWeightageMarks = 6
        ),

        // Chapter 10
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH10",
            name = "Plane",
            standard = 12,
            part = 1,
            chapterNumber = 10,
            description = "Vector & Cartesian forms of plane equation, normal form, plane through 3 points, intersection of planes, angle between planes, distance from point to plane.",
            subtopics = listOf(
                "Equation of plane", "Normal form", "Vector form", "Cartesian form",
                "Plane through point", "Plane through three points", "Plane perpendicular to vector",
                "Plane parallel to vectors", "Intersection of planes", "Angle between planes",
                "Angle between line and plane", "Distance from point to plane", "Coplanarity"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH10_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_10_01",
                    chapterId = "STD12_P1_CH10",
                    topicId = "Plane Equations",
                    formulaName = "General & Normal Plane Equation",
                    formula = "\\vec{r} \\cdot \\hat{n} = d \\quad (\\text{Normal}), \\quad ax + by + cz + d = 0 \\quad (\\text{General Cartesian})",
                    description = "Plane with normal vector n = (a, b, c) and perpendicular distance d."
                ),
                FormulaItem(
                    formulaId = "FOR_12_10_02",
                    chapterId = "STD12_P1_CH10",
                    topicId = "Distance Point to Plane",
                    formulaName = "Perpendicular Distance from (x₁, y₁, z₁) to Plane",
                    formula = "p = \\frac{|ax_1 + by_1 + cz_1 + d|}{\\sqrt{a^2 + b^2 + c^2}}",
                    description = "Distance of point from plane ax + by + cz + d = 0."
                )
            ),
            importantConcepts = listOf(
                "Angle θ between line with DRs (a₁, b₁, c₁) and plane with normal (a₂, b₂, c₂): sin θ = |(a₁a₂ + b₁b₂ + c₁c₂)| / (√Σa₁² √Σa₂²).",
                "Distance between parallel planes ax+by+cz+d₁=0 and ax+by+cz+d₂=0 is |d₁ - d₂| / √(a² + b² + c²)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_10_01",
                    title = "Distance from Point to Plane",
                    problem = "Find distance of (1, 1, 1) from plane 2x - 2y + z + 5 = 0.",
                    solution = "p = \\frac{|2(1) - 2(1) + 1(1) + 5|}{\\sqrt{2^2 + (-2)^2 + 1^2}} = \\frac{|1 + 5|}{\\sqrt{4 + 4 + 1}} = \\frac{6}{3} = 2 \\text{ units}.",
                    keyConcept = "Perpendicular distance formula for 3D plane"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 6 marks in MHT-CET.",
            jeeMainRelevance = "Tested together with Line in 3D in JEE Main.",
            cetWeightageMarks = 6
        ),

        // Chapter 11
        ComprehensiveChapter(
            chapterId = "STD12_P1_CH11",
            name = "Linear Programming",
            standard = 12,
            part = 1,
            chapterNumber = 11,
            description = "Formulation of LPP, objective function, constraints, feasible region, corner point method for maximization and minimization problems.",
            subtopics = listOf(
                "Linear programming problem", "Decision variables", "Objective function",
                "Constraints", "Non-negativity constraints", "Feasible region", "Feasible solution",
                "Optimal solution", "Graphical method", "Corner point method", "Maximum/minimum problems"
            ).mapIndexed { idx, title -> SubtopicItem("STD12_P1_CH11_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_12_11_01",
                    chapterId = "STD12_P1_CH11",
                    topicId = "LPP Formulation",
                    formulaName = "General LPP Objective & Constraint Format",
                    formula = "\\text{Optimize } Z = ax + by \\quad \\text{subject to } a_i x + b_i y \\le c_i, \\quad x \\ge 0, y \\ge 0",
                    description = "Linear objective function subjected to linear inequality constraints."
                )
            ),
            importantConcepts = listOf(
                "Corner Point Theorem: If a feasible region is convex and bounded, the optimal solution always occurs at one of the vertices (corner points).",
                "If the feasible region is unbounded, optimal value may or may not exist."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_12_11_01",
                    title = "Corner Point Optimization",
                    problem = "Maximize Z = 3x + 5y given corner points (0, 0), (4, 0), (2, 3), (0, 4).",
                    solution = "Z(0, 0) = 0\nZ(4, 0) = 12\nZ(2, 3) = 3(2) + 5(3) = 6 + 15 = 21\nZ(0, 4) = 20\nMaximum value of Z is 21 at (2, 3).",
                    keyConcept = "Evaluate Z at all corner points"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "Guaranteed 4 marks (2 questions) with high scoring accuracy in MHT-CET.",
            jeeMainRelevance = "Basic graphical optimization questions in JEE Main.",
            cetWeightageMarks = 4
        )
    )
}
