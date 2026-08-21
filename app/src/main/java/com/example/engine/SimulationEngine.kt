package com.example.engine

import com.example.i18n.AppLanguage

data class SimulationComparison(
    val optionKey: String,
    val title: String,
    val description: String,
    val estimatedYieldKg: Double,
    val waterSavedLiters: Int,
    val estimatedCostRs: Double,
    val estimatedRevenueRs: Double,
    val netProfitRs: Double,
    val riskLevel: String, // LOW, MEDIUM, HIGH
    val isRecommended: Boolean,
    val reason: String
)

object SimulationEngine {

    fun getScenarios(language: AppLanguage): List<SimulationComparison> {
        return listOf(
            SimulationComparison(
                optionKey = "skip_water",
                title = when (language) {
                    AppLanguage.TAMIL -> "🌧️ தண்ணீரை தவிர்த்து மழையை நம்புவது"
                    AppLanguage.HINDI -> "🌧️ पानी छोड़ें और बारिश पर निर्भर रहें"
                    AppLanguage.ENGLISH -> "🌧️ Skip Irrigation & Rely on Rain"
                },
                description = when (language) {
                    AppLanguage.TAMIL -> "நாளை 84% மழை வாய்ப்பு. நிலத்தில் ஏற்கனவே போதுமான ஈரப்பதம் உள்ளது."
                    AppLanguage.HINDI -> "कल 84% बारिश का अनुमान। मिट्टी में पहले से नमी है।"
                    AppLanguage.ENGLISH -> "84% rain chance tomorrow. Existing soil moisture 68% is optimal."
                },
                estimatedYieldKg = 18400.0,
                waterSavedLiters = 14000,
                estimatedCostRs = 34500.0,
                estimatedRevenueRs = 441600.0, // ₹24/kg
                netProfitRs = 407100.0,
                riskLevel = "LOW",
                isRecommended = true,
                reason = when (language) {
                    AppLanguage.TAMIL -> "சிறந்த லாபம் மற்றும் 14,000 லிட்டர் நீர் சேமிப்பு. வேர் அழுகல் நோய் தவிர்க்கப்படும் ⭐"
                    AppLanguage.HINDI -> "अधिकतम लाभ और 14,000 लीटर पानी की बचत। जड़ सड़न से बचाव ⭐"
                    AppLanguage.ENGLISH -> "Maximum profit, saves 14,000 L water, prevents root hypoxia ⭐"
                }
            ),
            SimulationComparison(
                optionKey = "water_tomorrow",
                title = when (language) {
                    AppLanguage.TAMIL -> "💧 வழக்கம் போல் நாளை தண்ணீர் பாய்ச்சுவது"
                    AppLanguage.HINDI -> "💧 हमेशा की तरह कल सिंचाई करना"
                    AppLanguage.ENGLISH -> "💧 Water As Usual Tomorrow"
                },
                description = when (language) {
                    AppLanguage.TAMIL -> "மழையுடன் சேர்த்து அதிகப்படியான நீர் தேங்க வாய்ப்புள்ளது."
                    AppLanguage.HINDI -> "बारिश के साथ अत्यधिक जलभराव का जोखिम।"
                    AppLanguage.ENGLISH -> "Risk of over-saturation when combined with expected shower."
                },
                estimatedYieldKg = 16800.0,
                waterSavedLiters = 0,
                estimatedCostRs = 38200.0,
                estimatedRevenueRs = 403200.0,
                netProfitRs = 365000.0,
                riskLevel = "MEDIUM",
                isRecommended = false,
                reason = when (language) {
                    AppLanguage.TAMIL -> "அதிகப்படியான நீரால் பூக்கள் உதிரக்கூடும். கூடுதல் பம்ப் செலவு ₹3,700."
                    AppLanguage.HINDI -> "अत्यधिक नमी से फूल झड़ सकते हैं। ₹3,700 अतिरिक्त लागत।"
                    AppLanguage.ENGLISH -> "Overwatering causes blossom drop & unnecessary pumping cost ₹3,700."
                }
            ),
            SimulationComparison(
                optionKey = "organic_fert",
                title = when (language) {
                    AppLanguage.TAMIL -> "🌱 இயற்கை நுண்ணூட்டச்சத்து மற்றும் போரான் தெளித்தல்"
                    AppLanguage.HINDI -> "🌱 जैविक सूक्ष्म पोषक तत्व और बोरान छिड़काव"
                    AppLanguage.ENGLISH -> "🌱 Apply Foliar Boron & Micronutrients"
                },
                description = when (language) {
                    AppLanguage.TAMIL -> "மழை நின்ற பிறகு பூக்கள் காயாக மாறும் விகிதத்தை 12% அதிகரிக்கும்."
                    AppLanguage.HINDI -> "बारिश के बाद छिड़काव से फल बनने की दर 12% बढ़ेगी।"
                    AppLanguage.ENGLISH -> "Post-rain foliar boost increases fruit setting ratio by 12%."
                },
                estimatedYieldKg = 20200.0,
                waterSavedLiters = 14000,
                estimatedCostRs = 37500.0,
                estimatedRevenueRs = 484800.0,
                netProfitRs = 447300.0,
                riskLevel = "LOW",
                isRecommended = false,
                reason = when (language) {
                    AppLanguage.TAMIL -> "மழைக்கு பிறகு செய்யக்கூடிய சிறந்த கூடுதல் லாப நடவடிக்கை."
                    AppLanguage.HINDI -> "बारिश के बाद लागू करने के लिए उत्कृष्ट लाभ विकल्प।"
                    AppLanguage.ENGLISH -> "High potential return of +₹40,200 when executed after rain passes."
                }
            ),
            SimulationComparison(
                optionKey = "early_harvest",
                title = when (language) {
                    AppLanguage.TAMIL -> "🌾 5 நாட்கள் முன்கூட்டியே முதல் அறுவடை செய்வது"
                    AppLanguage.HINDI -> "🌾 5 दिन पहले पहली तुड़ाई करना"
                    AppLanguage.ENGLISH -> "🌾 Harvest 5 Days Early for Mandi Spike"
                },
                description = when (language) {
                    AppLanguage.TAMIL -> "தற்போதைய உயர் சந்தை விலையை (₹28/கிலோ) பயன்படுத்திக் கொள்ளுதல்."
                    AppLanguage.HINDI -> "वर्तमान उच्च मंडी मूल्य (₹28/किग्रा) का लाभ उठाएं।"
                    AppLanguage.ENGLISH -> "Capitalize on temporary supply dip and premium ₹28/kg pricing."
                },
                estimatedYieldKg = 15200.0,
                waterSavedLiters = 8000,
                estimatedCostRs = 33000.0,
                estimatedRevenueRs = 425600.0,
                netProfitRs = 392600.0,
                riskLevel = "MEDIUM",
                isRecommended = false,
                reason = when (language) {
                    AppLanguage.TAMIL -> "அதிக விலை கிடைத்தாலும் காய் எடை சற்று குறைய வாய்ப்பு."
                    AppLanguage.HINDI -> "भाव अधिक मिलेगा किंतु फलों का वजन थोड़ा कम रह सकता है।"
                    AppLanguage.ENGLISH -> "Captures premium price per kg but total fruit weight is ~10% lower."
                }
            )
        )
    }
}
