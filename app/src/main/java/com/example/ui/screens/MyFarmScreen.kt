package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StatusBadge
import com.example.engine.HealthStatus
import com.example.i18n.Translations
import com.example.ui.AgriTwinUiState
import com.example.ui.theme.FarmGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.SkyWaterBlue

@Composable
fun MyFarmScreen(
    uiState: AgriTwinUiState,
    onNavigateToTwin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.currentLanguage
    val farm = uiState.farm

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Translations.get("nav_farm", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = farm?.name ?: "Muthu's Green Acres",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(
                    status = HealthStatus.GOOD,
                    text = "${farm?.healthScore ?: 86}% HEALTH"
                )
            }
        }

        // Primary Farm Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("primary_farm_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🌾 " + (farm?.name ?: "Muthu's Farm"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "📍 " + (farm?.locationName ?: "Madurai, Tamil Nadu"),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FarmSpecChip("Area", "${farm?.areaAcres ?: 2.0} Acres", modifier = Modifier.weight(1f))
                        FarmSpecChip("Crop", farm?.primaryCrop ?: "Tomato", modifier = Modifier.weight(1f))
                        FarmSpecChip("Method", "Drip System", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Variety: " + (farm?.cropVariety ?: "Shivam Hybrid (PKM-1)"),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Current Stage: " + (farm?.cropStage ?: "Flowering & Fruit Setting"),
                        fontSize = 13.sp,
                        color = FarmGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Soil: " + (farm?.soilType ?: "Red Loamy Clay"),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Field Zones List
        item {
            Text(
                text = "🌱 Field Zones & Sensor Telemetry",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(uiState.zones.size) { index ->
            val zone = uiState.zones[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = zone.zoneName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        StatusBadge(
                            status = if (zone.healthStatus == "GOOD") HealthStatus.GOOD else HealthStatus.WATCH,
                            text = zone.healthStatus
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${zone.cropName} • ${zone.cropStage}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "💧 Moisture: ${zone.soilMoisturePercent}%", fontSize = 12.sp, color = SkyWaterBlue, fontWeight = FontWeight.Bold)
                        Text(text = "🌡️ Temp: ${zone.soilTemperatureC}°C", fontSize = 12.sp, color = HarvestGold, fontWeight = FontWeight.Bold)
                        Text(text = "🧪 EC: ${zone.ecNutrientLevel}", fontSize = 12.sp, color = FarmGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Open Farm World 3D
        item {
            Button(
                onClick = onNavigateToTwin,
                colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "🌍 Open Farm World 3D", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FarmSpecChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
        }
    }
}
