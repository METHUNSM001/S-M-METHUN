package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StatusBadge
import com.example.engine.DiseaseRiskLevel
import com.example.engine.FarmDecisionSummary
import com.example.engine.HealthStatus
import com.example.engine.WaterStatus
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.AgriTwinUiState
import com.example.ui.theme.FarmGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.SkyWaterBlue
import com.example.ui.theme.StatusActionRed
import com.example.ui.theme.StatusGoodGreen
import com.example.ui.theme.StatusWatchAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    uiState: AgriTwinUiState,
    onNavigateToTwin: () -> Unit,
    onNavigateToVoice: (String?) -> Unit,
    onNavigateToSchemes: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.currentLanguage
    val decision = uiState.decision

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header with Greeting and Language Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Translations.get("good_morning", lang),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = uiState.farm?.name ?: "Muthu's Green Acres",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Quick Language Selector Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AppLanguage.entries.forEach { appLang ->
                        val isSelected = appLang == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) FarmGreenPrimary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { onLanguageSelected(appLang) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("lang_chip_${appLang.code}")
                        ) {
                            Text(
                                text = appLang.nativeName,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // BIG TODAY'S ACTION BANNER (The #1 Farmer Decision)
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (decision?.waterNeed == WaterStatus.NOT_NEEDED) Color(0xFF1B5E20)
                    else Color(0xFFE65100)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("todays_action_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📢", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Translations.get("todays_action", lang),
                                color = Color(0xFFFFD54F),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        StatusBadge(
                            status = if (decision?.waterNeed == WaterStatus.NOT_NEEDED) HealthStatus.GOOD else HealthStatus.ACTION_NEEDED,
                            text = if (decision?.waterNeed == WaterStatus.NOT_NEEDED) "SAVE WATER 💧" else "ACTION ⚡"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = decision?.todaysActionDetail
                            ?: Translations.get("action_dont_water", lang),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x33000000))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💡", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Decision Engine: Live Sensor + ICAR Rain Forecast",
                            color = Color(0xFFE0E0E0),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // BIG VOICE MIC CARD ("ASK MY FARM")
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(width = 1.5.dp, brush = Brush.linearGradient(listOf(FarmGreenPrimary, HarvestGold))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToVoice(null) }
                    .testTag("ask_my_farm_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(FarmGreenPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Assistant",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = Translations.get("ask_my_farm", lang),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = Translations.get("speak_prompt", lang),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Voice",
                        tint = FarmGreenPrimary
                    )
                }
            }
        }

        // Quick Voice Sample Chips
        item {
            Column {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "குரல் மூலம் கேட்கலாம்:"
                    else if (lang == AppLanguage.HINDI) "आवाज़ से पूछें:"
                    else "Quick Voice Prompts:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val prompt1 = Translations.get("chip_water_today", lang)
                    val prompt2 = Translations.get("chip_rain_tomorrow", lang)
                    val prompt3 = Translations.get("chip_crop_health", lang)
                    val prompt4 = Translations.get("chip_schemes", lang)

                    listOf(prompt1, prompt2, prompt3, prompt4).forEach { prompt ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onNavigateToVoice(prompt) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = "🎙️", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = prompt,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 10-SECOND STATUS GRID: 4 KEY CARDS
        item {
            Text(
                text = Translations.get("how_is_farm", lang),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Row 1: Farm Health & Weather
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1: Farm Health
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToTwin() }
                        .testTag("card_farm_health"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🌾", fontSize = 24.sp)
                            StatusBadge(
                                status = HealthStatus.GOOD,
                                text = "${decision?.healthScore ?: 88}%"
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = Translations.get("farm_health", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Translations.get("status_good", lang),
                            fontSize = 13.sp,
                            color = StatusGoodGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Card 2: Weather
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("card_weather"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🌦️", fontSize = 24.sp)
                            Text(
                                text = "${uiState.weather?.temperatureC ?: 28.5}°C",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = Translations.get("weather", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = decision?.rainSummary ?: Translations.get("rain_likely", lang),
                            fontSize = 12.sp,
                            color = SkyWaterBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Row 2: Water Status & Disease Risk
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 3: Water Status
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("card_water"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "💧", fontSize = 24.sp)
                            StatusBadge(
                                status = HealthStatus.GOOD,
                                text = "OFF"
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = Translations.get("water_status", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Translations.get("water_not_needed", lang),
                            fontSize = 12.sp,
                            color = StatusGoodGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Card 4: Disease Risk
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("card_disease"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🦠", fontSize = 24.sp)
                            StatusBadge(
                                status = if (decision?.diseaseRisk == DiseaseRiskLevel.LOW) HealthStatus.GOOD else HealthStatus.WATCH,
                                text = if (decision?.diseaseRisk == DiseaseRiskLevel.LOW) "LOW" else "WATCH"
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = Translations.get("disease_risk", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Translations.get("low_risk", lang),
                            fontSize = 12.sp,
                            color = if (decision?.diseaseRisk == DiseaseRiskLevel.LOW) StatusGoodGreen else StatusWatchAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // DIGITAL TWIN 3D WORLD ENTRY BANNER
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2818)),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTwin() }
                    .testTag("open_twin_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌍", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Translations.get("digital_twin_title", lang),
                                color = Color(0xFFFFD54F),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Translations.get("digital_twin_subtitle", lang),
                            color = Color(0xFFC8E6C9),
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onNavigateToTwin,
                        colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "EXPLORE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // GOVERNMENT SCHEME TEASER BANNER
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSchemes() }
                    .testTag("home_schemes_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🏛️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("schemes_title", lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "PM-KISAN ₹6,000 & 100% Drip Subsidy Matched",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "View Schemes",
                        tint = FarmGreenPrimary
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
