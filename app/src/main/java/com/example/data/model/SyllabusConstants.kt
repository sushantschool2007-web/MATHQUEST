package com.example.data.model

data class ChapterMetadata(
    val id: String,
    val name: String,
    val stdClass: Int,
    val weightageCetMarks: Int,
    val description: String,
    val icon: String,
    val subtopics: List<String>
)

object SyllabusConstants {
    val STD_XI_CHAPTERS = listOf(
        ChapterMetadata(
            id = "XI_TRIG_2",
            name = "Trigonometry II",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Trigonometric functions of sum, difference, multiple and submultiple angles, transformation formulae.",
            icon = "📐",
            subtopics = listOf("Compound Angles", "Multiple & Submultiple Angles", "Factorization Formulae", "Trigonometric Equations")
        ),
        ChapterMetadata(
            id = "XI_STRAIGHT_LINE",
            name = "Straight Line",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Slope, various forms of line equations, distance of point from line, angle between lines.",
            icon = "📏",
            subtopics = listOf("Slope & Intercepts", "Various Forms of Lines", "Distance & Parallel Lines", "Point of Intersection")
        ),
        ChapterMetadata(
            id = "XI_CIRCLE",
            name = "Circle",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Standard equation, general equation, tangent and normal, condition of tangency.",
            icon = "⚪",
            subtopics = listOf("Standard & General Form", "Equation of Tangent", "Length of Tangent", "Family of Circles")
        ),
        ChapterMetadata(
            id = "XI_PROBABILITY",
            name = "Probability",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Classical & axiomatic probability, conditional probability, multiplication theorem, Bayes' theorem.",
            icon = "🎲",
            subtopics = listOf("Conditional Probability", "Multiplication Rule", "Bayes' Theorem", "Independent Events")
        ),
        ChapterMetadata(
            id = "XI_COMPLEX_NUMBERS",
            name = "Complex Numbers",
            stdClass = 11,
            weightageCetMarks = 2,
            description = "Algebra of complex numbers, modulus and argument, polar & exponential form, De Moivre's theorem.",
            icon = "🔢",
            subtopics = listOf("Algebra of Complex Numbers", "Modulus and Argument", "Polar Form", "Cube Roots of Unity")
        ),
        ChapterMetadata(
            id = "XI_PERM_COMB",
            name = "Permutations and Combinations",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Fundamental principles of counting, permutations with/without repetition, combinations, properties.",
            icon = "🔄",
            subtopics = listOf("Fundamental Counting Rule", "Linear Permutations", "Combinations Formula", "Word Formation & Digits")
        ),
        ChapterMetadata(
            id = "XI_FUNCTIONS",
            name = "Functions",
            stdClass = 11,
            weightageCetMarks = 2,
            description = "Types of functions (injective, surjective, bijective), domain, range, composition, inverse functions.",
            icon = "📈",
            subtopics = listOf("Domain and Range", "One-One & Onto Functions", "Composite Functions", "Inverse Functions")
        ),
        ChapterMetadata(
            id = "XI_LIMITS",
            name = "Limits",
            stdClass = 11,
            weightageCetMarks = 2,
            description = "Standard limits, algebraic, trigonometric, exponential, logarithmic limits, L'Hôpital's rule shortcuts.",
            icon = "🎯",
            subtopics = listOf("Algebraic Limits", "Trigonometric Limits", "Exponential & Log Limits", "L'Hôpital Shortcuts")
        ),
        ChapterMetadata(
            id = "XI_CONTINUITY",
            name = "Continuity",
            stdClass = 11,
            weightageCetMarks = 2,
            description = "Continuity at a point and in an interval, removable & jump discontinuities, standard theorems.",
            icon = "〰️",
            subtopics = listOf("Definition of Continuity", "Types of Discontinuity", "Continuity of Composite Functions")
        ),
        ChapterMetadata(
            id = "XI_CONIC_SECTION",
            name = "Conic Section",
            stdClass = 11,
            weightageCetMarks = 4,
            description = "Parabola, ellipse, and hyperbola standard equations, eccentricity, focal properties, tangents.",
            icon = "🪐",
            subtopics = listOf("Parabola Properties", "Ellipse Standard Form", "Hyperbola & Asymptotes", "Focal Chord & Tangents")
        )
    )

    val STD_XII_CHAPTERS = listOf(
        ChapterMetadata(
            id = "XII_LOGIC",
            name = "Mathematical Logic",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Statements, truth tables, tautology, contradiction, duals, negations, switching circuits.",
            icon = "💡",
            subtopics = listOf("Statements & Truth Tables", "Tautology & Contradiction", "Negation of Compound Statements", "Switching Circuits")
        ),
        ChapterMetadata(
            id = "XII_MATRICES",
            name = "Matrices",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Elementary transformations, inverse of matrix by adjoint and reduction method, system of linear equations.",
            icon = "🔲",
            subtopics = listOf("Elementary Row Operations", "Adjoint & Inverse", "Method of Inversion", "Method of Reduction")
        ),
        ChapterMetadata(
            id = "XII_TRIG_FUNCTIONS",
            name = "Trigonometric Functions",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Principal and general solutions, polar coordinates, sine rule, cosine rule, projection rule, inverse trig functions.",
            icon = "🌀",
            subtopics = listOf("General Solutions", "Sine & Cosine Rules", "Half Angle Formulae", "Inverse Trigonometric Functions")
        ),
        ChapterMetadata(
            id = "XII_PAIR_STRAIGHT_LINES",
            name = "Pair of Straight Lines",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Combined equation of pair of lines, homogeneous equation, angle between lines, general second degree equation.",
            icon = "⚔️",
            subtopics = listOf("Homogeneous Equations", "Angle Between Pair of Lines", "Condition for Parallel/Perpendicular", "General 2nd Degree Equation")
        ),
        ChapterMetadata(
            id = "XII_VECTORS",
            name = "Vectors",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Section formula, scalar (dot) product, vector (cross) product, scalar triple product, vector triple product.",
            icon = "↗️",
            subtopics = listOf("Linear Combination & Collinearity", "Dot Product & Projection", "Cross Product & Area", "Scalar Triple Product & Coplanarity")
        ),
        ChapterMetadata(
            id = "XII_3D_GEOMETRY",
            name = "Three Dimensional Geometry",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Direction cosines and direction ratios, relation between direction cosines, angle between two lines.",
            icon = "🧊",
            subtopics = listOf("Direction Angles & Cosines", "Direction Ratios", "Angle Between Lines", "Perpendicularity & Parallelism")
        ),
        ChapterMetadata(
            id = "XII_LINE_PLANE",
            name = "Line and Plane",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Vector and Cartesian equation of line, skew lines, shortest distance, equation of plane, angle between planes.",
            icon = "✈️",
            subtopics = listOf("Vector & Cartesian Line Eq", "Shortest Distance Between Skew Lines", "Equation of Plane", "Distance of Point from Plane")
        ),
        ChapterMetadata(
            id = "XII_LPP",
            name = "Linear Programming",
            stdClass = 12,
            weightageCetMarks = 4,
            description = "Formulation of LPP, graphical method of solution, convex feasible region, bounded and unbounded solutions.",
            icon = "📊",
            subtopics = listOf("Formulation of LPP", "Feasible Region", "Corner Point Method", "Maximum/Minimum Optimization")
        ),
        ChapterMetadata(
            id = "XII_DIFF",
            name = "Differentiation",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Chain rule, derivatives of inverse, logarithmic, implicit, parametric functions, second order derivatives.",
            icon = "⚡",
            subtopics = listOf("Chain Rule & Inverse Trig", "Logarithmic Differentiation", "Parametric & Implicit", "Higher Order Derivatives")
        ),
        ChapterMetadata(
            id = "XII_AOD",
            name = "Applications of Derivatives",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Rate measure, tangent and normal, approximations, Rolle's & LMVT, increasing/decreasing, maxima and minima.",
            icon = "📈",
            subtopics = listOf("Rate of Change & Approximations", "Tangents and Normals", "Increasing and Decreasing Functions", "Maxima and Minima Word Problems")
        ),
        ChapterMetadata(
            id = "XII_INDEF_INTEG",
            name = "Indefinite Integration",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Standard integrals, substitution method, integration by parts, partial fractions, special integrals.",
            icon = "∫",
            subtopics = listOf("Integration by Substitution", "Standard Special Forms", "Integration by Parts", "Partial Fractions Method")
        ),
        ChapterMetadata(
            id = "XII_DEF_INTEG",
            name = "Definite Integration",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Fundamental theorem of calculus, properties of definite integrals, king's rule, reduction formulas.",
            icon = "🎯",
            subtopics = listOf("Fundamental Theorem", "King's Rule Properties", "Even & Odd Function Integrals", "Periodic Integrals")
        ),
        ChapterMetadata(
            id = "XII_APP_DEF_INTEG",
            name = "Application of Definite Integration",
            stdClass = 12,
            weightageCetMarks = 4,
            description = "Area bounded by standard curves, area between line and curve, area between two parabolas/circles.",
            icon = "🗺️",
            subtopics = listOf("Area Under Curve", "Area Between Line and Parabola", "Area Between Two Conics")
        ),
        ChapterMetadata(
            id = "XII_DIFF_EQ",
            name = "Differential Equations",
            stdClass = 12,
            weightageCetMarks = 8,
            description = "Order and degree, formation of DE, variable separable, homogeneous, linear differential equations, applications.",
            icon = "🔄",
            subtopics = listOf("Order and Degree", "Variable Separable Form", "Homogeneous Differential Equations", "Linear Differential Equations (IF)")
        ),
        ChapterMetadata(
            id = "XII_PROB_DIST",
            name = "Probability Distribution",
            stdClass = 12,
            weightageCetMarks = 6,
            description = "Random variable, PMF, PDF, expected value, variance, standard deviation of discrete random variables.",
            icon = "📊",
            subtopics = listOf("Discrete Random Variables & PMF", "Cumulative Distribution Function", "Expected Value E(X)", "Variance Var(X)")
        ),
        ChapterMetadata(
            id = "XII_BINOMIAL_DIST",
            name = "Binomial Distribution",
            stdClass = 12,
            weightageCetMarks = 4,
            description = "Bernoulli trials, binomial probability distribution, mean and variance of binomial distribution.",
            icon = "🎲",
            subtopics = listOf("Bernoulli Trials", "Binomial Distribution P(X=r)", "Mean = np, Variance = npq")
        )
    )

    val ALL_CHAPTERS = STD_XI_CHAPTERS + STD_XII_CHAPTERS

    fun getChapterByName(name: String): ChapterMetadata? {
        return ALL_CHAPTERS.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
