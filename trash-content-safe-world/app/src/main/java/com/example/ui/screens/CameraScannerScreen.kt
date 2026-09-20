package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.WasteCategory
import com.example.ui.ScannerHudState
import com.example.ui.theme.EcoMint
import com.example.ui.theme.EcoPrimary
import com.example.ui.theme.EcoPrimaryDark

@Composable
fun CameraScannerScreen(
    hudState: ScannerHudState,
    streakDays: Int,
    previewPoints: Int,
    onCategorySelected: (WasteCategory) -> Unit,
    onVolumeChanged: (Int) -> Unit,
    onToggleFaceBlur: () -> Unit,
    onTogglePlateBlur: () -> Unit,
    onConfirmAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var simulationMode by remember { mutableStateOf(false) }
    var isTorchOn by remember { mutableStateOf(false) }
    var cameraLensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var cameraError by remember { mutableStateOf<String?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            simulationMode = true
        }
    }

    // HUD Laser / Pulse Scanning animation
    val infiniteTransition = rememberInfiniteTransition(label = "hud_scan")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_line_y"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(bottom = 12.dp)
            .testTag("camera_scanner_screen")
    ) {
        // Top Camera HUD Telemetry Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x33000000)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasCameraPermission && !simulationMode && cameraError == null) Color(0xFF10B981)
                                else Color(0xFF38BDF8)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasCameraPermission && !simulationMode && cameraError == null) "CAMX LIVE (30 FPS)" else "EDGE-CV SIM (30 FPS)",
                        color = if (hasCameraPermission && !simulationMode && cameraError == null) Color(0xFF34D399) else Color(0xFF38BDF8),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick Camera Controls & Privacy Blur Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (hasCameraPermission && !simulationMode) {
                    // Torch / Flashlight Toggle
                    Surface(
                        shape = CircleShape,
                        color = if (isTorchOn) Color(0x44FBBF24) else Color(0x22FFFFFF)
                    ) {
                        IconButton(
                            onClick = {
                                val next = !isTorchOn
                                isTorchOn = next
                                cameraControl?.enableTorch(next)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Toggle Torch",
                                tint = if (isTorchOn) Color(0xFFFBBF24) else Color(0xFFCBD5E1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Lens Facing Switch
                    Surface(
                        shape = CircleShape,
                        color = Color(0x22FFFFFF)
                    ) {
                        IconButton(
                            onClick = {
                                cameraLensFacing = if (cameraLensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch Camera",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Privacy blur indicator toggle
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (hudState.isFaceBlurActive) Color(0x3310B981) else Color(0x33EF4444)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Privacy Blur",
                            tint = if (hudState.isFaceBlurActive) Color(0xFF34D399) else Color(0xFFF87171),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hudState.isFaceBlurActive) "Blur: ON (PDPA)" else "Blur: OFF",
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Camera Viewfinder Canvas with Bounding Box & Bounding Label
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF134E4A))
                    )
                )
        ) {
            // Live CameraX Preview View
            if (hasCameraPermission && !simulationMode) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.surfaceProvider = previewView.surfaceProvider
                                }
                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(cameraLensFacing)
                                    .build()
                                cameraProvider.unbindAll()
                                val camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                                cameraControl = camera.cameraControl
                                cameraError = null
                            } catch (exc: Exception) {
                                cameraError = exc.message
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = {
                        // Rebind when cameraLensFacing changes
                        val ctx = it.context
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also { p ->
                                    p.surfaceProvider = it.surfaceProvider
                                }
                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(cameraLensFacing)
                                    .build()
                                cameraProvider.unbindAll()
                                val camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                                cameraControl = camera.cameraControl
                            } catch (_: Exception) { }
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                )
            } else if (!hasCameraPermission && !simulationMode) {
                // Permission Request Card
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0x3310B981),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera Permission",
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ขออนุญาตเข้าถึงกล้องเพื่อสแกน",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ใช้งาน CameraX และ Edge-CV ในการตรวจจับและวิเคราะห์ขยะเพื่อโลกแบบเรียลไทม์",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = EcoPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("เปิดใช้งานกล้อง (Enable Camera)")
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = { simulationMode = true }) {
                        Text("ใช้โหมดจำลองขยะ (Simulation Mode)", color = Color(0xFF38BDF8), fontSize = 12.sp)
                    }
                }
            }
            // Simulated Bounding Box and Scan Lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Viewfinder crosshairs corners
                val cornerLen = 28.dp.toPx()
                val cornerStroke = 3.dp.toPx()
                val cornerColor = Color(0xFF34D399)

                // Top-Left
                drawLine(cornerColor, Offset(16f, 16f), Offset(16f + cornerLen, 16f), cornerStroke)
                drawLine(cornerColor, Offset(16f, 16f), Offset(16f, 16f + cornerLen), cornerStroke)

                // Top-Right
                drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f - cornerLen, 16f), cornerStroke)
                drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f, 16f + cornerLen), cornerStroke)

                // Bottom-Left
                drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f + cornerLen, height - 16f), cornerStroke)
                drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f, height - 16f - cornerLen), cornerStroke)

                // Bottom-Right
                drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f - cornerLen, height - 16f), cornerStroke)
                drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f, height - 16f - cornerLen), cornerStroke)

                // Bounding Box in the center
                val boxWidth = width * 0.70f
                val boxHeight = height * 0.52f
                val boxLeft = (width - boxWidth) / 2
                val boxTop = (height - boxHeight) / 2

                drawRoundRect(
                    color = Color(hudState.selectedCategory.colorHex),
                    topLeft = Offset(boxLeft, boxTop),
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    style = Stroke(width = 2.5.dp.toPx())
                )

                // Animated Laser Line
                val currentLineY = boxTop + (boxHeight * scanLineY)
                drawLine(
                    color = Color(0xFF34D399).copy(alpha = 0.85f),
                    start = Offset(boxLeft + 8.dp.toPx(), currentLineY),
                    end = Offset(boxLeft + boxWidth - 8.dp.toPx(), currentLineY),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Simulated Face Blur Overlay when active
            if (hudState.isFaceBlurActive) {
                Box(
                    modifier = Modifier
                        .padding(top = 28.dp, end = 28.dp)
                        .align(Alignment.TopEnd)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0x9964748B))
                        .blur(8.dp)
                        .border(1.dp, Color(0x66FFFFFF), CircleShape)
                )
            }

            // Live AI Detection Overlay Chip on top of the bounding box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xDD064E3B),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 120.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${hudState.selectedCategory.displayNameTh} (${hudState.confidencePct}%)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Bottom Floating Telemetry & Physics Proof Pill
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xB30F172A),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Anti-Fraud: Physics Pickup Verified • Zero Replay",
                        color = Color(0xFFE2E8F0),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Control Panel: Category Selector Chips
        Text(
            text = "เลือกหรือตรวจจับประเภทขยะ (AI Waste Classification):",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WasteCategory.entries.forEach { category ->
                val isSelected = hudState.selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = category.displayNameTh,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EcoPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF1E293B),
                        labelColor = Color(0xFFCBD5E1)
                    )
                )
            }
        }

        // Live Mathematical Formula Points Accumulation Gauge
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Formula Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tokenomics & Points Calculation",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Points = (V × W) + Streak - Fraud",
                            color = Color(0xFF34D399),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Large points badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EcoPrimary
                    ) {
                        Text(
                            text = "+$previewPoints pt",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Volume slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ปริมาตรประเมิน (Volume): ${hudState.volumeMl} ml",
                        color = Color(0xFFE2E8F0),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "น้ำหนักรีไซเคิล: x${hudState.selectedCategory.recyclabilityWeight}",
                        color = Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = hudState.volumeMl.toFloat(),
                    onValueChange = { onVolumeChanged(it.toInt()) },
                    valueRange = 100f..1500f,
                    steps = 13,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF34D399),
                        activeTrackColor = EcoPrimary,
                        inactiveTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.height(26.dp)
                )

                // Streak & Risk Telemetry info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Consistency Multiplier: +${String.format("%.1f", streakDays * 2.5f)} (Streak ${streakDays}d)",
                        color = Color(0xFFFBBF24),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Fraud Score: ${hudState.fraudRiskScore}",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main Confirm Action Button
        Button(
            onClick = onConfirmAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp)
                .testTag("confirm_waste_scan_button"),
            colors = ButtonDefaults.buttonColors(containerColor = EcoPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ยืนยันการแยกขยะ (รับ +$previewPoints พอยต์)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
