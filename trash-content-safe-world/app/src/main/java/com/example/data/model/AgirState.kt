package com.example.data.model

enum class AgirMood(val emoji: String, val statusDescTh: String) {
    JOYFUL("✨", "กำลังตื่นเต้นกับคอนเทนต์ดีๆ!"),
    PROUD("🏆", "ภูมิใจในตัวเธอมากๆ"),
    ENCOURAGING("🌱", "ทำดีไม่ต้องเขิน เป็นตัวเองได้เต็มที่!"),
    CURIOUS("🔍", "อยากรู้จังว่าวันนี้เจอขยะอะไรบ้าง"),
    ANALYZING("⚡", "กำลังสแกนแยกประเภทด้วย AI")
}

enum class AgirPersonalityMode(val titleTh: String, val styleDescription: String) {
    GEN_Z_BUDDY("เพื่อนซี้ Gen Z (Real & Fun)", "ภาษาเป็นกันเอง เฮฮา ไม่มีคำตัดสิน อบอุ่นสุดใจ"),
    GENTLE_MENTOR("พี่เลี้ยงสายละมุน (Gentle & Safe)", "อบอุ่น ให้กำลังใจ สร้างความปลอดภัยทางใจ"),
    ECO_CHAMP("แชมป์นักกู้โลก (Action-Packed)", "พลังล้น สดใส เน้นสถิติและการลดคาร์บอน")
}

data class AgirChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
