package com.example.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.data.local.entities.FarmZoneEntity
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DigitalTwinCanvas(
    zones: List<FarmZoneEntity>,
    is3DMode: Boolean,
    isRainExpected: Boolean,
    rotationDeg: Float,
    zoomScale: Float,
    selectedZoneId: String?,
    onZoneSelected: (FarmZoneEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "twin_anim")

    // Rain drop animation offset
    val rainAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain_anim"
    )

    // Sensor pulse glow
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(zones, is3DMode) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()

                    // Zone hit testing based on normalized screen regions
                    val relX = tapOffset.x / w
                    val relY = tapOffset.y / h

                    val hitZone = when {
                        relX < 0.5f && relY < 0.55f -> zones.find { it.id == "zone_a" } ?: zones.getOrNull(0)
                        relX >= 0.5f && relY < 0.55f -> zones.find { it.id == "zone_b" } ?: zones.getOrNull(1)
                        else -> zones.find { it.id == "zone_c" } ?: zones.getOrNull(2)
                    }
                    if (hitZone != null) {
                        onZoneSelected(hitZone)
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height

        // 1. Sky & Atmosphere Background Gradient
        val skyGradient = if (isRainExpected) {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF37474F), // Overcast Dark Grey
                    Color(0xFF546E7A),
                    Color(0xFFB0BEC5)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF81D4FA), // Sunny Morning Sky
                    Color(0xFFE1F5FE),
                    Color(0xFFF1F8E9)
                )
            )
        }
        drawRect(brush = skyGradient, size = Size(w, h))

        // 2. Weather particles (Rain if expected, Sun rays if sunny)
        if (isRainExpected) {
            drawRainParticles(w, h, rainAnim)
        } else {
            drawSunRays(w, h, pulseAnim)
        }

        // 3. Ground Terrain & Digital Twin Grid
        if (is3DMode) {
            draw3DIsometricFarm(
                w = w,
                h = h,
                zones = zones,
                selectedZoneId = selectedZoneId,
                pulseAnim = pulseAnim,
                rotationDeg = rotationDeg,
                zoomScale = zoomScale
            )
        } else {
            draw2DTopDownFarm(
                w = w,
                h = h,
                zones = zones,
                selectedZoneId = selectedZoneId,
                pulseAnim = pulseAnim
            )
        }
    }
}

private fun DrawScope.drawRainParticles(w: Float, h: Float, anim: Float) {
    val rainColor = Color(0x99B0BEC5)
    for (i in 0..40) {
        val startX = (i * 37f) % w
        val startY = ((i * 53f) + anim * h) % h
        drawLine(
            color = rainColor,
            start = Offset(startX, startY),
            end = Offset(startX - 8f, startY + 24f),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawSunRays(w: Float, h: Float, pulse: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x66FFE082), Color(0x00FFE082)),
            center = Offset(w * 0.85f, h * 0.15f),
            radius = 160f * pulse
        ),
        center = Offset(w * 0.85f, h * 0.15f),
        radius = 160f * pulse
    )
    drawCircle(
        color = Color(0xFFFFD54F),
        center = Offset(w * 0.85f, h * 0.15f),
        radius = 28f
    )
}

private fun DrawScope.draw3DIsometricFarm(
    w: Float,
    h: Float,
    zones: List<FarmZoneEntity>,
    selectedZoneId: String?,
    pulseAnim: Float,
    rotationDeg: Float,
    zoomScale: Float
) {
    val cx = w / 2f
    val cy = h * 0.58f

    val baseWidth = (w * 0.85f) * zoomScale
    val baseHeight = (h * 0.38f) * zoomScale

    // Earth Base Depth Layer (Isometric 3D extrusion)
    val depthPath = Path().apply {
        moveTo(cx - baseWidth / 2, cy)
        lineTo(cx, cy + baseHeight / 2)
        lineTo(cx + baseWidth / 2, cy)
        lineTo(cx + baseWidth / 2, cy + 32f)
        lineTo(cx, cy + baseHeight / 2 + 32f)
        lineTo(cx - baseWidth / 2, cy + 32f)
        close()
    }
    drawPath(depthPath, color = Color(0xFF4E342E)) // Deep soil brown

    // Top Isometric Surface
    val groundPath = Path().apply {
        moveTo(cx, cy - baseHeight / 2)
        lineTo(cx + baseWidth / 2, cy)
        lineTo(cx, cy + baseHeight / 2)
        lineTo(cx - baseWidth / 2, cy)
        close()
    }
    drawPath(groundPath, color = Color(0xFF2E7D32)) // Lush farm base

    // Zone A: East Field (Flowering Tomato) - Top Quadrant
    val zoneAPath = Path().apply {
        moveTo(cx, cy - baseHeight / 2)
        lineTo(cx + baseWidth / 4, cy - baseHeight / 4)
        lineTo(cx, cy)
        lineTo(cx - baseWidth / 4, cy - baseHeight / 4)
        close()
    }
    val isZoneASel = selectedZoneId == "zone_a"
    drawPath(
        zoneAPath,
        color = if (isZoneASel) Color(0xFF43A047) else Color(0xFF388E3C)
    )
    drawPath(
        zoneAPath,
        color = if (isZoneASel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        style = Stroke(width = if (isZoneASel) 4f else 2f)
    )

    // Zone A Crop Rows (Flowering dots)
    for (r in -2..2) {
        val rx = cx + (r * 18f)
        val ry = (cy - baseHeight / 4) + (r * 8f)
        drawCircle(color = Color(0xFFFFEB3B), center = Offset(rx, ry), radius = 4f)
        drawCircle(color = Color(0xFF1B5E20), center = Offset(rx + 6f, ry + 3f), radius = 6f)
    }

    // Zone B: West Field (Vegetative) - Right Quadrant
    val zoneBPath = Path().apply {
        moveTo(cx + baseWidth / 4, cy - baseHeight / 4)
        lineTo(cx + baseWidth / 2, cy)
        lineTo(cx + baseWidth / 4, cy + baseHeight / 4)
        lineTo(cx, cy)
        close()
    }
    val isZoneBSel = selectedZoneId == "zone_b"
    drawPath(
        zoneBPath,
        color = if (isZoneBSel) Color(0xFF2E7D32) else Color(0xFF1B5E20)
    )
    drawPath(
        zoneBPath,
        color = if (isZoneBSel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        style = Stroke(width = if (isZoneBSel) 4f else 2f)
    )

    // Zone C: Lowland Irrigation & Drip Block - Bottom Quadrant
    val zoneCPath = Path().apply {
        moveTo(cx - baseWidth / 4, cy - baseHeight / 4)
        lineTo(cx, cy)
        lineTo(cx + baseWidth / 4, cy + baseHeight / 4)
        lineTo(cx, cy + baseHeight / 2)
        lineTo(cx - baseWidth / 2, cy)
        close()
    }
    val isZoneCSel = selectedZoneId == "zone_c"
    drawPath(
        zoneCPath,
        color = if (isZoneCSel) Color(0xFF33691E) else Color(0xFF558B2F)
    )
    drawPath(
        zoneCPath,
        color = if (isZoneCSel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        style = Stroke(width = if (isZoneCSel) 4f else 2f)
    )

    // Drip Line Pipes in Zone C (Blue lines)
    drawLine(
        color = Color(0xFF0288D1),
        start = Offset(cx - baseWidth / 4, cy + 10f),
        end = Offset(cx, cy + baseHeight / 4 + 10f),
        strokeWidth = 3.5f,
        cap = StrokeCap.Round
    )

    // Farm House / Pump Shed (Isometric Cube)
    val shedX = cx - baseWidth * 0.32f
    val shedY = cy - 25f
    drawRect(
        color = Color(0xFF8D6E63),
        topLeft = Offset(shedX, shedY),
        size = Size(36f, 28f)
    )
    // Shed Roof
    val roofPath = Path().apply {
        moveTo(shedX - 4f, shedY)
        lineTo(shedX + 18f, shedY - 14f)
        lineTo(shedX + 40f, shedY)
        close()
    }
    drawPath(roofPath, color = Color(0xFFD84315))

    // IoT Sensor Beacon in center with glowing pulse
    drawCircle(
        color = Color(0xFF00E676).copy(alpha = 0.4f * pulseAnim),
        center = Offset(cx, cy),
        radius = 18f * pulseAnim
    )
    drawCircle(
        color = Color(0xFF00E676),
        center = Offset(cx, cy),
        radius = 6f
    )
}

private fun DrawScope.draw2DTopDownFarm(
    w: Float,
    h: Float,
    zones: List<FarmZoneEntity>,
    selectedZoneId: String?,
    pulseAnim: Float
) {
    val pad = 36f
    val mapWidth = w - (pad * 2)
    val mapHeight = h * 0.6f
    val topY = h * 0.18f

    // 2D Farm Outer Boundary (Wood fenced appearance)
    drawRect(
        color = Color(0xFF795548),
        topLeft = Offset(pad - 6f, topY - 6f),
        size = Size(mapWidth + 12f, mapHeight + 12f),
        style = Stroke(width = 8f)
    )

    // Zone A: East Field (Top Half)
    val isZoneASel = selectedZoneId == "zone_a"
    drawRect(
        color = if (isZoneASel) Color(0xFF43A047) else Color(0xFF388E3C),
        topLeft = Offset(pad, topY),
        size = Size(mapWidth / 2, mapHeight * 0.55f)
    )
    drawRect(
        color = if (isZoneASel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        topLeft = Offset(pad, topY),
        size = Size(mapWidth / 2, mapHeight * 0.55f),
        style = Stroke(width = if (isZoneASel) 4f else 2f)
    )

    // Zone B: West Field (Top Right)
    val isZoneBSel = selectedZoneId == "zone_b"
    drawRect(
        color = if (isZoneBSel) Color(0xFF2E7D32) else Color(0xFF1B5E20),
        topLeft = Offset(pad + mapWidth / 2, topY),
        size = Size(mapWidth / 2, mapHeight * 0.55f)
    )
    drawRect(
        color = if (isZoneBSel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        topLeft = Offset(pad + mapWidth / 2, topY),
        size = Size(mapWidth / 2, mapHeight * 0.55f),
        style = Stroke(width = if (isZoneBSel) 4f else 2f)
    )

    // Zone C: Drip Irrigation Block (Bottom Full Width)
    val isZoneCSel = selectedZoneId == "zone_c"
    drawRect(
        color = if (isZoneCSel) Color(0xFF558B2F) else Color(0xFF33691E),
        topLeft = Offset(pad, topY + mapHeight * 0.55f),
        size = Size(mapWidth, mapHeight * 0.45f)
    )
    drawRect(
        color = if (isZoneCSel) Color(0xFFFFD54F) else Color(0x66FFFFFF),
        topLeft = Offset(pad, topY + mapHeight * 0.55f),
        size = Size(mapWidth, mapHeight * 0.45f),
        style = Stroke(width = if (isZoneCSel) 4f else 2f)
    )

    // Drip lines across Zone C
    for (i in 1..4) {
        val lineY = topY + (mapHeight * 0.55f) + (i * 24f)
        drawLine(
            color = Color(0xFF0288D1),
            start = Offset(pad + 16f, lineY),
            end = Offset(pad + mapWidth - 16f, lineY),
            strokeWidth = 3f
        )
    }

    // IoT Sensor Beacon in center
    val cx = pad + mapWidth / 2
    val cy = topY + mapHeight * 0.55f
    drawCircle(
        color = Color(0xFF00E676).copy(alpha = 0.4f * pulseAnim),
        center = Offset(cx, cy),
        radius = 24f * pulseAnim
    )
    drawCircle(
        color = Color(0xFF00E676),
        center = Offset(cx, cy),
        radius = 8f
    )
}
