package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WasteCategory(
    val displayNameTh: String,
    val recyclabilityWeight: Float,
    val carbonFactorPerUnit: Float, // kg CO2e saved per liter
    val typicalVolumeMl: Int,
    val iconName: String,
    val colorHex: Long
) {
    PET_PLASTIC(
        displayNameTh = "ขวดพลาสติก PET",
        recyclabilityWeight = 1.5f,
        carbonFactorPerUnit = 0.12f,
        typicalVolumeMl = 500,
        iconName = "water_bottle",
        colorHex = 0xFF0284C7
    ),
    ALUMINUM_CAN(
        displayNameTh = "กระป๋องอะลูมิเนียม",
        recyclabilityWeight = 2.0f,
        carbonFactorPerUnit = 0.25f,
        typicalVolumeMl = 330,
        iconName = "inventory_2",
        colorHex = 0xFF059669
    ),
    CARDBOARD(
        displayNameTh = "กล่องกระดาษ/ลัง",
        recyclabilityWeight = 1.2f,
        carbonFactorPerUnit = 0.08f,
        typicalVolumeMl = 800,
        iconName = "package_2",
        colorHex = 0xFFD97706
    ),
    E_WASTE(
        displayNameTh = "ขยะอิเล็กทรอนิกส์/แบตฯ",
        recyclabilityWeight = 2.5f,
        carbonFactorPerUnit = 0.45f,
        typicalVolumeMl = 150,
        iconName = "battery_charging_full",
        colorHex = 0xFF7C3AED
    ),
    ORGANIC(
        displayNameTh = "ขยะอินทรีย์/เศษอาหาร",
        recyclabilityWeight = 1.0f,
        carbonFactorPerUnit = 0.05f,
        typicalVolumeMl = 400,
        iconName = "compost",
        colorHex = 0xFF65A30D
    )
}

@Entity(tableName = "waste_scans")
data class WasteScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryName: String,
    val displayName: String,
    val volumeMl: Int,
    val recyclabilityWeight: Float,
    val consistencyMultiplier: Float,
    val fraudRiskScore: Float,
    val calculatedPoints: Int,
    val co2OffsetKg: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val district: String = "เขตจตุจักร กรุงเทพฯ",
    val proofStatus: String = "VERIFIED_PROOF_OF_ACTION"
)
