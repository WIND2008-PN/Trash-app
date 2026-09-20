package com.example.data.model

enum class EsgDataProvider(val title: String, val ratingLabel: String, val description: String) {
    BLOOMBERG(
        title = "Bloomberg ESG Data Feed",
        ratingLabel = "Bloomberg ESG Score",
        description = "B2B Sustainability Disclosure & Scope 3 Quantitative Metrics Integration"
    ),
    MSCI(
        title = "MSCI ESG Research API",
        ratingLabel = "MSCI ESG Rating",
        description = "Global Standard Corporate ESG Risk & Climate Impact Rating"
    )
}

data class CorporateEsgSnapshot(
    val provider: EsgDataProvider,
    val totalScope3WasteKg: Float,
    val plasticDivertedKg: Float,
    val co2eOffsetKg: Float,
    val verifiedActionsCount: Int,
    val activeCivicParticipants: Int,
    val scoreOrRating: String, // e.g. "AA (Industry Leader)" or "84.2 / 100"
    val circularityRate: Float, // e.g. 78.4%
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)

data class DistrictHotspot(
    val districtId: String,
    val nameTh: String,
    val nameEn: String,
    val wasteDensityKgDay: Float,
    val clearanceRatePct: Float,
    val primaryWasteType: String,
    val statusLevel: String, // "High Need", "Active Clean", "Eco Optimal"
    val participantCount: Int
)
