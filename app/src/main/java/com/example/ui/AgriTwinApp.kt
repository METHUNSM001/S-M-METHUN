package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.i18n.Translations
import com.example.ui.navigation.AgriTwinBottomNavBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.AlertsScreen
import com.example.ui.screens.FarmWorldScreen
import com.example.ui.screens.FinanceAndSchemesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyFarmScreen
import com.example.ui.screens.ProfileAndSettingsScreen
import com.example.ui.screens.VoiceAssistantScreen
import com.example.ui.screens.WhatIfSimulatorScreen
import com.example.ui.theme.FarmGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.StatusActionRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTwinApp(
    viewModel: AgriTwinViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val lang = uiState.currentLanguage
    val unreadAlertsCount = uiState.alerts.size

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌱", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "AgriTwin AI",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Madurai • Tomato 2.0 Ac",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { currentScreen = Screen.Alerts },
                        modifier = Modifier.testTag("app_bar_notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadAlertsCount > 0) {
                                    Badge(
                                        containerColor = StatusActionRed,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = unreadAlertsCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            AgriTwinBottomNavBar(
                currentRoute = currentScreen.route,
                language = lang,
                onNavigate = { screen -> currentScreen = screen }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screen_crossfade"
            ) { screen ->
                when (screen) {
                    Screen.Home -> HomeScreen(
                        uiState = uiState,
                        onNavigateToTwin = { currentScreen = Screen.FarmWorld },
                        onNavigateToVoice = { prompt ->
                            currentScreen = Screen.VoiceAssistant
                            if (prompt != null) {
                                viewModel.askVoiceQuery(prompt)
                            }
                        },
                        onNavigateToSchemes = { currentScreen = Screen.Schemes },
                        onLanguageSelected = { appLang -> viewModel.setLanguage(appLang) }
                    )

                    Screen.FarmWorld -> FarmWorldScreen(
                        uiState = uiState,
                        onToggle3DMode = { viewModel.toggle3DMode() },
                        onSelectZone = { zone -> viewModel.selectZone(zone) },
                        onNavigateToSimulator = { currentScreen = Screen.WhatIfSimulator }
                    )

                    Screen.WhatIfSimulator -> WhatIfSimulatorScreen(
                        uiState = uiState,
                        onScenarioSelected = { key -> viewModel.selectScenario(key) },
                        onNavigateBack = { currentScreen = Screen.FarmWorld }
                    )

                    Screen.VoiceAssistant -> VoiceAssistantScreen(
                        uiState = uiState,
                        onAskQuery = { query -> viewModel.askVoiceQuery(query) },
                        onStartListening = { viewModel.startListening() },
                        onStopListening = { viewModel.stopListening() },
                        onSpeakAloud = { text, l -> viewModel.speakAloud(text, l) }
                    )

                    Screen.Schemes -> FinanceAndSchemesScreen(
                        uiState = uiState
                    )

                    Screen.Alerts -> AlertsScreen(
                        uiState = uiState
                    )

                    Screen.MyFarm -> MyFarmScreen(
                        uiState = uiState,
                        onNavigateToTwin = { currentScreen = Screen.FarmWorld }
                    )

                    Screen.Settings -> ProfileAndSettingsScreen(
                        uiState = uiState,
                        onLanguageSelected = { appLang -> viewModel.setLanguage(appLang) }
                    )
                }
            }
        }
    }
}
