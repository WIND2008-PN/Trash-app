package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.RewardVoucherEntity
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark
import kotlinx.coroutines.delay

@Composable
fun DynamicVoucherModal(
    voucher: RewardVoucherEntity,
    userPoints: Int,
    onClaim: (RewardVoucherEntity) -> Unit,
    onRedeem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var remainingSeconds by remember { mutableIntStateOf(15 * 60) }

    LaunchedEffect(voucher.isClaimed) {
        if (voucher.isClaimed && !voucher.isRedeemed) {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds -= 1
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("dynamic_voucher_modal"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Brand Title
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFECFDF5)
                ) {
                    Text(
                        text = voucher.brandName,
                        style = MaterialTheme.typography.labelLarge,
                        color = EcoPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = voucher.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = voucher.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (voucher.isRedeemed) {
                    // Redeemed state
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "✅ ใช้สิทธิเรียบร้อยแล้ว",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "ขอบคุณที่ช่วยลดขยะกับ Safe World",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else if (voucher.isClaimed) {
                    // Dynamic QR / Barcode Display
                    Text(
                        text = "แสดงรหัสนี้แก่พนักงานที่จุดบริการ",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // QR Matrix representation
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(140.dp)) {
                            val step = size.width / 14f
                            // Pattern simulation for dynamic QR with unique hash
                            val seed = (voucher.dynamicCode.hashCode().toLong() and 0xFFFFFF).toInt()
                            for (x in 0..13) {
                                for (y in 0..13) {
                                    val isCorner = (x < 4 && y < 4) || (x > 9 && y < 4) || (x < 4 && y > 9)
                                    val isFilled = isCorner || ((x * 17 + y * 31 + seed) % 3 == 0)
                                    if (isFilled) {
                                        drawRect(
                                            color = Color(0xFF0F172A),
                                            topLeft = Offset(x * step, y * step),
                                            size = Size(step * 0.9f, step * 0.9f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dynamic Security Token String
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = voucher.dynamicCode.ifEmpty { "TCW-8492-2026" },
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = EcoPrimaryDark,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Countdown Timer
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFEA580C),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "รหัสหมดอายุใน: %02d:%02d".format(minutes, seconds),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEA580C)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onRedeem(voucher.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("redeem_voucher_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EcoPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("กดเพื่อยืนยันการใช้สิทธิ (หน้าร้าน)")
                    }
                } else {
                    // Not claimed yet: Claim Button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "เงื่อนไขการใช้:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = voucher.terms,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val canAfford = userPoints >= voucher.pointsCost
                    Button(
                        onClick = { onClaim(voucher) },
                        enabled = canAfford,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("claim_voucher_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoPrimary,
                            disabledContainerColor = Color(0xFFCBD5E1)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (canAfford) {
                            Text("ใช้ ${voucher.pointsCost} พอยต์ เพื่อแลกสิทธิ์")
                        } else {
                            Text("พอยต์ไม่เพียงพอ (ต้องการ ${voucher.pointsCost} pt)")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ปิดหน้าต่าง", color = Color(0xFF64748B))
                }
            }
        }
    }
}
