package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.TrashSafeViewModel
import com.example.ui.components.AgirChatDialog
import com.example.ui.components.AgirCompanionBar
import com.example.ui.components.DynamicVoucherModal
import com.example.ui.screens.CameraScannerScreen
import com.example.ui.screens.EsgDashboardScreen
import com.example.ui.screens.RewardsMarketplaceScreen
import com.example.ui.screens.SafeFeedScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TrashSafeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TrashSafeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TrashSafeApp(viewModel: TrashSafeViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val agirMood by viewModel.agirMood.collectAsState()
    val agirSpeechBubble by viewModel.agirSpeechBubble.collectAsState()
    val streakDays by viewModel.userStreakDays.collectAsState()
    val totalPoints by viewModel.totalEarnedPoints.collectAsState()

    // Scanner state
    val hudState by viewModel.hudState.collectAsState()

    // Feed state
    val feedPosts by viewModel.feedPosts.collectAsState()

    // Rewards state
    val vouchers by viewModel.vouchers.collectAsState()
    val activeVoucherModal by viewModel.activeVoucherModal.collectAsState()

    // ESG state
    val selectedEsgProvider by viewModel.selectedEsgProvider.collectAsState()
    val esgSnapshot by viewModel.corporateEsgSnapshot.collectAsState()

    // Settings & Dialogs
    val personalityMode by viewModel.personalityMode.collectAsState()
    val isChatDialogOpen by viewModel.isChatDialogOpen.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    AppTab.SCANNER -> Icons.Default.CameraAlt
                                    AppTab.FEED -> Icons.Default.Favorite
                                    AppTab.REWARDS -> Icons.Default.ConfirmationNumber
                                    AppTab.ESG -> Icons.Default.BarChart
                                    AppTab.SETTINGS -> Icons.Default.Settings
                                },
                                contentDescription = tab.titleTh,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.titleTh,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EcoPrimary,
                            selectedTextColor = EcoPrimary,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B),
                            indicatorColor = Color(0xFFECFDF5)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Persistent Agir Companion Bar (Host & Safe Space Guide)
            AgirCompanionBar(
                mood = agirMood,
                speechText = agirSpeechBubble,
                streakDays = streakDays,
                points = totalPoints,
                onOpenChat = { viewModel.openChatDialog() }
            )

            // Screen Content View
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    AppTab.SCANNER -> CameraScannerScreen(
                        hudState = hudState,
                        streakDays = streakDays,
                        previewPoints = viewModel.calculateCurrentPointsPreview(),
                        onCategorySelected = { viewModel.updateScannerCategory(it) },
                        onVolumeChanged = { viewModel.updateScannerVolume(it) },
                        onToggleFaceBlur = { viewModel.toggleFaceBlur() },
                        onTogglePlateBlur = { viewModel.togglePlateBlur() },
                        onConfirmAction = { viewModel.confirmWasteAction() }
                    )

                    AppTab.FEED -> SafeFeedScreen(
                        posts = feedPosts,
                        onToggleReaction = { id, reaction, current ->
                            viewModel.toggleFeedReaction(id, reaction, current)
                        },
                        onCreatePost = { text -> viewModel.createFeedPost(text) }
                    )

                    AppTab.REWARDS -> RewardsMarketplaceScreen(
                        vouchers = vouchers,
                        userPoints = totalPoints,
                        streakDays = streakDays,
                        onSelectVoucher = { voucher -> viewModel.openVoucherDetail(voucher) }
                    )

                    AppTab.ESG -> EsgDashboardScreen(
                        snapshot = esgSnapshot,
                        selectedProvider = selectedEsgProvider,
                        districtHotspots = viewModel.districtHotspots,
                        onSelectProvider = { viewModel.selectEsgProvider(it) }
                    )

                    AppTab.SETTINGS -> SettingsScreen(
                        personalityMode = personalityMode,
                        isFaceBlurActive = hudState.isFaceBlurActive,
                        isPlateBlurActive = hudState.isPlateBlurActive,
                        onSelectPersonality = { viewModel.setPersonalityMode(it) },
                        onToggleFaceBlur = { viewModel.toggleFaceBlur() },
                        onTogglePlateBlur = { viewModel.togglePlateBlur() }
                    )
                }
            }
        }
    }

    // Full Safe-Space AI Chat Dialog with Agir
    if (isChatDialogOpen) {
        AgirChatDialog(
            messages = chatMessages,
            isLoading = isChatLoading,
            mood = agirMood,
            onSendMessage = { viewModel.sendChatMessage(it) },
            onDismiss = { viewModel.dismissChatDialog() }
        )
    }

    // Dynamic Barcode / QR Voucher Modal
    activeVoucherModal?.let { voucher ->
        DynamicVoucherModal(
            voucher = voucher,
            userPoints = totalPoints,
            onClaim = { viewModel.claimVoucher(it) },
            onRedeem = { viewModel.redeemVoucher(it) },
            onDismiss = { viewModel.dismissVoucherModal() }
        )
    }
}
