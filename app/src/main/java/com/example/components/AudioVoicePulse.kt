package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FarmGreenPrimary
import com.example.ui.theme.HarvestGold

@Composable
fun AudioVoicePulse(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "voice_pulse")

    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.55f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_1"
    )

    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.85f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_2"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(140.dp)
    ) {
        if (isListening) {
            // Outer Wave 2
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulseScale2)
                    .clip(CircleShape)
                    .background(HarvestGold.copy(alpha = 0.2f))
            )
            // Outer Wave 1
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(pulseScale1)
                    .clip(CircleShape)
                    .background(FarmGreenPrimary.copy(alpha = 0.35f))
            )
        }

        // Center Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (isListening) listOf(HarvestGold, HarvestGold.copy(alpha = 0.8f))
                        else listOf(FarmGreenPrimary, Color(0xFF0F3820))
                    )
                )
                .clickable(onClick = onClick)
                .testTag("voice_assistant_mic_button")
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.Mic else Icons.Default.Mic,
                contentDescription = "Voice Input Microphone",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
