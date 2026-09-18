package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * LaTeX and Mathematical equation formatter.
 * Converts raw LaTeX notations (e.g. \int, \frac{a}{b}, \lim, \sqrt{}, x^2, \theta)
 * into clean, high-legibility rendered mathematical symbols with dedicated syntax styling.
 */
@Composable
fun MathFormulaRenderer(
    rawFormula: String,
    modifier: Modifier = Modifier,
    isDark: Boolean = ThemeController.isDarkTheme,
    fontSize: Int = 15
) {
    val formatted = formatLatexToReadableMath(rawFormula)

    Surface(
        color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDark) DarkSpectrumBorder else LightSpectrumBorder
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatted,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize + 8).sp,
                    letterSpacing = 0.5.sp
                ),
                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
            )
        }
    }
}

fun formatLatexToReadableMath(input: String): String {
    var result = input
        .replace("\\frac", "")
        .replace("\\cdot", " · ")
        .replace("\\times", " × ")
        .replace("\\div", " ÷ ")
        .replace("\\pm", " ± ")
        .replace("\\mp", " ∓ ")
        .replace("\\int", "∫")
        .replace("\\iint", "∬")
        .replace("\\sum", "∑")
        .replace("\\prod", "∏")
        .replace("\\lim", "lim")
        .replace("\\to", " → ")
        .replace("\\rightarrow", " → ")
        .replace("\\infty", "∞")
        .replace("\\sqrt", "√")
        .replace("\\alpha", "α")
        .replace("\\beta", "β")
        .replace("\\gamma", "γ")
        .replace("\\theta", "θ")
        .replace("\\phi", "φ")
        .replace("\\pi", "π")
        .replace("\\lambda", "λ")
        .replace("\\Delta", "Δ")
        .replace("\\delta", "δ")
        .replace("\\sigma", "σ")
        .replace("\\omega", "ω")
        .replace("\\le", " ≤ ")
        .replace("\\leq", " ≤ ")
        .replace("\\ge", " ≥ ")
        .replace("\\geq", " ≥ ")
        .replace("\\neq", " ≠ ")
        .replace("\\approx", " ≈ ")
        .replace("\\in", " ∈ ")
        .replace("\\subset", " ⊂ ")
        .replace("\\cup", " ∪ ")
        .replace("\\cap", " ∩ ")
        .replace("\\emptyset", "∅")
        .replace("\\mathbf", "")
        .replace("\\text", "")
        .replace("\\sin", "sin")
        .replace("\\cos", "cos")
        .replace("\\tan", "tan")
        .replace("\\cot", "cot")
        .replace("\\sec", "sec")
        .replace("\\csc", "csc")
        .replace("\\ln", "ln")
        .replace("\\log", "log")
        .replace("{", "")
        .replace("}", "")

    // Replace basic exponents ^2, ^3
    result = result
        .replace("^2", "²")
        .replace("^3", "³")
        .replace("^4", "⁴")
        .replace("^n", "ⁿ")
        .replace("^x", "ˣ")
        .replace("^-1", "⁻¹")

    return result.trim()
}
