package com.example.data.repository

import kotlin.math.abs
import kotlin.math.sqrt

data class QuadraticResult(
    val a: Double,
    val b: Double,
    val c: Double,
    val discriminant: Double,
    val nature: String,
    val root1: Double?,
    val root2: Double?,
    val isReal: Boolean,
    val steps: List<String>
)

data class OhmResult(
    val voltage: Double,
    val current: Double,
    val resistance: Double,
    val powerWatts: Double,
    val steps: List<String>
)

data class LensResult(
    val focalLength: Double,
    val objectDistance: Double,
    val imageDistance: Double,
    val magnification: Double,
    val isReal: Boolean,
    val isInverted: Boolean,
    val sizeDescription: String,
    val steps: List<String>
)

object FormulaSolvers {

    fun solveQuadratic(a: Double, b: Double, c: Double): QuadraticResult {
        if (a == 0.0) {
            return QuadraticResult(
                a, b, c, 0.0,
                "Invalid Quadratic: Coefficient 'a' cannot be zero (this is a linear equation).",
                null, null, false,
                listOf("Since a = 0, the equation reduces to ${b}x + ${c} = 0, which is linear.")
            )
        }

        val d = (b * b) - (4 * a * c)
        val steps = mutableListOf<String>()
        steps.add("Step 1: Identify coefficients: a = $a, b = $b, c = $c")
        steps.add("Step 2: Calculate Discriminant D = b² - 4ac = ($b)² - 4($a)($c) = ${b * b} - ${4 * a * c} = $d")

        return when {
            d > 0 -> {
                val sqrtD = sqrt(d)
                val r1 = (-b + sqrtD) / (2 * a)
                val r2 = (-b - sqrtD) / (2 * a)
                steps.add("Step 3: Since D > 0, there are two distinct real roots.")
                steps.add("Step 4: x = (-b ± √D)/(2a) = (-($b) ± $sqrtD) / (2×$a)")
                steps.add("Root 1: x₁ = ${"%.3f".format(r1)} | Root 2: x₂ = ${"%.3f".format(r2)}")
                QuadraticResult(a, b, c, d, "Two Distinct Real Roots", r1, r2, true, steps)
            }
            d == 0.0 -> {
                val r = -b / (2 * a)
                steps.add("Step 3: Since D = 0, there are two real and equal roots.")
                steps.add("Step 4: x = -b / (2a) = -($b) / (2×$a) = ${"%.3f".format(r)}")
                QuadraticResult(a, b, c, d, "Two Equal Real Roots (Repeated)", r, r, true, steps)
            }
            else -> {
                steps.add("Step 3: Since D < 0 ($d), √D is imaginary.")
                steps.add("Step 4: No real roots exist in the set of real numbers ℝ.")
                QuadraticResult(a, b, c, d, "No Real Roots (Complex Conjugates)", null, null, false, steps)
            }
        }
    }

    fun solveOhm(v: Double?, i: Double?, r: Double?): OhmResult {
        val steps = mutableListOf<String>()
        var calcV = v ?: 0.0
        var calcI = i ?: 0.0
        var calcR = r ?: 0.0

        if (v == null && i != null && r != null) {
            calcV = i * r
            steps.add("Using V = I × R: V = $i A × $r Ω = $calcV V")
        } else if (i == null && v != null && r != null) {
            calcI = if (r != 0.0) v / r else 0.0
            steps.add("Using I = V / R: I = $v V / $r Ω = $calcI A")
        } else if (r == null && v != null && i != null) {
            calcR = if (i != 0.0) v / i else 0.0
            steps.add("Using R = V / I: R = $v V / $i A = $calcR Ω")
        } else {
            steps.add("Given V = $calcV V, I = $calcI A, R = $calcR Ω")
        }

        val power = calcV * calcI
        steps.add("Calculated Electric Power P = V × I = $calcV × $calcI = ${"%.2f".format(power)} Watts (W)")

        return OhmResult(calcV, calcI, calcR, power, steps)
    }

    fun solveLens(f: Double, u: Double): LensResult {
        val steps = mutableListOf<String>()
        steps.add("Step 1: Lens Formula: 1/f = 1/v - 1/u => 1/v = 1/f + 1/u")
        steps.add("Step 2: Given focal length f = $f cm, object distance u = $u cm (Cartesian sign convention)")

        val denominator = u + f
        if (denominator == 0.0) {
            steps.add("Object placed exactly at focal point: rays become parallel, image at infinity (v = ∞).")
            return LensResult(f, u, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true, true, "Infinitely Large", steps)
        }

        val v = (u * f) / denominator
        val m = if (u != 0.0) v / u else 0.0
        val isReal = v > 0
        val isInverted = m < 0
        val sizeDesc = when {
            abs(m) > 1.05 -> "Magnified / Enlarged (|m| = ${"%.2f".format(abs(m))})"
            abs(m) < 0.95 -> "Diminished / Reduced (|m| = ${"%.2f".format(abs(m))})"
            else -> "Same Size as Object (|m| = 1.0)"
        }

        steps.add("Step 3: 1/v = 1/$f + 1/($u) => v = (${u} × ${f}) / (${u} + ${f}) = ${"%.2f".format(v)} cm")
        steps.add("Step 4: Linear Magnification m = v / u = ${"%.2f".format(v)} / ${u} = ${"%.2f".format(m)}")
        steps.add("Step 5: Nature of image: ${if (isReal) "Real and Inverted" else "Virtual and Erect"}, $sizeDesc")

        return LensResult(f, u, v, m, isReal, isInverted, sizeDesc, steps)
    }
}
