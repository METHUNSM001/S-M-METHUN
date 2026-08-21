package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AgriTwinDatabase
import com.example.data.local.entities.AlertEntity
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.FarmZoneEntity
import com.example.data.local.entities.SimulationRecordEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WeatherRecordEntity
import com.example.data.repository.AgriTwinRepository
import com.example.engine.DecisionEngine
import com.example.engine.FarmDecisionSummary
import com.example.engine.GovtScheme
import com.example.engine.MultiAgentSystem
import com.example.engine.SchemeMatchingEngine
import com.example.engine.SimulationComparison
import com.example.engine.SimulationEngine
import com.example.engine.VoiceAssistantEngine
import com.example.engine.VoiceAssistantResult
import com.example.i18n.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class AgriTwinUiState(
    val currentLanguage: AppLanguage = AppLanguage.TAMIL,
    val profile: UserProfileEntity? = null,
    val farm: FarmEntity? = null,
    val zones: List<FarmZoneEntity> = emptyList(),
    val weather: WeatherRecordEntity? = null,
    val decision: FarmDecisionSummary? = null,
    val alerts: List<AlertEntity> = emptyList(),
    val is3DMode: Boolean = true,
    val selectedZone: FarmZoneEntity? = null,
    val isVoiceListening: Boolean = false,
    val isVoiceSpeaking: Boolean = false,
    val latestVoiceResult: VoiceAssistantResult? = null,
    val whatIfScenarios: List<SimulationComparison> = emptyList(),
    val selectedScenarioKey: String = "skip_water",
    val schemes: List<GovtScheme> = emptyList(),
    val isOfflineMode: Boolean = false
)

class AgriTwinViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: AgriTwinRepository
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _uiState = MutableStateFlow(AgriTwinUiState())
    val uiState: StateFlow<AgriTwinUiState> = _uiState.asStateFlow()

    init {
        val database = AgriTwinDatabase.getDatabase(application)
        repository = AgriTwinRepository(database)

        tts = TextToSpeech(application, this)

        viewModelScope.launch {
            repository.initializeDefaultDemoDataIfNeeded()
            observeData()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady = true
            updateTtsLanguage(_uiState.value.currentLanguage)
        }
    }

    private fun updateTtsLanguage(language: AppLanguage) {
        if (!isTtsReady || tts == null) return
        val locale = when (language) {
            AppLanguage.TAMIL -> Locale("ta", "IN")
            AppLanguage.HINDI -> Locale("hi", "IN")
            AppLanguage.ENGLISH -> Locale.ENGLISH
        }
        tts?.language = locale
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                repository.userProfile,
                repository.primaryFarm,
                repository.allAlerts
            ) { profile, farm, alerts ->
                Triple(profile, farm, alerts)
            }.collect { (profile, farm, alerts) ->
                val farmId = farm?.id ?: "demo_farm_1"
                val lang = AppLanguage.fromCode(profile?.language ?: "ta")

                repository.getFarmZones(farmId).collect { zones ->
                    repository.getLatestWeather(farmId).collect { weather ->
                        val decision = DecisionEngine.evaluate(farm, zones, weather, lang)
                        val scenarios = SimulationEngine.getScenarios(lang)
                        val schemes = SchemeMatchingEngine.getSchemes(lang)

                        _uiState.value = _uiState.value.copy(
                            currentLanguage = lang,
                            profile = profile,
                            farm = farm,
                            zones = zones,
                            weather = weather,
                            decision = decision,
                            alerts = alerts,
                            whatIfScenarios = scenarios,
                            schemes = schemes
                        )
                    }
                }
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.value = _uiState.value.copy(
            currentLanguage = language,
            decision = DecisionEngine.evaluate(_uiState.value.farm, _uiState.value.zones, _uiState.value.weather, language),
            whatIfScenarios = SimulationEngine.getScenarios(language),
            schemes = SchemeMatchingEngine.getSchemes(language)
        )
        updateTtsLanguage(language)
        viewModelScope.launch {
            repository.updateLanguage(language.code)
        }
    }

    fun toggle3DMode() {
        _uiState.value = _uiState.value.copy(is3DMode = !_uiState.value.is3DMode)
    }

    fun selectZone(zone: FarmZoneEntity?) {
        _uiState.value = _uiState.value.copy(selectedZone = zone)
    }

    fun selectScenario(key: String) {
        _uiState.value = _uiState.value.copy(selectedScenarioKey = key)
    }

    fun askVoiceQuery(query: String) {
        val currentLang = _uiState.value.currentLanguage
        val result = VoiceAssistantEngine.processQuery(
            query = query,
            currentLanguage = currentLang,
            farm = _uiState.value.farm,
            weather = _uiState.value.weather
        )

        _uiState.value = _uiState.value.copy(
            isVoiceListening = false,
            latestVoiceResult = result,
            isVoiceSpeaking = true
        )

        // Save conversation
        viewModelScope.launch {
            repository.addChatMessage(query, isUser = true, language = currentLang.code)
            repository.addChatMessage(result.spokenResponse, isUser = false, toolInvoked = result.toolInvoked, language = result.detectedLanguage.code)
        }

        // Speak aloud using TTS
        speakAloud(result.spokenResponse, result.detectedLanguage)
    }

    fun startListening() {
        _uiState.value = _uiState.value.copy(isVoiceListening = true)
    }

    fun stopListening() {
        _uiState.value = _uiState.value.copy(isVoiceListening = false)
    }

    fun speakAloud(text: String, language: AppLanguage) {
        if (!isTtsReady || tts == null) return
        updateTtsLanguage(language)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "agritwin_tts")
    }

    fun stopSpeaking() {
        tts?.stop()
        _uiState.value = _uiState.value.copy(isVoiceSpeaking = false)
    }

    fun saveCustomFarm(
        name: String,
        area: Double,
        crop: String,
        variety: String,
        stage: String,
        irrigation: String,
        soil: String
    ) {
        viewModelScope.launch {
            val farm = FarmEntity(
                id = "custom_farm_" + System.currentTimeMillis(),
                name = name,
                areaAcres = area,
                primaryCrop = crop,
                cropVariety = variety,
                cropStage = stage,
                irrigationMethod = irrigation,
                soilType = soil,
                isDemo = false
            )
            repository.saveFarm(farm)
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
