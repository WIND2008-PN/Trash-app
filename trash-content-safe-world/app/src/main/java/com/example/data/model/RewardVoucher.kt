package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_vouchers")
data class RewardVoucherEntity(
    @PrimaryKey
    val id: String,
    val brandName: String,
    val title: String,
    val subtitle: String,
    val pointsCost: Int,
    val category: String,
    val terms: String,
    val iconType: String,
    val isClaimed: Boolean = false,
    val isRedeemed: Boolean = false,
    val dynamicCode: String = "",
    val claimedTimestamp: Long = 0L,
    val expiresTimestamp: Long = 0L
)
