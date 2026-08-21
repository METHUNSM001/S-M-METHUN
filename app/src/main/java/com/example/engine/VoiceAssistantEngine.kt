package com.example.engine

import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.WeatherRecordEntity
import com.example.i18n.AppLanguage

data class VoiceAssistantResult(
    val detectedLanguage: AppLanguage,
    val matchedIntent: String,
    val toolInvoked: String,
    val spokenResponse: String,
    val visualCardTitle: String,
    val visualCardDetails: String,
    val actionText: String,
    val icon: String
)

object VoiceAssistantEngine {

    fun processQuery(
        query: String,
        currentLanguage: AppLanguage,
        farm: FarmEntity?,
        weather: WeatherRecordEntity?
    ): VoiceAssistantResult {
        val lower = query.lowercase().trim()

        // Detect language / Tanglish / Hindi / English
        val isTamil = lower.contains("thanni") || lower.contains("mazhai") || lower.contains("payir") ||
                lower.contains("epdi") || lower.contains("irukku") || lower.contains("enna") ||
                lower.contains("kidaikkum") || lower.contains("varuma") || query.any { it in '\u0B80'..'\u0BFF' }

        val isHindi = lower.contains("pani") || lower.contains("barish") || lower.contains("fasal") ||
                lower.contains("kaisa") || lower.contains("yojna") || lower.contains("hogi") ||
                query.any { it in '\u0900'..'\u097F' }

        val effectiveLang = when {
            isTamil -> AppLanguage.TAMIL
            isHindi -> AppLanguage.HINDI
            else -> currentLanguage
        }

        val rainProb = weather?.rainProbabilityPercent ?: 84
        val cropName = farm?.primaryCrop ?: "Tomato"

        return when {
            // Intent 1: Water Query ("Innaikku thanni vidalama?")
            lower.contains("thanni") || lower.contains("water") || lower.contains("pani") ||
                    lower.contains("irrigate") || lower.contains("தண்ணீர்") || lower.contains("पानी") -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "WATER_NEED_QUERY",
                        toolInvoked = "get_water_need()",
                        spokenResponse = "இன்று தண்ணீர் விட வேண்டாம். நாளை $rainProb% மழை வர வாய்ப்புள்ளது. மண்ணில் 68% ஈரப்பதம் உள்ளது.",
                        visualCardTitle = "💧 தண்ணீர் தேவை: இன்று வேண்டாம்",
                        visualCardDetails = "தற்போதைய மண் ஈரப்பதம் போதுமானது. மழையை நம்பி பாசனத்தை தவிர்க்கலாம்.",
                        actionText = "சொட்டு நீர் வால்வை மூடி வையுங்கள்.",
                        icon = "💧"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "WATER_NEED_QUERY",
                        toolInvoked = "get_water_need()",
                        spokenResponse = "आज पानी न दें। कल $rainProb% बारिश की संभावना है। मिट्टी में पर्याप्त नमी है।",
                        visualCardTitle = "💧 पानी की जरूरत: आज नहीं",
                        visualCardDetails = "मिट्टी में 68% नमी मौजूद है। कल बारिश से फसल को प्राकृतिक पानी मिलेगा।",
                        actionText = "ड्रिप सिस्टम बंद रखें।",
                        icon = "💧"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "WATER_NEED_QUERY",
                        toolInvoked = "get_water_need()",
                        spokenResponse = "You don't need to water today. Rain is likely tomorrow with $rainProb% probability. Soil moisture is optimal.",
                        visualCardTitle = "💧 Water Status: Not Needed Today",
                        visualCardDetails = "Current root-zone moisture is 68%. Heavy showers expected tomorrow.",
                        actionText = "Keep irrigation pump turned off.",
                        icon = "💧"
                    )
                }
            }

            // Intent 2: Rain Forecast ("Naalaikku mazhai varuma?")
            lower.contains("mazhai") || lower.contains("rain") || lower.contains("barish") ||
                    lower.contains("மழை") || lower.contains("मौसम") || lower.contains("weather") -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "RAIN_FORECAST_QUERY",
                        toolInvoked = "get_weather_forecast()",
                        spokenResponse = "ஆம். நாளை $rainProb% மழை பெய்ய வாய்ப்பு அதிகம். மாலை நேரத்தில் இடி மின்னலுடன் மழை வரலாம்.",
                        visualCardTitle = "🌧️ வானிலை: நாளை மழை எதிர்பார்க்கப்படுகிறது",
                        visualCardDetails = "மழை அளவு: 15-20 மி.மீ. வெப்பநிலை: 28°C. ஈரப்பதம்: 78%.",
                        actionText = "நிலத்தில் வடிகால் வாய்க்காலை சரிபார்க்கவும்.",
                        icon = "🌦️"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "RAIN_FORECAST_QUERY",
                        toolInvoked = "get_weather_forecast()",
                        spokenResponse = "हाँ। कल $rainProb% बारिश होने की प्रबल संभावना है। तापमान 28°C रहेगा।",
                        visualCardTitle = "🌧️ मौसम: कल वर्षा का अनुमान",
                        visualCardDetails = "संभावित वर्षा: 15-20 मिमी। नमी: 78%।",
                        actionText = "खेत की जल निकासी व्यवस्था तैयार रखें।",
                        icon = "🌦️"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "RAIN_FORECAST_QUERY",
                        toolInvoked = "get_weather_forecast()",
                        spokenResponse = "Yes. Rain is expected tomorrow with $rainProb% probability and 28°C temperature.",
                        visualCardTitle = "🌧️ Weather: Rain Expected Tomorrow",
                        visualCardDetails = "Precipitation forecast: 15-20mm. Humidity: 78%. Wind: 14 km/h.",
                        actionText = "Ensure clear drainage trenches around the field boundary.",
                        icon = "🌦️"
                    )
                }
            }

            // Intent 3: Crop Health ("En payir epdi irukku?")
            lower.contains("payir") || lower.contains("crop") || lower.contains("fasal") ||
                    lower.contains("health") || lower.contains("பயிர்") || lower.contains("फसल") -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "CROP_HEALTH_QUERY",
                        toolInvoked = "get_crop_status()",
                        spokenResponse = "உங்கள் தக்காளி பயிர் 88% ஆரோக்கியமாக உள்ளது. பூக்கள் மற்றும் காய்கள் நன்றாக பிடிக்கின்றன.",
                        visualCardTitle = "🌾 பயிர் நிலை: நன்று (88%)",
                        visualCardDetails = "பயிர் பருவம்: பூக்கும் & காய் பிடிக்கும் பருவம் (Day 42). பூச்சி தாக்குதல் இல்லை.",
                        actionText = "மழைக்கு பிறகு போரான் தெளிக்க திட்டமிடுங்கள்.",
                        icon = "🌱"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "CROP_HEALTH_QUERY",
                        toolInvoked = "get_crop_status()",
                        spokenResponse = "आपकी टमाटर की फसल 88% स्वस्थ है। फूल और फल की स्थिति बहुत अच्छी है।",
                        visualCardTitle = "🌾 फसल स्थिति: उत्तम (88%)",
                        visualCardDetails = "अवस्था: फूल एवं फल लगना (42वां दिन)। कोई गंभीर कीट नहीं।",
                        actionText = "बारिश के बाद सूक्ष्म पोषक तत्व स्प्रे करें।",
                        icon = "🌱"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "CROP_HEALTH_QUERY",
                        toolInvoked = "get_crop_status()",
                        spokenResponse = "Your $cropName crop is in good health at 88% vigor with strong flowering and fruit set.",
                        visualCardTitle = "🌾 Crop Condition: Healthy (88%)",
                        visualCardDetails = "Stage: Flowering & Fruit Setting (Day 42). No critical pest threshold.",
                        actionText = "Schedule foliar micronutrient spray after rain.",
                        icon = "🌱"
                    )
                }
            }

            // Intent 4: Government Schemes ("Enakku enna scheme irukku?")
            lower.contains("scheme") || lower.contains("thittam") || lower.contains("திட்டம்") ||
                    lower.contains("yojna") || lower.contains("योजना") || lower.contains("subsidy") ||
                    lower.contains("மானியம்") -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "SCHEMES_QUERY",
                        toolInvoked = "find_matching_schemes()",
                        spokenResponse = "உங்களுக்கு PM-KISAN ₹6,000 மற்றும் 100% சொட்டு நீர் பாசன மானியம் கிடைக்கும்.",
                        visualCardTitle = "🏛️ தகுதியான அரசு திட்டங்கள் (3)",
                        visualCardDetails = "1. PM-KISAN (ஆண்டுக்கு ₹6,000)\n2. PMKSY சொட்டு நீர் பாசன மானியம் (100%)\n3. PMFBY பயிர் காப்பீடு",
                        actionText = "திட்டங்கள் பகுதியில் விண்ணப்பிக்கும் முறையை பாருங்கள்.",
                        icon = "🏛️"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "SCHEMES_QUERY",
                        toolInvoked = "find_matching_schemes()",
                        spokenResponse = "आपको पीएम-किसान योजना (₹6,000) और ड्रिप सिंचाई पर 100% तक सब्सिडी मिल सकती है।",
                        visualCardTitle = "🏛️ पात्र सरकारी योजनाएं (3)",
                        visualCardDetails = "1. पीएम-किसान (₹6,000/वर्ष)\n2. पीएमकेएसवाई सूक्ष्म सिंचाई सब्सिडी\n3. पीएम फसल बीमा योजना",
                        actionText = "योजनाएं टैब में आवेदन प्रक्रिया देखें।",
                        icon = "🏛️"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "SCHEMES_QUERY",
                        toolInvoked = "find_matching_schemes()",
                        spokenResponse = "You qualify for PM-KISAN income support (₹6,000/yr) and 100% Micro-Irrigation Drip Subsidy.",
                        visualCardTitle = "🏛️ Eligible Schemes (3)",
                        visualCardDetails = "1. PM-KISAN (₹6,000/yr Direct Transfer)\n2. PMKSY Drip Subsidy (Up to 100% SF/MF)\n3. PMFBY Crop Insurance",
                        actionText = "Check application checklist in the Schemes tab.",
                        icon = "🏛️"
                    )
                }
            }

            // Intent 5: Loan Options ("Loan option irukka?")
            lower.contains("loan") || lower.contains("kadan") || lower.contains("கடன்") ||
                    lower.contains("rin") || lower.contains("ऋण") || lower.contains("kcc") -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "LOAN_QUERY",
                        toolInvoked = "find_loan_products()",
                        spokenResponse = "உங்கள் 2 ஏக்கர் நிலத்திற்கு கிசான் கிரெடிட் கார்டு மூலம் ₹1,60,000 வரை 4% சலுகை வட்டியில் கடன் பெறலாம்.",
                        visualCardTitle = "💰 கிசான் கிரெடிட் கார்டு (KCC கடன்)",
                        visualCardDetails = "அடமானம் இல்லா வரம்பு: ₹1,60,000. வட்டி: 4% (சரியான நேரத்தில் திரும்ப செலுத்தினால்).",
                        actionText = "வங்கி விண்ணப்பப் படிவத்தை நிதி பகுதியில் காண்க.",
                        icon = "💰"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "LOAN_QUERY",
                        toolInvoked = "find_loan_products()",
                        spokenResponse = "आप किसान क्रेडिट कार्ड (KCC) से ₹1,60,000 तक का ऋण 4% रियायती ब्याज पर ले सकते हैं।",
                        visualCardTitle = "💰 किसान क्रेडिट कार्ड (KCC)",
                        visualCardDetails = "बिना गारंटी सीमा: ₹1,60,000। ब्याज दर: 4% प्रति वर्ष।",
                        actionText = "बैंक आवेदन दस्तावेज सूची देखें।",
                        icon = "💰"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "LOAN_QUERY",
                        toolInvoked = "find_loan_products()",
                        spokenResponse = "You are eligible for Kisan Credit Card loan up to ₹1,60,000 at 4% effective interest without collateral.",
                        visualCardTitle = "💰 Kisan Credit Card (KCC Loan)",
                        visualCardDetails = "Collateral-free limit: ₹1,60,000. Interest rate: 4% p.a. with timely prompt repayment.",
                        actionText = "View required documents in the Finance section.",
                        icon = "💰"
                    )
                }
            }

            // Fallback / General Digital Twin Query
            else -> {
                when (effectiveLang) {
                    AppLanguage.TAMIL -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.TAMIL,
                        matchedIntent = "GENERAL_TWIN_QUERY",
                        toolInvoked = "get_farm_overview()",
                        spokenResponse = "உங்கள் பண்ணை நலம் 88%. இன்று தண்ணீர் தேவையில்லை, நாளை மழை வாய்ப்புள்ளது. நிகர லாப கணிப்பு ₹4.07 லட்சம்.",
                        visualCardTitle = "👨‍🌾 பண்ணை முழு விவரம்",
                        visualCardDetails = "வானிலை: மழை சாத்தியம் ($rainProb%)\nநீர் நிலை: இன்று தேவையில்லை\nநோய் அபாயம்: குறைவு\nலாப கணிப்பு: ₹4,07,100",
                        actionText = "பண்ணை உலகம் 3D சென்று ஆய்வு செய்யலாம்.",
                        icon = "🌾"
                    )
                    AppLanguage.HINDI -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.HINDI,
                        matchedIntent = "GENERAL_TWIN_QUERY",
                        toolInvoked = "get_farm_overview()",
                        spokenResponse = "आपके खेत का स्वास्थ्य 88% है। आज पानी की जरूरत नहीं है, कल बारिश का अनुमान है।",
                        visualCardTitle = "👨‍🌾 खेत समग्र स्थिति",
                        visualCardDetails = "मौसम: बारिश का अनुमान ($rainProb%)\nसिंचाई: आज जरूरत नहीं\nरोग जोखिम: कम\nअनुमानित लाभ: ₹4,07,100",
                        actionText = "डिजिटल फार्म दुनिया 3D देखें।",
                        icon = "🌾"
                    )
                    AppLanguage.ENGLISH -> VoiceAssistantResult(
                        detectedLanguage = AppLanguage.ENGLISH,
                        matchedIntent = "GENERAL_TWIN_QUERY",
                        toolInvoked = "get_farm_overview()",
                        spokenResponse = "Your farm health is 88%. Irrigation is not needed today as rain is expected tomorrow. Estimated net profit is ₹4.07 Lakh.",
                        visualCardTitle = "👨‍🌾 Farm Overview",
                        visualCardDetails = "Weather: Rain Expected ($rainProb%)\nIrrigation: Not needed today\nDisease Risk: Low\nNet Profit: ₹4,07,100",
                        actionText = "Open Farm World 3D to inspect zones.",
                        icon = "🌾"
                    )
                }
            }
        }
    }
}
