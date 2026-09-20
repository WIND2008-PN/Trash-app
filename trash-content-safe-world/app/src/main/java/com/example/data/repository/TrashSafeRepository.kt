package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CorporateEsgSnapshot
import com.example.data.model.DistrictHotspot
import com.example.data.model.EsgDataProvider
import com.example.data.model.FeedPostEntity
import com.example.data.model.RewardVoucherEntity
import com.example.data.model.WasteCategory
import com.example.data.model.WasteScanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.math.max
import kotlin.math.roundToInt

class TrashSafeRepository(private val database: AppDatabase) {
    private val wasteScanDao = database.wasteScanDao()
    private val feedPostDao = database.feedPostDao()
    private val voucherDao = database.voucherDao()

    val allScans: Flow<List<WasteScanEntity>> = wasteScanDao.getAllScans()
    val totalScansCount: Flow<Int> = wasteScanDao.getScansCount()
    val totalEarnedPoints: Flow<Int?> = wasteScanDao.getTotalEarnedPoints()
    val totalCo2Offset: Flow<Float?> = wasteScanDao.getTotalCo2Offset()

    val allFeedPosts: Flow<List<FeedPostEntity>> = feedPostDao.getAllPosts()
    val allVouchers: Flow<List<RewardVoucherEntity>> = voucherDao.getAllVouchers()
    val claimedVouchers: Flow<List<RewardVoucherEntity>> = voucherDao.getClaimedVouchers()

    suspend fun seedInitialDataIfEmpty() {
        val existingPosts = allFeedPosts.first()
        if (existingPosts.isEmpty()) {
            val initialPosts = listOf(
                FeedPostEntity(
                    authorName = "น้องไอซ์ สายรักษ์โลก",
                    authorTag = "@ice_greentea",
                    caption = "ตอนแรกจะเดินผ่านไปแล้ว กลัวเพื่อนแซวว่า 'ทำคอนเทนต์ขยะ' 555 แต่ Agir บอกว่าไม่ต้องอาย! เก็บแยกขวด PET 3 ขวดลงถังรีไซเคิล รู้สึกใจฟูมาก 🌱✨",
                    wasteCategory = WasteCategory.PET_PLASTIC.name,
                    pointsEarned = 38,
                    co2OffsetKg = 0.36f,
                    inspireCount = 24,
                    heartCount = 48,
                    sproutCount = 19,
                    district = "จตุจักร กรุงเทพฯ",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 35
                ),
                FeedPostEntity(
                    authorName = "บิ๊ก เบิ้ม Eco",
                    authorTag = "@big_zerowaste",
                    caption = "เก็บกระป๋องกาแฟอะลูมิเนียมริมสวนเบญจกิติ! อะลูมิเนียมรีไซเคิลได้ไม่รู้จบ วันนี้รักษา Streak วันที่ 5 ได้แล้ว สู้ๆ นะทุกคน 💚",
                    wasteCategory = WasteCategory.ALUMINUM_CAN.name,
                    pointsEarned = 45,
                    co2OffsetKg = 0.50f,
                    inspireCount = 31,
                    heartCount = 56,
                    sproutCount = 28,
                    district = "คลองเตย กรุงเทพฯ",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120
                ),
                FeedPostEntity(
                    authorName = "ฟ้าใส ใจสะอาด",
                    authorTag = "@fah_cleancity",
                    caption = "เคลียร์กล่องพัสดุและแบตเตอรี่เก่าส่งจุดรับ E-Waste! ปลอดภัยต่อชุมชน แถมได้แต้มมาแลกคูปอง Inthanin แล้ว เย้!",
                    wasteCategory = WasteCategory.E_WASTE.name,
                    pointsEarned = 52,
                    co2OffsetKg = 0.68f,
                    inspireCount = 42,
                    heartCount = 63,
                    sproutCount = 37,
                    district = "ปทุมวัน กรุงเทพฯ",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 360
                )
            )
            feedPostDao.insertAll(initialPosts)
        }

        val existingVouchers = allVouchers.first()
        if (existingVouchers.isEmpty()) {
            val initialVouchers = listOf(
                RewardVoucherEntity(
                    id = "v-inthanin-01",
                    brandName = "Inthanin Coffee",
                    title = "ฟรี! กาแฟออร์แกนิก 1 แก้ว",
                    subtitle = "เมื่อนำแก้วมาเอง หรือรีไซเคิลขยะกับ Safe World",
                    pointsCost = 60,
                    category = "Food & Beverage",
                    terms = "ใช้ได้ที่ Inthanin ทุกสาขาทั่วประเทศ แสดงรหัส QR แก่บาริสต้า",
                    iconType = "coffee"
                ),
                RewardVoucherEntity(
                    id = "v-bts-02",
                    brandName = "BTS Skytrain",
                    title = "BTS Green Pass เดินทางฟรี 1 เที่ยว",
                    subtitle = "ส่งเสริมการเดินทางคาร์บอนต่ำในกรุงเทพฯ",
                    pointsCost = 90,
                    category = "Green Mobility",
                    terms = "ใช้สแกนผ่านประตูสถานีรถไฟฟ้าบีทีเอสได้ทุกสาย",
                    iconType = "train"
                ),
                RewardVoucherEntity(
                    id = "v-refill-03",
                    brandName = "Zero Waste Refill Station",
                    title = "ส่วนลด 25% สบู่และน้ำยาเติม",
                    subtitle = "ลดบรรจุภัณฑ์แบบใช้ครั้งเดียวทิ้ง",
                    pointsCost = 45,
                    category = "Eco Living",
                    terms = "สำหรับยอดซื้อขั้นต่ำ 150 บาท ที่สถานีรีฟิลที่ร่วมรายการ",
                    iconType = "storefront"
                ),
                RewardVoucherEntity(
                    id = "v-patagonia-04",
                    brandName = "Patagonia Thailand",
                    title = "Cash Voucher มูลค่า 150 บาท",
                    subtitle = "สำหรับคอลเลกชัน Worn Wear & Upcycled Gear",
                    pointsCost = 150,
                    category = "Sustainable Fashion",
                    terms = "ใช้ได้ทั้งหน้าร้านและออนไลน์ ตรวจสอบเงื่อนไขเพิ่มเติม",
                    iconType = "shopping_bag"
                )
            )
            voucherDao.insertAll(initialVouchers)
        }
    }

    /**
     * Calculates Points according to the exact mathematical specification:
     * Points = (Waste Volume * Material Recyclability Weight) + Consistency Multiplier - Fraud Risk Score
     */
    fun calculatePoints(
        volumeMl: Int,
        recyclabilityWeight: Float,
        streakDays: Int,
        fraudRiskScore: Float
    ): Int {
        val volumeUnits = volumeMl / 100.0f
        val consistencyMultiplier = streakDays * 2.5f
        val rawPoints = (volumeUnits * recyclabilityWeight) + consistencyMultiplier - fraudRiskScore
        return max(1, rawPoints.roundToInt())
    }

    suspend fun logWasteScan(
        category: WasteCategory,
        volumeMl: Int,
        streakDays: Int,
        fraudRiskScore: Float = 0.0f,
        district: String = "เขตจตุจักร กรุงเทพฯ"
    ): WasteScanEntity {
        val points = calculatePoints(
            volumeMl = volumeMl,
            recyclabilityWeight = category.recyclabilityWeight,
            streakDays = streakDays,
            fraudRiskScore = fraudRiskScore
        )
        val co2Offset = (volumeMl / 1000.0f) * category.carbonFactorPerUnit

        val scan = WasteScanEntity(
            categoryName = category.name,
            displayName = category.displayNameTh,
            volumeMl = volumeMl,
            recyclabilityWeight = category.recyclabilityWeight,
            consistencyMultiplier = streakDays * 2.5f,
            fraudRiskScore = fraudRiskScore,
            calculatedPoints = points,
            co2OffsetKg = co2Offset,
            district = district
        )
        val id = wasteScanDao.insertScan(scan)
        return scan.copy(id = id)
    }

    suspend fun createFeedPostFromScan(
        scan: WasteScanEntity,
        userNote: String
    ): FeedPostEntity {
        val post = FeedPostEntity(
            authorName = "You (Civic Champion)",
            authorTag = "@me_ecosafe",
            caption = if (userNote.isNotBlank()) userNote else "คอนเทนต์ขยะเพื่อโลก! แยก ${scan.displayName} เรียบร้อย ได้ $ {scan.calculatedPoints} พอยต์ ขอบคุณคำแนะนำจาก Agir นะ 🌱✨",
            wasteCategory = scan.categoryName,
            pointsEarned = scan.calculatedPoints,
            co2OffsetKg = scan.co2OffsetKg,
            district = scan.district,
            inspireCount = 1,
            heartCount = 1,
            sproutCount = 1,
            hasUserHeart = true
        )
        val id = feedPostDao.insertPost(post)
        return post.copy(id = id)
    }

    suspend fun toggleReaction(postId: Long, reaction: String, currentState: Boolean) {
        val delta = if (currentState) -1 else 1
        when (reaction) {
            "INSPIRE" -> feedPostDao.toggleInspire(postId, delta, !currentState)
            "HEART" -> feedPostDao.toggleHeart(postId, delta, !currentState)
            "SPROUT" -> feedPostDao.toggleSprout(postId, delta, !currentState)
        }
    }

    suspend fun claimVoucher(voucherId: String): String {
        val dynamicToken = "TCW-${(1000..9999).random()}-${(1000..9999).random()}"
        val now = System.currentTimeMillis()
        val expires = now + (15 * 60 * 1000) // 15 minutes dynamic validity
        voucherDao.claimVoucher(voucherId, dynamicToken, now, expires)
        return dynamicToken
    }

    suspend fun redeemVoucher(voucherId: String) {
        voucherDao.markRedeemed(voucherId)
    }

    fun getCorporateEsgSnapshot(provider: EsgDataProvider, userScansCount: Int): CorporateEsgSnapshot {
        val baseScope3Waste = 1240.5f + (userScansCount * 0.45f)
        val basePlasticDiverted = 780.2f + (userScansCount * 0.32f)
        val baseCo2Offset = 312.8f + (userScansCount * 0.18f)

        return when (provider) {
            EsgDataProvider.BLOOMBERG -> CorporateEsgSnapshot(
                provider = provider,
                totalScope3WasteKg = baseScope3Waste,
                plasticDivertedKg = basePlasticDiverted,
                co2eOffsetKg = baseCo2Offset,
                verifiedActionsCount = 4280 + userScansCount,
                activeCivicParticipants = 1492,
                scoreOrRating = "86.4 / 100 (Top Decile)",
                circularityRate = 81.2f
            )
            EsgDataProvider.MSCI -> CorporateEsgSnapshot(
                provider = provider,
                totalScope3WasteKg = baseScope3Waste,
                plasticDivertedKg = basePlasticDiverted,
                co2eOffsetKg = baseCo2Offset,
                verifiedActionsCount = 4280 + userScansCount,
                activeCivicParticipants = 1492,
                scoreOrRating = "AA (Industry Leader)",
                circularityRate = 79.8f
            )
        }
    }

    fun getDistrictHotspots(): List<DistrictHotspot> {
        return listOf(
            DistrictHotspot("bkk-01", "จตุจักร", "Chatuchak", 1450f, 84.5f, "ขวดพลาสติก PET", "Active Clean", 342),
            DistrictHotspot("bkk-02", "ปทุมวัน", "Pathum Wan", 1920f, 76.2f, "บรรจุภัณฑ์อาหาร", "High Need", 518),
            DistrictHotspot("bkk-03", "คลองเตย", "Khlong Toei", 2280f, 69.4f, "กระป๋อง & พลาสติก", "High Need", 620),
            DistrictHotspot("bkk-04", "บางรัก", "Bang Rak", 1120f, 91.0f, "กล่องกระดาษพัสดุ", "Eco Optimal", 289),
            DistrictHotspot("bkk-05", "พระโขนง", "Phra Khanong", 890f, 93.4f, "ขยะอินทรีย์", "Eco Optimal", 175)
        )
    }
}
