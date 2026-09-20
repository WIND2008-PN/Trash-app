package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CorporateEsgSnapshot
import com.example.data.model.DistrictHotspot
import com.example.data.model.EsgDataProvider
import com.example.ui.theme.EcoMint
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark

@Composable
fun EsgDashboardScreen(
    snapshot: CorporateEsgSnapshot,
    selectedProvider: EsgDataProvider,
    districtHotspots: List<DistrictHotspot>,
    onSelectProvider: (EsgDataProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp)
            .testTag("esg_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // API Data Provider Selector (Bloomberg vs MSCI)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "B2B ESG & Capital Market Integration",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = EcoPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Live Stream",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EcoPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        EsgDataProvider.entries.forEach { provider ->
                            val isSelected = selectedProvider == provider
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectProvider(provider) },
                                label = {
                                    Text(
                                        text = provider.title,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EcoPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFFF1F5F9)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = selectedProvider.description,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Hero KPI Grid (Scope 3 Waste, Plastic, CO2e, Rating)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Scope 3 Waste Reduced
                    MetricCard(
                        title = "Scope 3 Waste Reduced",
                        value = "${String.format("%.1f", snapshot.totalScope3WasteKg)} kg",
                        subtext = "Verified diverted waste",
                        icon = Icons.Default.Delete,
                        accentColor = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f)
                    )

                    // Plastic Diverted
                    MetricCard(
                        title = "Plastic Diverted",
                        value = "${String.format("%.1f", snapshot.plasticDivertedKg)} kg",
                        subtext = "Kept out of landfills/ocean",
                        icon = Icons.Default.Recycling,
                        accentColor = EcoPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // CO2e Offsets
                    MetricCard(
                        title = "Carbon Offsets (CO2e)",
                        value = "${String.format("%.1f", snapshot.co2eOffsetKg)} kg",
                        subtext = "Avoided Scope 3 emissions",
                        icon = Icons.Default.Co2,
                        accentColor = Color(0xFFD97706),
                        modifier = Modifier.weight(1f)
                    )

                    // Rating Score
                    MetricCard(
                        title = snapshot.provider.ratingLabel,
                        value = snapshot.scoreOrRating,
                        subtext = "Circularity: ${snapshot.circularityRate}%",
                        icon = Icons.Default.Assessment,
                        accentColor = Color(0xFF7C3AED),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Smart City Municipal Waste Hotspot Aggregation
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
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = null,
                                tint = EcoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Smart City Municipal Heatmap (Bangkok Pilot)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "BMA API V2",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "ข้อมูลความหนาแน่นขยะและการจัดเก็บตามเขตเมือง เพื่อวางแผนโลจิสติกส์รีไซเคิล",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    districtHotspots.forEach { hotspot ->
                        DistrictHotspotRow(hotspot = hotspot)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                maxLines = 1
            )

            Text(
                text = subtext,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun DistrictHotspotRow(hotspot: DistrictHotspot) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "เขต${hotspot.nameTh} (${hotspot.nameEn})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "ขยะเด่น: ${hotspot.primaryWasteType} • ชุมชน ${hotspot.participantCount} คน",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (hotspot.statusLevel) {
                        "High Need" -> Color(0xFFFEE2E2)
                        "Active Clean" -> Color(0xFFFEF3C7)
                        else -> Color(0xFFDCFCE7)
                    }
                ) {
                    Text(
                        text = hotspot.statusLevel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (hotspot.statusLevel) {
                            "High Need" -> Color(0xFFDC2626)
                            "Active Clean" -> Color(0xFFD97706)
                            else -> EcoPrimaryDark
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clearance Rate Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Clearance Efficiency: ${hotspot.clearanceRatePct}%",
                    fontSize = 11.sp,
                    color = Color(0xFF475569)
                )
                Text(
                    text = "${hotspot.wasteDensityKgDay.toInt()} kg/วัน",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { hotspot.clearanceRatePct / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EcoPrimary,
                trackColor = Color(0xFFE2E8F0)
            )
        }
    }
}
