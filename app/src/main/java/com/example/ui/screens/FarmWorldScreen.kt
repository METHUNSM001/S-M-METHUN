package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.DigitalTwinCanvas
import com.example.components.StatusBadge
import com.example.data.local.entities.FarmZoneEntity
import com.example.engine.HealthStatus
import com.example.i18n.Translations
import com.example.ui.AgriTwinUiState
import com.example.ui.theme.FarmGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.SkyWaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmWorldScreen(
    uiState: AgriTwinUiState,
    onToggle3DMode: () -> Unit,
    onSelectZone: (FarmZoneEntity?) -> Unit,
    onNavigateToSimulator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.currentLanguage
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var rotationDeg by remember { mutableFloatStateOf(0f) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1B261D))
    ) {
        // Interactive 3D / 2D Digital Twin Canvas
        DigitalTwinCanvas(
            zones = uiState.zones,
            is3DMode = uiState.is3DMode,
            isRainExpected = uiState.decision?.isRainExpected ?: true,
            rotationDeg = rotationDeg,
            zoomScale = zoomScale,
            selectedZoneId = uiState.selectedZone?.id,
            onZoneSelected = { zone -> onSelectZone(zone) }
        )

        // Top Control Overlay: Header & 3D/2D Toggle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC000000)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🌍", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = Translations.get("digital_twin_title", lang),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.farm?.areaAcres ?: 2.0} Acres • ${uiState.farm?.primaryCrop ?: "Tomato"}",
                                color = Color(0xFFA5D6A7),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // 3D / 2D Mode Switch Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = FarmGreenPrimary,
                    modifier = Modifier
                        .clickable { onToggle3DMode() }
                        .testTag("toggle_3d_mode_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = "Toggle 3D/2D",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uiState.is3DMode) Translations.get("mode_3d", lang)
                            else Translations.get("mode_2d", lang),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hint Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x99000000),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "👆 " + Translations.get("tap_zone_hint", lang),
                    color = Color(0xFFFFD54F),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // Bottom Controls: Zoom Slider + What-If Simulation Trigger
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Zoom & Controls Bar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xCC000000),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔍 Zoom", color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = zoomScale,
                        onValueChange = { zoomScale = it },
                        valueRange = 0.8f..1.5f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = HarvestGold,
                            activeTrackColor = FarmGreenPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { zoomScale = 1.0f; rotationDeg = 0f }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset View",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // WHAT-IF SIMULATOR CTA BUTTON
            Button(
                onClick = onNavigateToSimulator,
                colors = ButtonDefaults.buttonColors(containerColor = HarvestGold),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("open_what_if_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Simulator",
                    tint = Color(0xFF1B261D)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Translations.get("what_if_title", lang),
                    color = Color(0xFF1B261D),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Zone Inspection Bottom Sheet Dialog when a zone is tapped
        if (uiState.selectedZone != null) {
            val zone = uiState.selectedZone
            ModalBottomSheet(
                onDismissRequest = { onSelectZone(null) },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = zone.zoneName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${zone.cropName} • ${zone.cropStage}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        StatusBadge(
                            status = if (zone.healthStatus == "GOOD") HealthStatus.GOOD else HealthStatus.WATCH,
                            text = zone.healthStatus
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3 Metric Cards: Soil Moisture, Soil Temp, EC Level
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBox(
                            icon = "💧",
                            title = Translations.get("zone_moisture", lang),
                            value = "${zone.soilMoisturePercent}%",
                            color = SkyWaterBlue,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            icon = "🌡️",
                            title = Translations.get("zone_temp", lang),
                            value = "${zone.soilTemperatureC}°C",
                            color = HarvestGold,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            icon = "🧪",
                            title = Translations.get("zone_ec", lang),
                            value = "${zone.ecNutrientLevel} dS/m",
                            color = FarmGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Zone Specific Recommendation
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "💡 " + Translations.get("zone_recommendation", lang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmGreenPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = zone.recommendation,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { onSelectZone(null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "CLOSE", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    icon: String,
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
