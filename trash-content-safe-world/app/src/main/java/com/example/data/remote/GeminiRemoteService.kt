package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRemoteService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun queryAgir(
        userMessage: String,
        personalityPrompt: String,
        currentStreak: Int,
        totalPoints: Int
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Intelligent fallback engine simulating Agir's genuine presence & persona
            return@withContext generateLocalAgirResponse(userMessage, currentStreak, totalPoints)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val systemContext = """
                $personalityPrompt
                Context: User has current eco-action streak of $currentStreak days and $totalPoints points.
                Platform: "Trash Content Safe World" (เปลี่ยน 'คอนเทนต์ขยะ' เป็นพลังบวกทางสังคม).
                Protect psychological safety: Never judge, shame, or lecture. Be an encouraging, cool, empathetic Gen Z buddy.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemContext\n\nUser: $userMessage\nAgir:"))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 250)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text")
                    if (!text.isNullOrBlank()) {
                        return@withContext text.trim()
                    }
                }
            }
            // If API returned error (e.g. quota or bad key), fallback smoothly
            generateLocalAgirResponse(userMessage, currentStreak, totalPoints)
        } catch (e: Exception) {
            Log.e("GeminiRemoteService", "Error calling Gemini: ${e.message}")
            generateLocalAgirResponse(userMessage, currentStreak, totalPoints)
        }
    }

    private fun generateLocalAgirResponse(message: String, streak: Int, points: Int): String {
        val lower = message.lowercase()
        return when {
            lower.contains("เขิน") || lower.contains("อาย") || lower.contains("คนมอง") -> {
                "เข้าใจมากๆ เลยนะ! ช่วงแรกๆ ใครเก็บขยะก็กลัวคนมองว่า 'ทำคอนเทนต์เอาหน้า' แต่รู้มั้ย... คอนเทนต์ขยะของเธอใน Safe World เนี่ย มันคือพลังบวกที่ช่วยโลกจริงๆ ไม่ต้องสนใจสายตาใครเลย Agir อยู่ข้างๆ เสมอ สู้ไปด้วยกันนะ! 🌱✨"
            }
            lower.contains("ขวด") || lower.contains("pet") || lower.contains("พลาสติก") -> {
                "ขวด PET ถือเป็นสมบัติรีไซเคิลเลยนะ! ทริคง่ายๆ ก่อนทิ้ง: เทน้ำให้หมด บีบให้แบน แล้วแยกฝาออก จะช่วยประหยัดพื้นที่จัดเก็บและลดการปล่อยก๊าซเรือนกระจกได้เยอะมากเลย ได้แต้มโบนัสคูณ 1.5 ด้วยนะ! 🧴💚"
            }
            lower.contains("คะแนน") || lower.contains("แต้ม") || lower.contains("พอยต์") -> {
                "ตอนนี้เธอสะสมไปแล้ว $points พอยต์ และ Streak ต่อเนื่อง $streak วันแล้วนะ! อีกนิดเดียวก็แลกเวาเชอร์เครื่องดื่มฟรีจาก Inthanin หรือบัตร BTS Green Pass ได้แล้ว รีบไปที่แท็บรางวัลดูได้เลย! ☕🎫"
            }
            lower.contains("เหนื่อย") || lower.contains("ท้อ") -> {
                "กอดๆ นะคนเก่ง วันนี้เหนื่อยก็พักก่อนได้เลย ไม่ต้องกดดันตัวเองนะ แค่คิดจะแยกขยะชิ้นเดียวก็ถือเป็นก้าวที่ยิ่งใหญ่มากๆ แล้ว Agir ภูมิใจในตัวเธอเสมอนะ 💚✨"
            }
            lower.contains("esg") || lower.contains("องค์กร") || lower.contains("bloomberg") || lower.contains("msci") -> {
                "ทุกแอคชั่นของพวกเราในแอปนี้ จะถูกแปลงเป็น Verified Scope 3 Waste Metrics และรายงานตรงสู่แดชบอร์ด ESG ตามมาตรฐาน Bloomberg & MSCI ด้วยนะ บริษัทพาร์ทเนอร์เอาข้อมูลไปทำรายงานความยั่งยืนได้จริง ไม่ใช่แค่เรื่องเล่นๆ แน่นอน!"
            }
            else -> {
                "Agir ได้ยินแล้วน้า! การที่เธอลงมือทำเพื่อสิ่งแวดล้อมวันนี้ มันมีความหมายมากจริงๆ สตรีค $streak วันของเธอเจ๋งสุดๆ อยากให้ Agir ช่วยแนะแนวเรื่องการแยกประเภทไหน ถามมาได้ตลอดเวลาเลยนะเพื่อนซี้! 🌍💪"
            }
        }
    }
}
