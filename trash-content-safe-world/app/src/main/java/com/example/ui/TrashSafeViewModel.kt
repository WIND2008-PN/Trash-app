package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AgirChatMessage
import com.example.data.model.AgirMood
import com.example.data.model.AgirPersonalityMode
import com.example.data.model.CorporateEsgSnapshot
import com.example.data.model.DistrictHotspot
import com.example.data.model.EsgDataProvider
import com.example.data.model.FeedPostEntity
import com.example.data.model.RewardVoucherEntity
import com.example.data.model.WasteCategory
import com.example.data.model.WasteScanEntity
import com.example.data.remote.GeminiRemoteService
import com.example.data.repository.TrashSafeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleTh: String, val icon: String) {
    SCANNER("สแกน AI", "center_focus_strong"),
    FEED("Safe Feed", "favorite"),
    REWARDS("รางวัล", "confirmation_number"),
    ESG("ESG City", "bar_chart"),
    SETTINGS("ตั้งค่า", "settings")
}

data class ScannerHudState(
    val selectedCategory: WasteCategory = WasteCategory.PET_PLASTIC,
    val volumeMl: Int = 500,
    val confidencePct: Int = 96,
    val isFaceBlurActive: Boolean = true,
    val isPlateBlurActive: Boolean = true,
    val fraudRiskScore: Float = 0.0f,
    val motionVerified: Boolean = true,
    val duplicateHashCheck: String = "HASH_PASS_UNIQUE_SCENE",
    val isScanningAnimation: Boolean = false,
    val lastScannedItem: WasteScanEntity? = null
)

class TrashSafeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = TrashSafeRepository(database)
    private val geminiService = GeminiRemoteService()

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.SCANNER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Agir Companion State
    private val _agirMood = MutableStateFlow(AgirMood.JOYFUL)
    val agirMood: StateFlow<AgirMood> = _agirMood.asStateFlow()

    private val _agirSpeechBubble = MutableStateFlow("ยินดีต้อนรับสู่ Safe World! วันนี้แยกขยะชิ้นไหน ให้ Agir ช่วยวิเคราะห์ได้เลยนะ 🌱")
    val agirSpeechBubble: StateFlow<String> = _agirSpeechBubble.asStateFlow()

    private val _personalityMode = MutableStateFlow(AgirPersonalityMode.GEN_Z_BUDDY)
    val personalityMode: StateFlow<AgirPersonalityMode> = _personalityMode.asStateFlow()

    private val _isChatDialogOpen = MutableStateFlow(false)
    val isChatDialogOpen: StateFlow<Boolean> = _isChatDialogOpen.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<AgirChatMessage>>(
        listOf(
            AgirChatMessage(
                text = "หวัดดีจ้า! Agir เป็นเพื่อนคู่หูและเจ้าของพื้นที่ Safe World นะ 💚 มีอะไรปรึกษา หรืออยากคุยเรื่องรีไซเคิลแบบสบายใจ ถามได้ตลอดเลยนะ ไม่มีการตัดสินกันแน่นอน!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<AgirChatMessage>> = _chatMessages.asStateFlow()
    val isChatLoading = MutableStateFlow(false)

    // User Profile & Gamification
    val userStreakDays = MutableStateFlow(5)
    val bonusPointsWallet = MutableStateFlow(120) // Initial welcome points

    val allScans: StateFlow<List<WasteScanEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalEarnedPoints: StateFlow<Int> = combine(
        repository.totalEarnedPoints,
        bonusPointsWallet
    ) { earned, bonus ->
        (earned ?: 0) + bonus
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 120)

    val totalCo2Offset: StateFlow<Float> = repository.totalCo2Offset
        .combine(MutableStateFlow(0f)) { dbCo2, _ -> dbCo2 ?: 0.0f }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0f)

    // Scanner HUD State
    private val _hudState = MutableStateFlow(ScannerHudState())
    val hudState: StateFlow<ScannerHudState> = _hudState.asStateFlow()

    // Social Safe Feed
    val feedPosts: StateFlow<List<FeedPostEntity>> = repository.allFeedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Rewards & Vouchers
    val vouchers: StateFlow<List<RewardVoucherEntity>> = repository.allVouchers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeVoucherModal = MutableStateFlow<RewardVoucherEntity?>(null)
    val activeVoucherModal: StateFlow<RewardVoucherEntity?> = _activeVoucherModal.asStateFlow()

    // ESG & Smart City
    private val _selectedEsgProvider = MutableStateFlow(EsgDataProvider.BLOOMBERG)
    val selectedEsgProvider: StateFlow<EsgDataProvider> = _selectedEsgProvider.asStateFlow()

    val districtHotspots: List<DistrictHotspot> = repository.getDistrictHotspots()

    val corporateEsgSnapshot: StateFlow<CorporateEsgSnapshot> = combine(
        _selectedEsgProvider,
        repository.totalScansCount
    ) { provider, count ->
        repository.getCorporateEsgSnapshot(provider, count)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.getCorporateEsgSnapshot(EsgDataProvider.BLOOMBERG, 0)
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
        when (tab) {
            AppTab.SCANNER -> {
                _agirMood.value = AgirMood.ANALYZING
                _agirSpeechBubble.value = "เปิดกล้องส่องขยะได้เลย! AI พร้อมตรวจจับแบบเรียลไทม์แล้ว ⚡"
            }
            AppTab.FEED -> {
                _agirMood.value = AgirMood.JOYFUL
                _agirSpeechBubble.value = "Safe Feed แดนพลังบวก! ดูสิทุกคนกำลังช่วยกันเปลี่ยนคอนเทนต์ขยะเป็นพลังโลก 💚"
            }
            AppTab.REWARDS -> {
                _agirMood.value = AgirMood.PROUD
                _agirSpeechBubble.value = "แต้มที่สะสมไว้แลกสิทธิพิเศษจากแบรนด์พาร์ทเนอร์ได้เลยนะ ชื่นใจสุดๆ! ☕🎫"
            }
            AppTab.ESG -> {
                _agirMood.value = AgirMood.CURIOUS
                _agirSpeechBubble.value = "นี่คือแดชบอร์ดเมืองและข้อมูล ESG ส่งตรงถึง Bloomberg & MSCI เชียวนะ! 📊"
            }
            AppTab.SETTINGS -> {
                _agirMood.value = AgirMood.ENCOURAGING
                _agirSpeechBubble.value = "ปรับแต่งการตั้งค่าความเป็นส่วนตัวและสไตล์ของ Agir ได้ตามใจเลยนะ ⚙️"
            }
        }
    }

    fun updateScannerCategory(category: WasteCategory) {
        _hudState.value = _hudState.value.copy(
            selectedCategory = category,
            volumeMl = category.typicalVolumeMl,
            confidencePct = (92..99).random()
        )
        _agirSpeechBubble.value = "ตรวจพบ ${category.displayNameTh}! น้ำหนักรีไซเคิล x${category.recyclabilityWeight} เลยนะ!"
    }

    fun updateScannerVolume(volumeMl: Int) {
        _hudState.value = _hudState.value.copy(volumeMl = volumeMl)
    }

    fun toggleFaceBlur() {
        _hudState.value = _hudState.value.copy(isFaceBlurActive = !_hudState.value.isFaceBlurActive)
    }

    fun togglePlateBlur() {
        _hudState.value = _hudState.value.copy(isPlateBlurActive = !_hudState.value.isPlateBlurActive)
    }

    fun calculateCurrentPointsPreview(): Int {
        val s = _hudState.value
        return repository.calculatePoints(
            volumeMl = s.volumeMl,
            recyclabilityWeight = s.selectedCategory.recyclabilityWeight,
            streakDays = userStreakDays.value,
            fraudRiskScore = s.fraudRiskScore
        )
    }

    fun confirmWasteAction() {
        viewModelScope.launch {
            _hudState.value = _hudState.value.copy(isScanningAnimation = true)
            delay(400)

            val currentCategory = _hudState.value.selectedCategory
            val scan = repository.logWasteScan(
                category = currentCategory,
                volumeMl = _hudState.value.volumeMl,
                streakDays = userStreakDays.value,
                fraudRiskScore = _hudState.value.fraudRiskScore
            )

            _hudState.value = _hudState.value.copy(
                isScanningAnimation = false,
                lastScannedItem = scan
            )
            _agirMood.value = AgirMood.PROUD
            _agirSpeechBubble.value = "สุดยอดไปเลย! บันทึกสำเร็จ ได้รับ +${scan.calculatedPoints} พอยต์! โลกขอบคุณเธอนะ 🌟🌱"

            // Auto-prompt to create a safe-feed post
            repository.createFeedPostFromScan(
                scan = scan,
                userNote = "เพิ่งแยก ${scan.displayName} (${scan.volumeMl}ml) สำเร็จ! ลดคาร์บอนไปได้ ${String.format("%.2f", scan.co2OffsetKg)} kg CO2e ขอบคุณ Agir ที่ช่วยดูแลนะ 🌱"
            )
        }
    }

    fun toggleFeedReaction(postId: Long, reaction: String, currentState: Boolean) {
        viewModelScope.launch {
            repository.toggleReaction(postId, reaction, currentState)
        }
    }

    fun createFeedPost(text: String) {
        viewModelScope.launch {
            val sampleScan = WasteScanEntity(
                categoryName = WasteCategory.PET_PLASTIC.name,
                displayName = "ขยะรีไซเคิลชุมชน",
                volumeMl = 500,
                recyclabilityWeight = 1.5f,
                consistencyMultiplier = 12.5f,
                fraudRiskScore = 0f,
                calculatedPoints = 25,
                co2OffsetKg = 0.20f
            )
            repository.createFeedPostFromScan(sampleScan, text)
            _agirSpeechBubble.value = "โพสต์ของเธอถูกแชร์สู่ Safe Feed เรียบร้อย! มีแต่พลังบวกแน่นอน 💚"
        }
    }

    fun openVoucherDetail(voucher: RewardVoucherEntity) {
        _activeVoucherModal.value = voucher
    }

    fun dismissVoucherModal() {
        _activeVoucherModal.value = null
    }

    fun claimVoucher(voucher: RewardVoucherEntity) {
        viewModelScope.launch {
            val currentPoints = totalEarnedPoints.value
            if (currentPoints >= voucher.pointsCost) {
                bonusPointsWallet.value -= voucher.pointsCost
                val code = repository.claimVoucher(voucher.id)
                _activeVoucherModal.value = voucher.copy(isClaimed = true, dynamicCode = code)
                _agirMood.value = AgirMood.JOYFUL
                _agirSpeechBubble.value = "แลก ${voucher.brandName} สำเร็จแล้ว! รหัส Dynamic QR พร้อมใช้งาน 15 นาทีนะ 🎉"
            }
        }
    }

    fun redeemVoucher(voucherId: String) {
        viewModelScope.launch {
            repository.redeemVoucher(voucherId)
            val current = _activeVoucherModal.value
            if (current != null && current.id == voucherId) {
                _activeVoucherModal.value = current.copy(isRedeemed = true)
            }
            _agirSpeechBubble.value = "ใช้สิทธิเรียบร้อย! ขอบคุณที่ร่วมสร้าง Safe World ไปด้วยกันนะ 🌱"
        }
    }

    fun selectEsgProvider(provider: EsgDataProvider) {
        _selectedEsgProvider.value = provider
    }

    fun setPersonalityMode(mode: AgirPersonalityMode) {
        _personalityMode.value = mode
        _agirSpeechBubble.value = "เปลี่ยนโหมดเป็น '${mode.titleTh}' เรียบร้อยแล้วนะ!"
    }

    fun openChatDialog() {
        _isChatDialogOpen.value = true
    }

    fun dismissChatDialog() {
        _isChatDialogOpen.value = false
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = AgirChatMessage(text = userText, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg
        isChatLoading.value = true

        viewModelScope.launch {
            val response = geminiService.queryAgir(
                userMessage = userText,
                personalityPrompt = "You are Agir, the warm, non-judgmental AI companion and owner of 'Trash Content Safe World'. Style: ${_personalityMode.value.styleDescription}.",
                currentStreak = userStreakDays.value,
                totalPoints = totalEarnedPoints.value
            )
            isChatLoading.value = false
            val agirMsg = AgirChatMessage(text = response, isUser = false)
            _chatMessages.value = _chatMessages.value + agirMsg
        }
    }
}
