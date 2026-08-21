package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.i18n.AppLanguage
import com.example.i18n.Translations

sealed class Screen(val route: String, val icon: ImageVector, val titleKey: String) {
    data object Home : Screen("home", Icons.Default.Home, "nav_home")
    data object MyFarm : Screen("my_farm", Icons.Default.Agriculture, "nav_farm")
    data object FarmWorld : Screen("farm_world", Icons.Default.ViewInAr, "nav_twin")
    data object VoiceAssistant : Screen("voice", Icons.Default.Mic, "nav_ask")
    data object Schemes : Screen("schemes", Icons.Default.AccountBalance, "nav_schemes")
    data object Alerts : Screen("alerts", Icons.Default.Notifications, "nav_alerts")
    data object Settings : Screen("settings", Icons.Default.Settings, "nav_settings")
    data object WhatIfSimulator : Screen("what_if", Icons.Default.ViewInAr, "what_if_title")
}

val BottomNavItems = listOf(
    Screen.Home,
    Screen.FarmWorld,
    Screen.VoiceAssistant,
    Screen.Schemes,
    Screen.Settings
)
