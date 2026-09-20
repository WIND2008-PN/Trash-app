package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgirMood
import com.example.ui.theme.EcoMint
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark

@Composable
fun AgirCompanionBar(
    mood: AgirMood,
    speechText: String,
    streakDays: Int,
    points: Int,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gentle floating breathing animation for Agir
    val infiniteTransition = rememberInfiniteTransition(label = "agir_bobbing")
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing_y"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onOpenChat() }
            .testTag("agir_companion_bar"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Row: Agir Avatar + Status & Streak Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Agir Avatar with gentle bobbing
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(0, bobbingOffset.toInt()) }
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(EcoPrimary, EcoMint)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mood.emoji,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Agir (คู่หู Safe World)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = EcoPrimaryDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EcoMint.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = EcoPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "Online",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EcoPrimary
                                    )
                                }
                            }
                        }

                        Text(
                            text = mood.statusDescTh,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Streak & Points pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.clickable { onOpenChat() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥 ${streakDays}d",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA580C)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🌱 $points pt",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Speech Bubble Container
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFECFDF5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Agir Talk",
                        tint = EcoPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = speechText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF064E3B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "คุยกับ Agir ›",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EcoPrimary
                    )
                }
            }
        }
    }
}
