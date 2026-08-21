package com.example.engine

import com.example.i18n.AppLanguage

data class GovtScheme(
    val id: String,
    val title: String,
    val category: String, // INCOME_SUPPORT, INSURANCE, IRRIGATION, CREDIT, MACHINERY
    val benefitAmount: String,
    val description: String,
    val eligibility: String,
    val documentsNeeded: List<String>,
    val howToApply: String,
    val officialPortal: String,
    val isVerified: Boolean = true
)

object SchemeMatchingEngine {

    fun getSchemes(language: AppLanguage): List<GovtScheme> {
        return when (language) {
            AppLanguage.TAMIL -> listOf(
                GovtScheme(
                    id = "pm_kisan",
                    title = "பி.எம். கிசான் சம்மான் நிதி (PM-KISAN)",
                    category = "வருமான உதவி",
                    benefitAmount = "ஆண்டுக்கு ₹6,000",
                    description = "சிறு மற்றும் குறு விவசாயிகளுக்கு ஆண்டுதோறும் 3 தவணைகளாக தலா ₹2,000 வீதம் நேரடியாக வங்கிக் கணக்கில் வரவு வைக்கப்படுகிறது.",
                    eligibility = "சொந்த நிலம் வைத்துள்ள விவசாயிகள் (சிறு/குறு நில உரிமையாளர்கள்). ஆதார் மற்றும் நில ஆவணம் இணைக்கப்பட்டிருக்க வேண்டும்.",
                    documentsNeeded = listOf("ஆதார் அட்டை (Aadhaar)", "பட்டா / சிட்டா (Patta/Chitta)", "வங்கி கணக்கு புத்தகம் (Bank Passbook)"),
                    howToApply = "pmkisan.gov.in இணையதளத்தில் அல்லது அருகிலுள்ள இ-சேவை மையத்தில் e-KYC செய்து விண்ணப்பிக்கலாம்.",
                    officialPortal = "https://pmkisan.gov.in"
                ),
                GovtScheme(
                    id = "pmksy_drip",
                    title = "நுண்ணீர் பாசன திட்டம் (PMKSY - சொட்டு நீர் மானியம்)",
                    category = "பாசன மானியம்",
                    benefitAmount = "100% வரை மானியம் (சிறு/குறு விவசாயிகளுக்கு)",
                    description = "தக்காளி மற்றும் தோட்டக்கலை பயிர்களுக்கு சொட்டு நீர் பாசன கருவிகள் நிறுவ அரசு முழு மானியம் வழங்குகிறது.",
                    eligibility = "நில உரிமை சான்று, கிணறு/ஆழ்துளை கிணறு பாசன வசதி உள்ள விவசாயிகள்.",
                    documentsNeeded = listOf("பட்டா & அடங்கல்", "நில வரைபடம் (FMB)", "ஆதார் அட்டை", "பாஸ்போர்ட் அளவு புகைப்படம்"),
                    howToApply = "உள்ளூர் வட்டார தோட்டக்கலை உதவி இயக்குநர் அலுவலகத்தில் அல்லது tnhorticulture.tn.gov.in தளத்தில் பதிவு செய்யவும்.",
                    officialPortal = "https://pmksy.gov.in"
                ),
                GovtScheme(
                    id = "pmfby",
                    title = "பிரதம மந்திரி பயிர் காப்பீட்டுத் திட்டம் (PMFBY)",
                    category = "பயிர் காப்பீடு",
                    benefitAmount = "முழு பயிர் இழப்பீட்டு தொகை",
                    description = "வறட்சி, புயல், பெருமழை, வெள்ளம் மற்றும் பூச்சி தாக்குதலில் ஏற்படும் மகசூல் இழப்பிற்கு 100% இழப்பீடு.",
                    eligibility = "அறிவிக்கப்பட்ட பருவத்தில் தக்காளி உள்ளிட்ட பயிர்களை பயிரிடும் அனைத்து விவசாயிகளும் தகுதியுடையவர்கள்.",
                    documentsNeeded = listOf("அடங்கல் சான்று (VAO வழங்கியது)", "வங்கி கணக்கு நகல்", "ஆதார் அட்டை"),
                    howToApply = "தொடக்க வேளாண்மை கூட்டுறவு வங்கி அல்லது பொது சேவை மையத்தில் (CSC) குறிப்பிட்ட தேதிக்குள் பதிவு செய்யவும்.",
                    officialPortal = "https://pmfby.gov.in"
                ),
                GovtScheme(
                    id = "kcc_loan",
                    title = "கிசான் கிரெடிட் கார்டு கடன் (KCC Loan)",
                    category = "விவசாயக் கடன்",
                    benefitAmount = "₹3 லட்சம் வரை 4% சலுகை வட்டியில்",
                    description = "பயிர்க்கடன் மற்றும் உரம், பூச்சிக்கொல்லி, அறுவடை செலவுகளுக்கு உடனடி சலுகை வட்டி கடன்.",
                    eligibility = "நில உரிமையாளர்கள், குத்தகை விவசாயிகள் மற்றும் கூட்டுப்பண்ணை குழுக்கள்.",
                    documentsNeeded = listOf("நில உரிமை பட்டா", "பயிர் சாகுபடி சான்றிதழ்", "பான் கார்டு & ஆதார்"),
                    howToApply = "அருகிலுள்ள தேசியமயமாக்கப்பட்ட அல்லது கூட்டுறவு வங்கிக் கிளையில் விண்ணப்பம் சமர்ப்பிக்கவும்.",
                    officialPortal = "https://agricoop.nic.in"
                ),
                GovtScheme(
                    id = "solar_pump",
                    title = "முதலமைச்சரின் சூரியசக்தி பம்புசெட் திட்டம் (PM-KUSUM)",
                    category = "மின்சாரம் / பாசனம்",
                    benefitAmount = "70% அரசு மானியம்",
                    description = "மின் இணைப்பு இல்லாத விவசாய நிலங்களுக்கு 5HP/7.5HP சூரிய சக்தி மோட்டார் பம்புசெட் அமைக்கும் திட்டம்.",
                    eligibility = "இலவச மின்சார இணைப்பு பெறாத கிணறு/போர்வெல் உள்ள விவசாயிகள்.",
                    documentsNeeded = listOf("பட்டா & சிட்டா", "பூமித்தாய் நிலத்தடி நீர் சான்றிதழ்", "ஆதார்"),
                    howToApply = "வேளாண் பொறியியல் துறை (AED) அலுவலகத்தில் பதிவு செய்யவும்.",
                    officialPortal = "https://pmkusum.mnre.gov.in"
                )
            )

            AppLanguage.HINDI -> listOf(
                GovtScheme(
                    id = "pm_kisan",
                    title = "प्रधानमंत्री किसान सम्मान निधि (PM-KISAN)",
                    category = "आय सहायता",
                    benefitAmount = "₹6,000 प्रति वर्ष",
                    description = "पात्र किसान परिवारों को प्रति वर्ष ₹2,000 की 3 समान किस्तों में प्रत्यक्ष बैंक खाते में सहायता।",
                    eligibility = "खेती योग्य भूमि के मालिक सभी छोटे व सीमांत किसान। आधार लिंक बैंक खाता अनिवार्य।",
                    documentsNeeded = listOf("आधार कार्ड", "खसरा-खतौनी नकल", "बैंक पासबुक"),
                    howToApply = "pmkisan.gov.in पोर्टल अथवा निकटतम जन सेवा केंद्र (CSC) पर e-KYC पूर्ण करें।",
                    officialPortal = "https://pmkisan.gov.in"
                ),
                GovtScheme(
                    id = "pmksy_drip",
                    title = "पीएम कृषि सिंचाई योजना (ड्रिप व स्प्रिंकलर सब्सिडी)",
                    category = "सिंचाई सब्सिडी",
                    benefitAmount = "55% से 90% तक सरकारी अनुदान",
                    description = "टमाटर और बागवानी फसलों में ड्रिप सिंचाई प्रणाली लगाने पर भारी सब्सिडी।",
                    eligibility = "जल स्रोत (बोरवेल/कुआं) युक्त कृषि भूमि के स्वामी।",
                    documentsNeeded = listOf("भूमि अभिलेख (खतौनी)", "आधार कार्ड", "बैंक विवरण", "खेत का नक्शा"),
                    howToApply = "जिला उद्यान विभाग कार्यालय या dbt horticulture पोर्टल पर ऑनलाइन आवेदन करें।",
                    officialPortal = "https://pmksy.gov.in"
                ),
                GovtScheme(
                    id = "pmfby",
                    title = "प्रधानमंत्री फसल बीमा योजना (PMFBY)",
                    category = "फसल सुरक्षा",
                    benefitAmount = "फसल नुकसान पर शत-प्रतिशत क्लेम",
                    description = "प्राकृतिक आपदाओं, सूखा, बाढ़ व कीट प्रकोप से होने वाले फसल नुकसान पर व्यापक सुरक्षा।",
                    eligibility = "अधिसूचित फसलों की बुवाई करने वाले सभी किसान।",
                    documentsNeeded = listOf("बुवाई प्रमाण पत्र", "खतौनी नकल", "बैंक खाता विवरण"),
                    howToApply = "बैंक शाखा अथवा pmfby.gov.in पर अंतिम तिथि से पहले प्रीमियम जमा करें।",
                    officialPortal = "https://pmfby.gov.in"
                ),
                GovtScheme(
                    id = "kcc_loan",
                    title = "किसान क्रेडिट कार्ड (KCC ऋण)",
                    category = "कृषि ऋण",
                    benefitAmount = "₹3 लाख तक केवल 4% ब्याज पर",
                    description = "फसल लागत, खाद, बीज व कृषि उपकरणों के लिए रियायती ब्याज दर पर आसान लोन।",
                    eligibility = "व्यक्तिगत/संयुक्त किसान, बटाईदार और काश्तकार।",
                    documentsNeeded = listOf("जमीन के दस्तावेज", "आधार व पैन कार्ड", "शपथ पत्र"),
                    howToApply = "अपने बैंक शाखा में KCC फॉर्म भरकर जमा करें।",
                    officialPortal = "https://agricoop.nic.in"
                )
            )

            AppLanguage.ENGLISH -> listOf(
                GovtScheme(
                    id = "pm_kisan",
                    title = "PM-KISAN Samman Nidhi",
                    category = "Direct Income Support",
                    benefitAmount = "₹6,000 / Year",
                    description = "Direct financial benefit transferred into the Aadhaar-linked bank accounts of landholder farmer families in 3 installments of ₹2,000.",
                    eligibility = "All landholding farmer families with cultivable land holdings in revenue records.",
                    documentsNeeded = listOf("Aadhaar Card", "Land Record (Patta/RoR)", "Bank Passbook"),
                    howToApply = "Register at pmkisan.gov.in or visit the nearest Common Service Centre (CSC).",
                    officialPortal = "https://pmkisan.gov.in"
                ),
                GovtScheme(
                    id = "pmksy_drip",
                    title = "PM Krishi Sinchayee Yojana (Micro Irrigation Subsidy)",
                    category = "Irrigation Subsidy",
                    benefitAmount = "Up to 100% Subsidy for SF/MF",
                    description = "Capital subsidy for precision drip irrigation installation in horticulture and vegetable crops.",
                    eligibility = "Farmers possessing operational water source (borewell/open well) and cultivable land.",
                    documentsNeeded = listOf("Patta & Chitta", "Field Map (FMB)", "Aadhaar Card", "Bank Details"),
                    howToApply = "Apply through District Horticulture Officer or the State Horticulture portal.",
                    officialPortal = "https://pmksy.gov.in"
                ),
                GovtScheme(
                    id = "pmfby",
                    title = "PM Fasal Bima Yojana (Crop Insurance)",
                    category = "Crop Loss Protection",
                    benefitAmount = "Full Yield Indemnity",
                    description = "Comprehensive crop insurance covering localized calamities, post-harvest losses, and unseasonal rainfall.",
                    eligibility = "All farmers growing notified crops in notified areas during the season.",
                    documentsNeeded = listOf("Sowing Certificate", "Land Record", "Bank Passbook Copy"),
                    howToApply = "Enroll via bank branch, PACCS, or directly on the National Crop Insurance Portal.",
                    officialPortal = "https://pmfby.gov.in"
                ),
                GovtScheme(
                    id = "kcc_loan",
                    title = "Kisan Credit Card (KCC Loan)",
                    category = "Concessional Credit",
                    benefitAmount = "Up to ₹3 Lakh @ 4% Effective Interest",
                    description = "Timely credit for crop cultivation expenses, inputs, and post-harvest maintenance with interest subvention.",
                    eligibility = "Owner cultivators, tenant farmers, and Self Help Groups (SHGs).",
                    documentsNeeded = listOf("Land Ownership Document", "Crop Sowing Proof", "KYC (Aadhaar/PAN)"),
                    howToApply = "Submit 1-page simplified KCC application at any commercial or regional rural bank branch.",
                    officialPortal = "https://agricoop.nic.in"
                ),
                GovtScheme(
                    id = "solar_pump",
                    title = "PM-KUSUM Solar Agri Pump Scheme",
                    category = "Clean Energy & Water",
                    benefitAmount = "Up to 70% Subsidy",
                    description = "Installation of standalone solar agriculture pumps (3HP / 5HP / 7.5HP) in off-grid farmlands.",
                    eligibility = "Farmers without electric agricultural grid connection having verified groundwater.",
                    documentsNeeded = listOf("Land Patta", "Water Source Certificate", "Aadhaar & Bank Details"),
                    howToApply = "Apply via State Agricultural Engineering or Renewable Energy Nodal Agency.",
                    officialPortal = "https://pmkusum.mnre.gov.in"
                )
            )
        }
    }
}
