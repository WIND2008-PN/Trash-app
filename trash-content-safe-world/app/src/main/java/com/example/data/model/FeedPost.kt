package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_posts")
data class FeedPostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorName: String,
    val authorTag: String,
    val caption: String,
    val wasteCategory: String,
    val pointsEarned: Int,
    val co2OffsetKg: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val inspireCount: Int = 0,
    val heartCount: Int = 0,
    val sproutCount: Int = 0,
    val hasUserInspire: Boolean = false,
    val hasUserHeart: Boolean = false,
    val hasUserSprout: Boolean = false,
    val agirSafeCertified: Boolean = true,
    val district: String = "กรุงเทพมหานคร"
)
