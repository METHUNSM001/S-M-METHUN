package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HealthStatus
import com.example.ui.theme.StatusActionBg
import com.example.ui.theme.StatusActionRed
import com.example.ui.theme.StatusGoodBg
import com.example.ui.theme.StatusGoodGreen
import com.example.ui.theme.StatusWatchAmber
import com.example.ui.theme.StatusWatchBg

@Composable
fun StatusBadge(
    status: HealthStatus,
    text: String,
    modifier: Modifier = Modifier
) {
    val (bg, textColor, dotColor, icon) = when (status) {
        HealthStatus.GOOD -> Quadruple(StatusGoodBg, StatusGoodGreen, StatusGoodGreen, "🟢")
        HealthStatus.WATCH -> Quadruple(StatusWatchBg, StatusWatchAmber, StatusWatchAmber, "🟡")
        HealthStatus.ACTION_NEEDED -> Quadruple(StatusActionBg, StatusActionRed, StatusActionRed, "🔴")
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
