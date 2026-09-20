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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.data.model.AgirPersonalityMode
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark

@Composable
fun SettingsScreen(
    personalityMode: AgirPersonalityMode,
    isFaceBlurActive: Boolean,
    isPlateBlurActive: Boolean,
    onSelectPersonality: (AgirPersonalityMode) -> Unit,
    onToggleFaceBlur: () -> Unit,
    onTogglePlateBlur: () -> Unit,
    modifier: Modifier = Modifier
) {
    val apiKeyConfigured = try {
        BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"
    } catch (e: Throwable) {
        false
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section: Agir AI Personality Mode
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECFDF5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = null,
                                tint = EcoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "บุคลิกภาพ AI Companion (Agir Persona)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "ปรับสไตล์การพูดและการให้กำลังใจของ Agir",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AgirPersonalityMode.entries.forEach { mode ->
                        val isSelected = personalityMode == mode
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSelectPersonality(mode) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mode.titleTh,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) EcoPrimaryDark else Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = mode.styleDescription,
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = EcoPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Security, Privacy & PDPA
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ความเป็นส่วนตัว & มาตรฐานความปลอดภัย (PDPA/GDPR)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "เซนเซอร์และระบบเบลออัตโนมัติก่อนส่งข้อมูล",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Face blur switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ระบบเบลอใบหน้าอัตโนมัติ (Automated Face Blurring)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "ตรวจจับและเบลอใบหน้าบุคคลในเฟรมกล้องแบบ Edge-AI เรียลไทม์",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = isFaceBlurActive,
                            onCheckedChange = { onToggleFaceBlur() },
                            colors = SwitchDefaults.colors(checkedThumbColor = EcoPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Plate blur switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "เบลอป้ายทะเบียนยานพาหนะ (License Plate Redaction)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "ป้องกันการบันทึกข้อมูลส่วนบุคคลในพื้นที่สาธารณะ",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = isPlateBlurActive,
                            onCheckedChange = { onTogglePlateBlur() },
                            colors = SwitchDefaults.colors(checkedThumbColor = EcoPrimary)
                        )
                    }
                }
            }
        }

        // Section: Gemini AI Brain Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Gemini AI Engine (Agir Cognitive Core)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "ขับเคลื่อนโมเดล gemini-3.5-flash",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (apiKeyConfigured) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = if (apiKeyConfigured) "Connected" else "Local Smart AI Mode",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (apiKeyConfigured) EcoPrimaryDark else Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (apiKeyConfigured) {
                            "API Key พร้อมใช้งาน: Agir เชื่อมต่อกับ Gemini REST API ในการตอบคำถาม สนับสนุนทางจิตใจ และวิเคราะห์ข้อมูลความยั่งยืนแบบเรียลไทม์"
                        } else {
                            "โหมด AI จำลองอัจฉริยะ (Built-in Cognitive Engine): พร้อมให้คำแนะนำและพูดคุยใน Safe Space ทันที สามารถเพิ่ม GEMINI_API_KEY ใน AI Studio Secrets Panel เพื่อใช้งานโมเดล Cloud เต็มรูปแบบได้ทุกเมื่อ"
                        },
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // About Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Trash Content Safe World • v1.0 Production",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Empowering Gen Z Civic Movement with AI Companion Agir",
                    fontSize = 10.sp,
                    color = Color(0xFFCBD5E1)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
