package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RewardVoucherEntity
import com.example.ui.theme.EcoMint
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark

@Composable
fun RewardsMarketplaceScreen(
    vouchers: List<RewardVoucherEntity>,
    userPoints: Int,
    streakDays: Int,
    onSelectVoucher: (RewardVoucherEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredVouchers = remember(vouchers, selectedCategory) {
        if (selectedCategory == "All") vouchers else vouchers.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp)
            .testTag("rewards_marketplace_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Wallet & Tree Progress Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(EcoPrimaryDark, EcoPrimary, EcoMint)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "พอยต์ความยั่งยืนของเธอ (Wallet Balance)",
                                    fontSize = 12.sp,
                                    color = Color(0xFFD1FAE5)
                                )
                                Text(
                                    text = "$userPoints pt",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0x33FFFFFF)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Streak x${String.format("%.1f", 1.0f + (streakDays * 0.1f))}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Green Tree Tier Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Park,
                                    contentDescription = null,
                                    tint = Color(0xFFD1FAE5),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ระดับ: Urban Forest Guardian (Lv. 3)",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "เป้าหมาย: 200 pt",
                                fontSize = 11.sp,
                                color = Color(0xFFD1FAE5)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { (userPoints % 200) / 200f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFFFBBF24),
                            trackColor = Color(0x44FFFFFF)
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val categories = listOf("All", "Food", "Mobility", "Eco Living", "Fashion")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EcoPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White
                        )
                    )
                }
            }
        }

        // Header Title
        item {
            Text(
                text = "เวาเชอร์และสิทธิพิเศษจากพาร์ทเนอร์รักษ์โลก (${filteredVouchers.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
        }

        // Voucher Items
        items(filteredVouchers, key = { it.id }) { voucher ->
            VoucherCard(
                voucher = voucher,
                canAfford = userPoints >= voucher.pointsCost,
                onClick = { onSelectVoucher(voucher) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun VoucherCard(
    voucher: RewardVoucherEntity,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("voucher_card_${voucher.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Icon Avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        when {
                            voucher.isRedeemed -> Color(0xFFE2E8F0)
                            voucher.isClaimed -> Color(0xFFDCFCE7)
                            else -> Color(0xFFF1F5F9)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (voucher.iconType) {
                        "coffee" -> Icons.Default.Coffee
                        "train" -> Icons.Default.Train
                        "storefront" -> Icons.Default.Store
                        "shopping_bag" -> Icons.Default.LocalMall
                        else -> Icons.Default.CardGiftcard
                    },
                    contentDescription = null,
                    tint = if (voucher.isClaimed) EcoPrimary else Color(0xFF475569),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voucher.brandName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EcoPrimary
                )
                Text(
                    text = voucher.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
                Text(
                    text = voucher.subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Cost or Status Pill
            if (voucher.isRedeemed) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "ใช้แล้ว",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            } else if (voucher.isClaimed) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = EcoPrimaryDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "เปิด QR",
                            color = EcoPrimaryDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (canAfford) Color(0xFFECFDF5) else Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "${voucher.pointsCost} pt",
                        color = if (canAfford) EcoPrimaryDark else Color(0xFF94A3B8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
