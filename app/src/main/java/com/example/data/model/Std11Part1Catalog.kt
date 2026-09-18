package com.example.data.model

object Std11Part1Catalog {
    val chapters: List<ComprehensiveChapter> = listOf(
        // Chapter 1
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH01",
            name = "Measurement of Angles",
            standard = 11,
            part = 1,
            chapterNumber = 1,
            description = "Systems of angle measurement (sexagesimal and circular), angle in standard position, coterminal angles, arc length and sector area.",
            subtopics = listOf(
                "Directed angles", "Positive and negative angles", "Zero angle", "Straight angle",
                "Standard position", "Coterminal angles", "Quadrants", "Quadrantal angles",
                "Sexagesimal system", "Circular system", "Degree-radian conversion", "Arc length", "Area of sector"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH01_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_1_01",
                    chapterId = "STD11_P1_CH01",
                    topicId = "Conversion",
                    formulaName = "Degree to Radian",
                    formula = "\\theta\\text{ radians} = \\theta^{\\circ} \\times \\frac{\\pi}{180}",
                    variables = "\\theta = \\text{angle in degrees}",
                    description = "Converts sexagesimal degrees into circular system radians."
                ),
                FormulaItem(
                    formulaId = "FOR_11_1_02",
                    chapterId = "STD11_P1_CH01",
                    topicId = "Conversion",
                    formulaName = "Radian to Degree",
                    formula = "\\theta^{\\circ} = \\theta\\text{ radians} \\times \\frac{180}{\\pi}",
                    variables = "\\theta = \\text{angle in radians}",
                    description = "Converts circular radians into sexagesimal degrees (1 rad ≈ 57°17'45\")."
                ),
                FormulaItem(
                    formulaId = "FOR_11_1_03",
                    chapterId = "STD11_P1_CH01",
                    topicId = "Arc Length",
                    formulaName = "Arc Length of Circle",
                    formula = "s = r\\theta",
                    variables = "s = \\text{arc length}, r = \\text{radius}, \\theta = \\text{central angle in radians}",
                    description = "Calculates the length of an arc subtended by angle theta in radians."
                ),
                FormulaItem(
                    formulaId = "FOR_11_1_04",
                    chapterId = "STD11_P1_CH01",
                    topicId = "Sector Area",
                    formulaName = "Area of Sector",
                    formula = "A = \\frac{1}{2}r^2\\theta = \\frac{1}{2}rs",
                    variables = "A = \\text{sector area}, r = \\text{radius}, \\theta = \\text{angle in radians}, s = \\text{arc length}",
                    description = "Area of circular sector given radius and central angle in radians."
                )
            ),
            importantConcepts = listOf(
                "Initial ray and terminal ray determine the sign of the directed angle.",
                "Angles differing by an integral multiple of 360° (2π radians) are coterminal angles.",
                "Quadrantal angles have their terminal ray along coordinate axes (0°, 90°, 180°, 270°, etc.)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_1_01",
                    title = "Arc Length & Central Angle",
                    problem = "The radius of a circle is 14 cm. Find the length of an arc which subtends an angle of 45° at the centre.",
                    solution = "1. Convert 45° to radians: \\theta = 45 \\times \\frac{\\pi}{180} = \\frac{\\pi}{4} \\text{ rad}.\n2. Use s = r\\theta = 14 \\times \\frac{\\pi}{4} = \\frac{14 \\times 22}{4 \\times 7} = 11 \\text{ cm}.",
                    keyConcept = "s = r\\theta with \\theta in radians"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "High foundational value; 1 question asked periodically or embedded into trigonometry.",
            jeeMainRelevance = "Direct questions rare, but foundational for angular mechanics and calculus.",
            cetWeightageMarks = 2
        ),

        // Chapter 2
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH02",
            name = "Trigonometric Functions",
            standard = 11,
            part = 1,
            chapterNumber = 2,
            description = "Trigonometric functions using unit circle, standard angles, domain, range, periodicity, fundamental identities and trigonometric graphs.",
            subtopics = listOf(
                "Unit circle", "Trigonometric ratios", "Signs in four quadrants", "Standard angles",
                "Domain and range", "Periodicity", "Fundamental identities", "Trigonometric graphs",
                "sin x", "cos x", "tan x", "Transformations of graphs", "Negative angles"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH02_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_2_01",
                    chapterId = "STD11_P1_CH02",
                    topicId = "Fundamental Identities",
                    formulaName = "Pythagorean Identities",
                    formula = "\\sin^2 x + \\cos^2 x = 1, \\quad 1 + \\tan^2 x = \\sec^2 x, \\quad 1 + \\cot^2 x = \\csc^2 x",
                    description = "Three core trigonometric identities valid in their respective domains."
                ),
                FormulaItem(
                    formulaId = "FOR_11_2_02",
                    chapterId = "STD11_P1_CH02",
                    topicId = "Negative Angles",
                    formulaName = "Negative Angle Parity",
                    formula = "\\sin(-x) = -\\sin x, \\quad \\cos(-x) = \\cos x, \\quad \\tan(-x) = -\\tan x",
                    description = "cos x and sec x are even functions; sin, tan, cot, csc are odd."
                )
            ),
            importantConcepts = listOf(
                "ASTC rule: All positive in Q1, Sin/Csc in Q2, Tan/Cot in Q3, Cos/Sec in Q4.",
                "Periodicity: sin, cos, sec, csc have period 2π; tan, cot have period π.",
                "Standard values: sin(30°)=1/2, sin(45°)=1/√2, sin(60°)=√3/2, cos(0°)=1, cos(90°)=0."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_2_01",
                    title = "Sign and Value in Quadrant III",
                    problem = "If \\tan x = 4/3 and x lies in third quadrant, find the value of \\sin x + \\cos x.",
                    solution = "In Q3, sin x and cos x are negative. 1 + \\tan^2 x = \\sec^2 x \\implies \\sec^2 x = 1 + 16/9 = 25/9 \\implies \\cos x = -3/5.\n\\sin x = -\\sqrt{1 - 9/25} = -4/5. Thus \\sin x + \\cos x = -4/5 - 3/5 = -7/5.",
                    keyConcept = "Quadrant signs determine ± roots."
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 2-4 marks in Std 11 CET questions.",
            jeeMainRelevance = "Crucial stepping stone for calculus and functional equations.",
            cetWeightageMarks = 4
        ),

        // Chapter 3
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH03",
            name = "Trigonometric Functions of Compound Angles",
            standard = 11,
            part = 1,
            chapterNumber = 3,
            description = "Addition and subtraction theorems, double, triple, and half angle formulae.",
            subtopics = listOf(
                "sin(A+B)", "sin(A-B)", "cos(A+B)", "cos(A-B)",
                "tan(A+B)", "tan(A-B)", "Double angle", "Triple angle", "Half angle"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH03_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_3_01",
                    chapterId = "STD11_P1_CH03",
                    topicId = "Addition",
                    formulaName = "Sine and Cosine of Compound Angles",
                    formula = "\\sin(A \\pm B) = \\sin A\\cos B \\pm \\cos A\\sin B, \\quad \\cos(A \\pm B) = \\cos A\\cos B \\mp \\sin A\\sin B",
                    description = "Fundamental addition and subtraction identities."
                ),
                FormulaItem(
                    formulaId = "FOR_11_3_02",
                    chapterId = "STD11_P1_CH03",
                    topicId = "Double Angle",
                    formulaName = "Double Angle Formulae",
                    formula = "\\sin 2A = 2\\sin A\\cos A = \\frac{2\\tan A}{1+\\tan^2 A}, \\quad \\cos 2A = \\cos^2 A - \\sin^2 A = 2\\cos^2 A - 1 = 1 - 2\\sin^2 A",
                    description = "Double angle expressions in sine, cosine, and tangent."
                ),
                FormulaItem(
                    formulaId = "FOR_11_3_03",
                    chapterId = "STD11_P1_CH03",
                    topicId = "Triple Angle",
                    formulaName = "Triple Angle Formulae",
                    formula = "\\sin 3A = 3\\sin A - 4\\sin^3 A, \\quad \\cos 3A = 4\\cos^3 A - 3\\cos A, \\quad \\tan 3A = \\frac{3\\tan A - \\tan^3 A}{1 - 3\\tan^2 A}",
                    description = "Triple angle relations."
                )
            ),
            importantConcepts = listOf(
                "1 - cos 2A = 2 sin² A and 1 + cos 2A = 2 cos² A (Power reduction formulas indispensable in Integration).",
                "tan(A + B + C) when A + B + C = π implies tan A + tan B + tan C = tan A tan B tan C."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_3_01",
                    title = "Find tan 15°",
                    problem = "Compute the exact value of \\tan 15°.",
                    solution = "\\tan 15° = \\tan(45° - 30°) = \\frac{\\tan 45° - \\tan 30°}{1 + \\tan 45°\\tan 30°} = \\frac{1 - 1/\\sqrt{3}}{1 + 1/\\sqrt{3}} = \\frac{\\sqrt{3}-1}{\\sqrt{3}+1} = 2 - \\sqrt{3}.",
                    keyConcept = "Compound angle tangent formula"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Extremely high. Essential for CET XII integration and differentiation.",
            jeeMainRelevance = "Used heavily in complex numbers and trigonometry.",
            cetWeightageMarks = 4
        ),

        // Chapter 4
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH04",
            name = "Factorization Formulae",
            standard = 11,
            part = 1,
            chapterNumber = 4,
            description = "Sum to product and product to sum conversions, conditional trigonometric identities for angles of a triangle.",
            subtopics = listOf(
                "Sum to product", "Product to sum", "Trigonometric transformations", "Trigonometric functions of angles of a triangle"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH04_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_4_01",
                    chapterId = "STD11_P1_CH04",
                    topicId = "Sum to Product",
                    formulaName = "C-D Formulae",
                    formula = "\\sin C + \\sin D = 2\\sin\\frac{C+D}{2}\\cos\\frac{C-D}{2}, \\quad \\cos C + \\cos D = 2\\cos\\frac{C+D}{2}\\cos\\frac{C-D}{2}",
                    description = "Converts sum or difference of sines/cosines into products."
                ),
                FormulaItem(
                    formulaId = "FOR_11_4_02",
                    chapterId = "STD11_P1_CH04",
                    topicId = "Product to Sum",
                    formulaName = "Product to Sum Formulae",
                    formula = "2\\sin A\\cos B = \\sin(A+B) + \\sin(A-B), \\quad 2\\cos A\\cos B = \\cos(A+B) + \\cos(A-B)",
                    description = "Converts product of terms into sum or difference."
                )
            ),
            importantConcepts = listOf(
                "In triangle ABC, A + B + C = π. Thus sin(A+B) = sin C and cos(A+B) = -cos C."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_4_01",
                    title = "Product into Sum",
                    problem = "Express 2 \\sin 5x \\cos 3x as a sum.",
                    solution = "Using 2\\sin A\\cos B = \\sin(A+B) + \\sin(A-B):\n2 \\sin 5x \\cos 3x = \\sin(5x + 3x) + \\sin(5x - 3x) = \\sin 8x + \\sin 2x.",
                    keyConcept = "Product to sum transformation"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Regularly tested in MHT-CET calculus integration.",
            jeeMainRelevance = "Used for telescoping sums and product series.",
            cetWeightageMarks = 2
        ),

        // Chapter 5
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH05",
            name = "Locus",
            standard = 11,
            part = 1,
            chapterNumber = 5,
            description = "Definition of locus, algebraic formulation of moving points under given geometric constraints, shift of origin.",
            subtopics = listOf(
                "Definition of locus", "Equation of locus", "Locus of a moving point", "Basic locus problems", "Shift of origin"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH05_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_5_01",
                    chapterId = "STD11_P1_CH05",
                    topicId = "Shift of Origin",
                    formulaName = "Translation of Axes",
                    formula = "x = X + h, \\quad y = Y + k",
                    variables = "(h, k) = \\text{new origin}, (x,y) = \\text{old coords}, (X,Y) = \\text{new coords}",
                    description = "Transformation of coordinates when the origin is shifted to (h, k) without rotating axes."
                )
            ),
            importantConcepts = listOf(
                "A locus is the set of all points, and only those points, whose coordinates satisfy the given geometric condition.",
                "Steps: Take moving point P(h, k), apply distance/slope condition, simplify and replace (h, k) by (x, y)."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_5_01",
                    title = "Equidistant Point Locus",
                    problem = "Find the equation of locus of point P which is equidistant from A(1, 3) and B(5, 7).",
                    solution = "PA = PB \\implies PA^2 = PB^2 \\implies (x-1)^2 + (y-3)^2 = (x-5)^2 + (y-7)^2\n\\implies x^2-2x+1 + y^2-6y+9 = x^2-10x+25 + y^2-14y+49\n\\implies 8x + 8y - 64 = 0 \\implies x + y - 8 = 0 (Perpendicular bisector).",
                    keyConcept = "Equidistant locus is perpendicular bisector"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "Foundation for conic sections in Std 11 & 12.",
            jeeMainRelevance = "Important analytical geometry fundamental.",
            cetWeightageMarks = 2
        ),

        // Chapter 6
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH06",
            name = "Straight Line",
            standard = 11,
            part = 1,
            chapterNumber = 6,
            description = "Inclination, slope, various standard forms of line equations, distance of point from line, parallel/perpendicular conditions, family of lines.",
            subtopics = listOf(
                "Inclination", "Slope", "Coordinate axes", "Intercept form", "Slope-point form",
                "Slope-intercept form", "Two-point form", "Double-intercept form", "Parametric form",
                "Normal form", "General form", "Angle between lines", "Parallel lines",
                "Perpendicular lines", "Concurrent lines", "Distance of point from line",
                "Distance between parallel lines", "Angle bisectors", "Family of lines"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH06_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_6_01",
                    chapterId = "STD11_P1_CH06",
                    topicId = "Slope",
                    formulaName = "Slope and Forms",
                    formula = "m = \\tan\\theta = \\frac{y_2-y_1}{x_2-x_1}, \\quad y - y_1 = m(x - x_1), \\quad y = mx + c, \\quad \\frac{x}{a} + \\frac{y}{b} = 1",
                    description = "Standard line representation forms."
                ),
                FormulaItem(
                    formulaId = "FOR_11_6_02",
                    chapterId = "STD11_P1_CH06",
                    topicId = "Perpendicular Distance",
                    formulaName = "Distance from Point to Line",
                    formula = "d = \\frac{|ax_1 + by_1 + c|}{\\sqrt{a^2 + b^2}}",
                    variables = "(x_1, y_1) = \\text{point}, ax+by+c=0 = \\text{line equation}",
                    description = "Perpendicular distance of point (x₁, y₁) from line ax + by + c = 0."
                ),
                FormulaItem(
                    formulaId = "FOR_11_6_03",
                    chapterId = "STD11_P1_CH06",
                    topicId = "Parallel Distance",
                    formulaName = "Distance Between Parallel Lines",
                    formula = "d = \\frac{|c_1 - c_2|}{\\sqrt{a^2 + b^2}}",
                    variables = "ax+by+c_1=0 \\text{ and } ax+by+c_2=0",
                    description = "Distance between two parallel lines with identical coefficients a and b."
                )
            ),
            importantConcepts = listOf(
                "Two lines with slopes m₁ and m₂ are perpendicular iff m₁ · m₂ = -1.",
                "Angle between two lines: tan θ = |(m₁ - m₂) / (1 + m₁ m₂)|.",
                "Three lines are concurrent if the determinant of their coefficients is zero."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_6_01",
                    title = "Perpendicular Distance",
                    problem = "Find the distance of point (2, 3) from the line 3x + 4y - 8 = 0.",
                    solution = "d = \\frac{|3(2) + 4(3) - 8|}{\\sqrt{3^2 + 4^2}} = \\frac{|6 + 12 - 8|}{\\sqrt{25}} = \\frac{10}{5} = 2 \\text{ units}.",
                    keyConcept = "Perpendicular distance formula"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct weightage 4 marks (2 questions) in CET.",
            jeeMainRelevance = "Core fundamental of Coordinate Geometry in JEE Main.",
            cetWeightageMarks = 4
        ),

        // Chapter 7
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH07",
            name = "Circle and Conics",
            standard = 11,
            part = 1,
            chapterNumber = 7,
            description = "Standard, centre-radius, diameter and general equations of circle, focus-directrix definition, standard parabola, ellipse, and hyperbola.",
            subtopics = listOf(
                "Circle", "Standard equation", "Centre-radius form", "General equation",
                "Diameter form", "Parametric equation", "Conic sections", "Parabola",
                "Ellipse", "Hyperbola", "Focus-directrix definition", "Standard equations",
                "Parametric equations", "Applications"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH07_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_7_01",
                    chapterId = "STD11_P1_CH07",
                    topicId = "Circle",
                    formulaName = "General Equation of Circle",
                    formula = "x^2 + y^2 + 2gx + 2fy + c = 0, \\quad \\text{Center} = (-g, -f), \\quad r = \\sqrt{g^2 + f^2 - c}",
                    description = "General second-degree circular equation."
                ),
                FormulaItem(
                    formulaId = "FOR_11_7_02",
                    chapterId = "STD11_P1_CH07",
                    topicId = "Conics",
                    formulaName = "Standard Conic Sections",
                    formula = "y^2 = 4ax (e=1), \\quad \\frac{x^2}{a^2} + \\frac{y^2}{b^2} = 1 (e<1), \\quad \\frac{x^2}{a^2} - \\frac{y^2}{b^2} = 1 (e>1)",
                    description = "Standard forms of parabola, ellipse, and hyperbola with eccentricity e."
                )
            ),
            importantConcepts = listOf(
                "Condition for circle: coefficient of x² = coefficient of y² and coefficient of xy = 0.",
                "Eccentricity e = SP/PM: e = 1 for Parabola, e < 1 for Ellipse, e > 1 for Hyperbola."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_7_01",
                    title = "Circle Radius and Center",
                    problem = "Find center and radius of x² + y² - 4x + 6y - 12 = 0.",
                    solution = "2g = -4 \\implies g = -2; 2f = 6 \\implies f = 3; c = -12.\nCenter = (-g, -f) = (2, -3).\nRadius = \\sqrt{g^2 + f^2 - c} = \\sqrt{4 + 9 - (-12)} = \\sqrt{25} = 5.",
                    keyConcept = "r = sqrt(g² + f² - c)"
                )
            ),
            difficultyLevel = "Hard",
            mhtCetRelevance = "Direct weightage 4 marks in MHT-CET Std 11 section.",
            jeeMainRelevance = "One of the most heavily tested domains in JEE Main (2-3 questions per paper).",
            cetWeightageMarks = 4
        ),

        // Chapter 8
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH08",
            name = "Vectors",
            standard = 11,
            part = 1,
            chapterNumber = 8,
            description = "Vector magnitude, types of vectors, triangle and parallelogram laws, scalar and vector products, physical applications.",
            subtopics = listOf(
                "Vector definition", "Magnitude", "Zero vector", "Unit vector", "Equal vectors",
                "Negative vector", "Parallel vectors", "Collinear vectors", "Coplanar vectors",
                "Vector addition", "Triangle law", "Parallelogram law", "Polygon law",
                "Position vector", "3D vectors", "Scalar product", "Vector product",
                "Work done", "Resolution of force", "Moment of force"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH08_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_8_01",
                    chapterId = "STD11_P1_CH08",
                    topicId = "Products",
                    formulaName = "Scalar and Vector Products",
                    formula = "\\vec{a} \\cdot \\vec{b} = |\\vec{a}||\\vec{b}|\\cos\\theta, \\quad \\vec{a} \\times \\vec{b} = |\\vec{a}||\\vec{b}|\\sin\\theta\\,\\hat{n}",
                    description = "Dot product results in a scalar, cross product results in a normal vector."
                ),
                FormulaItem(
                    formulaId = "FOR_11_8_02",
                    chapterId = "STD11_P1_CH08",
                    topicId = "Magnitude",
                    formulaName = "Vector Magnitude & Unit Vector",
                    formula = "|\\vec{a}| = \\sqrt{a_1^2 + a_2^2 + a_3^2}, \\quad \\hat{a} = \\frac{\\vec{a}}{|\\vec{a}|}",
                    description = "Length of 3D vector and normalized unit direction."
                )
            ),
            importantConcepts = listOf(
                "Two non-zero vectors are perpendicular iff a · b = 0.",
                "Two non-zero vectors are parallel/collinear iff a × b = 0 (or a = λb).",
                "Work done W = F · d and Torque / Moment = r × F."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_8_01",
                    title = "Perpendicular Vectors Condition",
                    problem = "Find p such that 2i + pj + k is perpendicular to 4i - 2j - 2k.",
                    solution = "For perpendicularity, \\vec{a} \\cdot \\vec{b} = 0:\n(2)(4) + (p)(-2) + (1)(-2) = 0 \\implies 8 - 2p - 2 = 0 \\implies 6 = 2p \\implies p = 3.",
                    keyConcept = "a · b = 0 for orthogonal vectors"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Crucial foundation for Std 12 Vectors and 3D Geometry (total 12-14 marks).",
            jeeMainRelevance = "Guaranteed 1-2 questions in every JEE shift.",
            cetWeightageMarks = 4
        ),

        // Chapter 9
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH09",
            name = "Linear Inequations",
            standard = 11,
            part = 1,
            chapterNumber = 9,
            description = "Inequations in one and two variables, graphical representations, feasible region determination.",
            subtopics = listOf(
                "Linear inequations in one variable", "Graphical representation", "Systems of inequations",
                "Two-variable inequations", "Feasible regions", "Graphical solutions"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH09_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_9_01",
                    chapterId = "STD11_P1_CH09",
                    topicId = "Inequality Rule",
                    formulaName = "Sign Inversion under Negative Multiplication",
                    formula = "ax \\le b \\implies x \\ge \\frac{b}{a} \\quad \\text{if } a < 0",
                    description = "Reverses the direction of inequality when multiplied/divided by a negative quantity."
                )
            ),
            importantConcepts = listOf(
                "The line ax + by + c = 0 divides the Cartesian plane into two open half-planes.",
                "Testing the origin (0, 0) determines which half-plane satisfies the inequation."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_9_01",
                    title = "Feasible Half-Plane",
                    problem = "Determine whether origin lies in the solution set of 2x + 3y ≤ 6.",
                    solution = "Substitute (0, 0): 2(0) + 3(0) = 0 ≤ 6 (True). Thus, the origin is included in the solution region.",
                    keyConcept = "Origin test for half-planes"
                )
            ),
            difficultyLevel = "Easy",
            mhtCetRelevance = "Direct prerequisite for Std 12 Linear Programming (LPP).",
            jeeMainRelevance = "Used in optimization and range analysis.",
            cetWeightageMarks = 2
        ),

        // Chapter 10
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH10",
            name = "Determinants",
            standard = 11,
            part = 1,
            chapterNumber = 10,
            description = "Determinants of order 2 and 3, minors, cofactors, properties of determinants, Cramer's rule, area of triangle.",
            subtopics = listOf(
                "Determinants", "Order 2", "Order 3", "Minors", "Cofactors",
                "Expansion", "Properties", "Area of triangle", "Cramer's Rule", "Applications"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH10_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_10_01",
                    chapterId = "STD11_P1_CH10",
                    topicId = "Cramer's Rule",
                    formulaName = "Cramer's Rule for Linear Systems",
                    formula = "x = \\frac{D_x}{D}, \\quad y = \\frac{D_y}{D}, \\quad z = \\frac{D_z}{D} \\quad (D \\ne 0)",
                    description = "Gives unique solutions for system of 3 linear equations in 3 variables."
                ),
                FormulaItem(
                    formulaId = "FOR_11_10_02",
                    chapterId = "STD11_P1_CH10",
                    topicId = "Area",
                    formulaName = "Triangle Area via Determinants",
                    formula = "\\text{Area} = \\frac{1}{2}\\left|\\det\\begin{pmatrix} x_1 & y_1 & 1 \\\\ x_2 & y_2 & 1 \\\\ x_3 & y_3 & 1 \\end{pmatrix}\\right|",
                    description = "Area of triangle with vertices (x₁, y₁), (x₂, y₂), (x₃, y₃). Zero area indicates collinearity."
                )
            ),
            importantConcepts = listOf(
                "If two rows (or columns) are identical or proportional, the determinant value is zero.",
                "Elementary row/column operations Ri -> Ri + k Rj do not change the determinant value."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_10_01",
                    title = "Cramer's Rule Solution",
                    problem = "Solve 2x + y = 7 and 3x - y = 3.",
                    solution = "D = |2  1; 3 -1| = -2 - 3 = -5.\nDx = |7  1; 3 -1| = -7 - 3 = -10 \\implies x = -10 / -5 = 2.\nDy = |2  7; 3  3| = 6 - 21 = -15 \\implies y = -15 / -5 = 3.\nSolution is (2, 3).",
                    keyConcept = "x = Dx / D, y = Dy / D"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct 2-4 marks; essential for Std 12 Matrices.",
            jeeMainRelevance = "Consistency of linear equations is tested in every JEE session.",
            cetWeightageMarks = 4
        ),

        // Chapter 11
        ComprehensiveChapter(
            chapterId = "STD11_P1_CH11",
            name = "Matrices",
            standard = 11,
            part = 1,
            chapterNumber = 11,
            description = "Definition, order, types of matrices, matrix algebra, transpose, symmetric and skew-symmetric matrices.",
            subtopics = listOf(
                "Definition", "Order", "Row matrix", "Column matrix", "Square matrix",
                "Zero matrix", "Diagonal matrix", "Scalar matrix", "Identity matrix",
                "Triangular matrix", "Singular matrix", "Non-singular matrix", "Transpose",
                "Symmetric matrix", "Skew-symmetric matrix", "Addition", "Subtraction",
                "Scalar multiplication", "Matrix multiplication", "Properties of matrix multiplication",
                "Properties of transpose"
            ).mapIndexed { idx, title -> SubtopicItem("STD11_P1_CH11_S${idx + 1}", title) },
            importantFormulas = listOf(
                FormulaItem(
                    formulaId = "FOR_11_11_01",
                    chapterId = "STD11_P1_CH11",
                    topicId = "Transpose",
                    formulaName = "Transpose Properties",
                    formula = "(AB)^T = B^T A^T, \\quad (A + B)^T = A^T + B^T, \\quad (A^T)^T = A",
                    description = "Reversal law of transpose of matrix products."
                ),
                FormulaItem(
                    formulaId = "FOR_11_11_02",
                    chapterId = "STD11_P1_CH11",
                    topicId = "Symmetric Decomposition",
                    formulaName = "Symmetric and Skew-Symmetric Decomposition",
                    formula = "A = \\frac{1}{2}(A + A^T) + \\frac{1}{2}(A - A^T)",
                    description = "Any square matrix can be uniquely expressed as sum of symmetric and skew-symmetric matrices."
                )
            ),
            importantConcepts = listOf(
                "Matrix multiplication is non-commutative in general: AB ≠ BA.",
                "For a skew-symmetric matrix, A^T = -A, and all diagonal entries are strictly zero."
            ),
            solvedExamples = listOf(
                SolvedExampleItem(
                    id = "EX_11_11_01",
                    title = "Matrix Product Verification",
                    problem = "If A = [[1, 2], [3, 4]] and B = [[1, 0], [0, 2]], find AB.",
                    solution = "Row 1: [1*1 + 2*0, 1*0 + 2*2] = [1, 4].\nRow 2: [3*1 + 4*0, 3*0 + 4*2] = [3, 8].\nAB = [[1, 4], [3, 8]].",
                    keyConcept = "Row-by-column dot multiplication"
                )
            ),
            difficultyLevel = "Medium",
            mhtCetRelevance = "Direct foundation for Std 12 Matrices (inverse, adjoint, transformations) worth 6 marks.",
            jeeMainRelevance = "Tested as high-weightage topic across all JEE papers.",
            cetWeightageMarks = 4
        )
    )
}
