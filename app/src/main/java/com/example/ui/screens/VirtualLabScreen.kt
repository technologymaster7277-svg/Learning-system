package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LabSimulationType
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun VirtualLabScreen(
    modifier: Modifier = Modifier
) {
    var selectedSim by remember { mutableStateOf(LabSimulationType.PROJECTILE_MOTION) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Disclaimer & Info Header
        Surface(
            color = AsiCobalt.copy(alpha = 0.15f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Science, contentDescription = null, tint = AsiCobalt, modifier = Modifier.size(20.dp))
                Column {
                    Text(
                        text = "ASI VIRTUAL SCIENCE & VISUAL LAB",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp),
                        color = AsiCobalt
                    )
                    Text(
                        text = "Interactive parameter models for physics, chemistry & mathematics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Simulation Selector Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items(LabSimulationType.values()) { sim ->
                val isSelected = selectedSim == sim
                Surface(
                    color = if (isSelected) AsiCyan else AsiNavyCard,
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AsiCyan else AsiNavyBorder)
                    ),
                    modifier = Modifier.clickable { selectedSim = sim }
                ) {
                    Text(
                        text = sim.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSelected) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Simulation Container
        when (selectedSim) {
            LabSimulationType.PROJECTILE_MOTION -> ProjectileSimulationView()
            LabSimulationType.RAY_OPTICS -> RayOpticsSimulationView()
            LabSimulationType.OHMS_LAW -> OhmsLawSimulationView()
            LabSimulationType.BOHR_MODEL -> BohrModelSimulationView()
            LabSimulationType.QUADRATIC_GRAPH -> QuadraticGraphSimulationView()
        }
    }
}

// 1. PROJECTILE MECHANICS SIMULATION
@Composable
fun ProjectileSimulationView() {
    var velocity by remember { mutableFloatStateOf(30f) } // m/s
    var angleDeg by remember { mutableFloatStateOf(45f) } // degrees
    val g = 9.8f

    val angleRad = (angleDeg * Math.PI / 180.0).toFloat()
    val maxHeight = (velocity * velocity * sin(angleRad) * sin(angleRad)) / (2 * g)
    val flightTime = (2 * velocity * sin(angleRad)) / g
    val range = (velocity * velocity * sin(2 * angleRad)) / g

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Interactive Trajectory Canvas",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(AsiNavyDarkest, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw ground line
                            drawLine(
                                color = AsiNavyBorder,
                                start = Offset(0f, h - 20f),
                                end = Offset(w, h - 20f),
                                strokeWidth = 3f
                            )

                            // Draw trajectory path
                            val path = Path()
                            val originX = 20f
                            val originY = h - 20f
                            path.moveTo(originX, originY)

                            val steps = 60
                            val scaleX = (w - 40f) / max(100f, range * 1.1f)
                            val scaleY = (h - 40f) / max(40f, maxHeight * 1.3f)

                            for (i in 0..steps) {
                                val t = (flightTime * i) / steps
                                val x = velocity * cos(angleRad) * t
                                val y = (velocity * sin(angleRad) * t) - (0.5f * g * t * t)
                                val canvasX = originX + (x * scaleX)
                                val canvasY = originY - (y * scaleY)
                                path.lineTo(canvasX, canvasY)
                            }

                            drawPath(
                                path = path,
                                color = AsiCyan,
                                style = Stroke(width = 4f)
                            )

                            // Launch angle vector
                            val vectorLen = 40f
                            val vecX = originX + vectorLen * cos(angleRad)
                            val vecY = originY - vectorLen * sin(angleRad)
                            drawLine(
                                color = AsiAmber,
                                start = Offset(originX, originY),
                                end = Offset(vecX, vecY),
                                strokeWidth = 3f
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Controls
                    Text("Launch Velocity: ${velocity.toInt()} m/s", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = velocity,
                        onValueChange = { velocity = it },
                        valueRange = 10f..60f,
                        colors = SliderDefaults.colors(thumbColor = AsiCyan, activeTrackColor = AsiCyan)
                    )

                    Text("Projection Angle: ${angleDeg.toInt()}°", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = angleDeg,
                        onValueChange = { angleDeg = it },
                        valueRange = 15f..75f,
                        colors = SliderDefaults.colors(thumbColor = AsiCyan, activeTrackColor = AsiCyan)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Real-time Physics Output
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Max Height (H)", fontSize = 11.sp, color = AsiTextSecondaryDark)
                                Text("${"%.1f".format(maxHeight)} m", fontWeight = FontWeight.Bold, color = AsiCyan)
                            }
                        }
                        Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Flight Time (T)", fontSize = 11.sp, color = AsiTextSecondaryDark)
                                Text("${"%.2f".format(flightTime)} s", fontWeight = FontWeight.Bold, color = AsiAmber)
                            }
                        }
                        Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Range (R)", fontSize = 11.sp, color = AsiTextSecondaryDark)
                                Text("${"%.1f".format(range)} m", fontWeight = FontWeight.Bold, color = AsiEmerald)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 2. RAY OPTICS CONVEX LENS SIMULATION
@Composable
fun RayOpticsSimulationView() {
    var f by remember { mutableFloatStateOf(15f) } // focal length (+15cm)
    var u by remember { mutableFloatStateOf(-30f) } // object distance (-30cm)

    // 1/v = 1/f + 1/u => v = (f * u) / (u + f)
    val v = if ((u + f) != 0f) (f * u) / (u + f) else 0f
    val m = if (u != 0f) v / u else 0f

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Convex Lens Optical Ray Tracing",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(AsiNavyDarkest, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val midX = w / 2f
                            val midY = h / 2f

                            // Principal Axis
                            drawLine(color = AsiNavyBorder, start = Offset(0f, midY), end = Offset(w, midY), strokeWidth = 2f)

                            // Convex Lens at center
                            drawLine(color = AsiCyan, start = Offset(midX, midY - 65f), end = Offset(midX, midY + 65f), strokeWidth = 5f)

                            val scale = 3.5f
                            // Focal points F1 and F2
                            val f1X = midX - f * scale
                            val f2X = midX + f * scale
                            drawCircle(color = AsiAmber, radius = 4f, center = Offset(f1X, midY))
                            drawCircle(color = AsiAmber, radius = 4f, center = Offset(f2X, midY))

                            // Object arrow (left of lens)
                            val objX = midX + u * scale
                            val objHeight = 40f
                            drawLine(color = AsiEmerald, start = Offset(objX, midY), end = Offset(objX, midY - objHeight), strokeWidth = 4f)

                            // Image arrow (right of lens or virtual)
                            val imgX = midX + v * scale
                            val imgHeight = objHeight * m
                            drawLine(color = AsiRuby, start = Offset(imgX, midY), end = Offset(imgX, midY + imgHeight), strokeWidth = 4f)

                            // Principal Ray 1 (parallel to axis, then through focus F2)
                            drawLine(color = AsiCobalt.copy(alpha = 0.7f), start = Offset(objX, midY - objHeight), end = Offset(midX, midY - objHeight), strokeWidth = 2f)
                            drawLine(color = AsiCobalt.copy(alpha = 0.7f), start = Offset(midX, midY - objHeight), end = Offset(imgX, midY + imgHeight), strokeWidth = 2f)

                            // Principal Ray 2 (straight through optical center)
                            drawLine(color = AsiCyan.copy(alpha = 0.7f), start = Offset(objX, midY - objHeight), end = Offset(imgX, midY + imgHeight), strokeWidth = 2f)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Object Distance u: ${u.toInt()} cm", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = u,
                        onValueChange = { u = it },
                        valueRange = -45f..-12f,
                        colors = SliderDefaults.colors(thumbColor = AsiEmerald, activeTrackColor = AsiEmerald)
                    )

                    Text("Focal Length f: ${f.toInt()} cm", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = f,
                        onValueChange = { f = it },
                        valueRange = 10f..25f,
                        colors = SliderDefaults.colors(thumbColor = AsiAmber, activeTrackColor = AsiAmber)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Image Distance (v) = ${"%.1f".format(v)} cm", fontWeight = FontWeight.Bold, color = AsiCyan)
                            Text("Magnification (m) = ${"%.2f".format(m)} (${if (m < 0) "Inverted, Real" else "Erect, Virtual"})", fontSize = 13.sp, color = AsiEmerald)
                        }
                    }
                }
            }
        }
    }
}

// 3. ELECTRIC CIRCUIT & OHM'S LAW SIMULATION
@Composable
fun OhmsLawSimulationView() {
    var voltage by remember { mutableFloatStateOf(12f) }
    var resistance by remember { mutableFloatStateOf(4f) }

    val current = if (resistance > 0f) voltage / resistance else 0f
    val power = voltage * current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Electric Circuit Schematic & Dynamic Bulb Glow",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(AsiNavyDarkest, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw closed rectangular wire loop
                            val left = 40f
                            val top = 30f
                            val right = w - 40f
                            val bottom = h - 30f

                            val wireColor = AsiNavyBorder
                            drawLine(color = wireColor, start = Offset(left, top), end = Offset(right, top), strokeWidth = 4f)
                            drawLine(color = wireColor, start = Offset(right, top), end = Offset(right, bottom), strokeWidth = 4f)
                            drawLine(color = wireColor, start = Offset(right, bottom), end = Offset(left, bottom), strokeWidth = 4f)
                            drawLine(color = wireColor, start = Offset(left, bottom), end = Offset(left, top), strokeWidth = 4f)

                            // Battery at left
                            val batY = (top + bottom) / 2f
                            drawLine(color = AsiCyan, start = Offset(left - 15f, batY - 15f), end = Offset(left + 15f, batY - 15f), strokeWidth = 5f)
                            drawLine(color = AsiCyan, start = Offset(left - 8f, batY + 15f), end = Offset(left + 8f, batY + 15f), strokeWidth = 3f)

                            // Resistor at bottom
                            val midBotX = (left + right) / 2f
                            drawRect(color = AsiCobalt, topLeft = Offset(midBotX - 25f, bottom - 10f), size = androidx.compose.ui.geometry.Size(50f, 20f))

                            // Light Bulb at top
                            val midTopX = (left + right) / 2f
                            val glowAlpha = (current / 8f).coerceIn(0.1f, 1f)
                            // Outer glow circle
                            drawCircle(
                                color = Color(0xFFFFD54F).copy(alpha = glowAlpha * 0.4f),
                                radius = 28f + (current * 4f).coerceAtMost(30f),
                                center = Offset(midTopX, top)
                            )
                            // Bulb glass
                            drawCircle(
                                color = Color(0xFFFFEB3B).copy(alpha = glowAlpha.coerceAtLeast(0.3f)),
                                radius = 18f,
                                center = Offset(midTopX, top)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Potential Difference V: ${voltage.toInt()} Volts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = voltage,
                        onValueChange = { voltage = it },
                        valueRange = 0f..24f,
                        colors = SliderDefaults.colors(thumbColor = AsiCyan, activeTrackColor = AsiCyan)
                    )

                    Text("Resistance R: ${resistance.toInt()} Ohms (Ω)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Slider(
                        value = resistance,
                        onValueChange = { resistance = it },
                        valueRange = 1f..16f,
                        colors = SliderDefaults.colors(thumbColor = AsiCobalt, activeTrackColor = AsiCobalt)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Current I = V/R", fontSize = 11.sp, color = AsiTextSecondaryDark)
                                Text("${"%.2f".format(current)} A", fontWeight = FontWeight.Bold, color = AsiCyan)
                            }
                        }
                        Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Electric Power P = VI", fontSize = 11.sp, color = AsiTextSecondaryDark)
                                Text("${"%.1f".format(power)} Watts", fontWeight = FontWeight.Bold, color = AsiAmber)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 4. BOHR ATOMIC ORBITAL SIMULATION
@Composable
fun BohrModelSimulationView() {
    val elements = listOf(
        Triple("Hydrogen", 1, listOf(1)),
        Triple("Helium", 2, listOf(2)),
        Triple("Carbon", 6, listOf(2, 4)),
        Triple("Sodium", 11, listOf(2, 8, 1))
    )
    var selectedElemIndex by remember { mutableIntStateOf(3) } // Sodium default
    val elem = elements[selectedElemIndex]

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bohr Atomic Shell Configuration",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Element selector row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        elements.forEachIndexed { idx, (name, z, _) ->
                            val isSel = selectedElemIndex == idx
                            Surface(
                                color = if (isSel) AsiCyan else AsiNavyDarkest,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedElemIndex = idx }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isSel) AsiNavyDarkest else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        "Z=$z",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSel) AsiNavyDarkest else AsiTextSecondaryDark
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(AsiNavyDarkest, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Nucleus (+Z)
                            drawCircle(color = AsiRuby, radius = 14f, center = center)

                            val shellRadii = listOf(40f, 65f, 90f)
                            val shells = elem.third

                            shells.forEachIndexed { shellIndex, electrons ->
                                val r = shellRadii[shellIndex]
                                // Orbit ring
                                drawCircle(
                                    color = AsiCobalt.copy(alpha = 0.5f),
                                    radius = r,
                                    center = center,
                                    style = Stroke(width = 1.5f)
                                )

                                // Electron dots evenly distributed
                                for (e in 0 until electrons) {
                                    val angle = (2 * Math.PI * e / electrons).toFloat()
                                    val ex = center.x + r * cos(angle)
                                    val ey = center.y + r * sin(angle)
                                    drawCircle(color = AsiCyan, radius = 4f, center = Offset(ex, ey))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("${elem.first} (Z = ${elem.second})", fontWeight = FontWeight.Bold, color = AsiCyan)
                            Text("Electronic Configuration: ${elem.third.joinToString(" , ")} (K, L, M shells)", color = AsiEmerald, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

// 5. QUADRATIC PARABOLA FUNCTION GRAPHER
@Composable
fun QuadraticGraphSimulationView() {
    var a by remember { mutableFloatStateOf(1f) }
    var b by remember { mutableFloatStateOf(-2f) }
    var c by remember { mutableFloatStateOf(-3f) }

    val d = (b * b) - (4 * a * c)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AsiNavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quadratic Parabola Visualizer: y = ax² + bx + c",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AsiCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(AsiNavyDarkest, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val origin = Offset(w / 2f, h / 2f)

                            // Cartesian Grid lines
                            drawLine(color = AsiNavyBorder, start = Offset(0f, origin.y), end = Offset(w, origin.y), strokeWidth = 2f)
                            drawLine(color = AsiNavyBorder, start = Offset(origin.x, 0f), end = Offset(origin.x, h), strokeWidth = 2f)

                            val scale = 14f
                            val path = Path()

                            val xMin = -10f
                            val xMax = 10f
                            val numPoints = 80

                            var first = true
                            for (i in 0..numPoints) {
                                val xVal = xMin + (xMax - xMin) * (i.toFloat() / numPoints)
                                val yVal = a * xVal * xVal + b * xVal + c
                                val screenX = origin.x + xVal * scale
                                val screenY = origin.y - yVal * scale

                                if (first) {
                                    path.moveTo(screenX, screenY)
                                    first = false
                                } else {
                                    path.lineTo(screenX, screenY)
                                }
                            }

                            drawPath(path = path, color = AsiCyan, style = Stroke(width = 3.5f))

                            // Vertex (-b/2a, -D/4a)
                            val vx = -b / (2 * a)
                            val vy = a * vx * vx + b * vx + c
                            val screenVx = origin.x + vx * scale
                            val screenVy = origin.y - vy * scale
                            drawCircle(color = AsiAmber, radius = 5f, center = Offset(screenVx, screenVy))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Coefficient a (curvature): ${"%.1f".format(a)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Slider(value = a, onValueChange = { a = if (it != 0f) it else 0.5f }, valueRange = -3f..3f)

                    Text("Coefficient b: ${"%.1f".format(b)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Slider(value = b, onValueChange = { b = it }, valueRange = -6f..6f)

                    Text("Constant c: ${"%.1f".format(c)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Slider(value = c, onValueChange = { c = it }, valueRange = -6f..6f)

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(color = AsiNavyCardElevated, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("D = b² - 4ac = ${"%.2f".format(d)}", fontWeight = FontWeight.Bold, color = AsiCyan)
                            Text(
                                text = if (d > 0) "Parabola cuts x-axis at 2 real roots" else if (d == 0f) "Parabola touches x-axis at 1 point (vertex)" else "Parabola does not intersect x-axis (no real roots)",
                                color = if (d >= 0) AsiEmerald else AsiRuby,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
