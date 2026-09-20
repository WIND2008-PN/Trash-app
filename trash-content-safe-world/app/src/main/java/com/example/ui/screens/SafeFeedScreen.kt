package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.FeedPostEntity
import com.example.ui.theme.EcoMint
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SafeFeedScreen(
    posts: List<FeedPostEntity>,
    onToggleReaction: (Long, String, Boolean) -> Unit,
    onCreatePost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newPostText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("safe_feed_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Banner explaining the Civic Movement
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EcoMint.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = EcoPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Agir Psychological Safety Gatekeeper",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EcoPrimaryDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "คอนเทนต์ขยะเพื่อโลก (Safe Space Movement)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "เปลี่ยนคำว่า 'คอนเทนต์ขยะ' ให้กลายเป็นพลังบวกของคนรุ่นใหม่ ไม่มีการบูลลี่ ไม่มีตัวเลขแสดงความล้มเหลว มีเพียง 3 เวกเตอร์พลังบวก: Inspire • Heart • Sprout 🌱",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // List of Civic Safe Feed Posts
            items(posts, key = { it.id }) { post ->
                SafeFeedPostCard(
                    post = post,
                    onToggleInspire = { onToggleReaction(post.id, "INSPIRE", post.hasUserInspire) },
                    onToggleHeart = { onToggleReaction(post.id, "HEART", post.hasUserHeart) },
                    onToggleSprout = { onToggleReaction(post.id, "SPROUT", post.hasUserSprout) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Floating Action Button to Share Action
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("create_post_fab"),
            containerColor = EcoPrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Post")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "แชร์คอนเทนต์ขยะ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text(
                    text = "แชร์พลังบวกสู่ Safe Feed 🌱",
                    fontWeight = FontWeight.Bold,
                    color = EcoPrimaryDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "โพสต์นี้จะผ่านการคัดกรองอัตโนมัติด้วย AI Agir เพื่อรักษาความปลอดภัยทางจิตใจของคอมมูนิตี้",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPostText,
                        onValueChange = { newPostText = it },
                        placeholder = { Text("วันนี้เก็บแยกขยะอะไรมา รู้สึกยังไงบ้าง เล่าให้ฟังได้เลยนะ...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("create_post_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPostText.isNotBlank()) {
                            onCreatePost(newPostText)
                            newPostText = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoPrimary)
                ) {
                    Text("เผยแพร่สู่ Safe Feed")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCreateDialog = false },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text("ยกเลิก", color = Color(0xFF64748B))
                }
            }
        )
    }
}

@Composable
fun SafeFeedPostCard(
    post: FeedPostEntity,
    onToggleInspire: () -> Unit,
    onToggleHeart: () -> Unit,
    onToggleSprout: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm • d MMM", Locale.forLanguageTag("th-TH")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feed_post_card_${post.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Author & Verification Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(EcoPrimary, EcoMint))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            if (post.agirSafeCertified) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Safe Verified",
                                        tint = EcoPrimary,
                                        modifier = Modifier
                                            .padding(3.dp)
                                            .size(10.dp)
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${post.district} • ${dateFormat.format(Date(post.timestamp))}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Points & Carbon Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFECFDF5)
                ) {
                    Text(
                        text = "+${post.pointsEarned} pt",
                        color = EcoPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Caption
            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1E293B),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Action Telemetry Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ขยะที่แยก: ${post.wasteCategory}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "ลดคาร์บอน: -${String.format("%.2f", post.co2OffsetKg)} kg CO2e",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Positive-Only Reaction Vectors: Inspire, Heart, Sprout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 1. Inspire (Star)
                ReactionButton(
                    icon = Icons.Default.Star,
                    label = "Inspire",
                    count = post.inspireCount,
                    isActive = post.hasUserInspire,
                    activeColor = Color(0xFFF59E0B),
                    onClick = onToggleInspire
                )

                // 2. Heart
                ReactionButton(
                    icon = Icons.Default.Favorite,
                    label = "Heart",
                    count = post.heartCount,
                    isActive = post.hasUserHeart,
                    activeColor = Color(0xFFEF4444),
                    onClick = onToggleHeart
                )

                // 3. Sprout
                ReactionButton(
                    icon = Icons.Default.Spa,
                    label = "Sprout",
                    count = post.sproutCount,
                    isActive = post.hasUserSprout,
                    activeColor = EcoPrimary,
                    onClick = onToggleSprout
                )
            }
        }
    }
}

@Composable
fun ReactionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: Int,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isActive) activeColor.copy(alpha = 0.15f) else Color(0xFFF8FAFC),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) activeColor else Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$count",
                fontSize = 12.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) activeColor else Color(0xFF64748B)
            )
        }
    }
}
