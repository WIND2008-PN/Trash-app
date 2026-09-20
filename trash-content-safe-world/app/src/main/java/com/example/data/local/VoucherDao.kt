package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RewardVoucherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {
    @Query("SELECT * FROM reward_vouchers ORDER BY isClaimed ASC, pointsCost ASC")
    fun getAllVouchers(): Flow<List<RewardVoucherEntity>>

    @Query("SELECT * FROM reward_vouchers WHERE isClaimed = 1 ORDER BY claimedTimestamp DESC")
    fun getClaimedVouchers(): Flow<List<RewardVoucherEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vouchers: List<RewardVoucherEntity>)

    @Update
    suspend fun updateVoucher(voucher: RewardVoucherEntity)

    @Query("UPDATE reward_vouchers SET isClaimed = 1, dynamicCode = :code, claimedTimestamp = :now, expiresTimestamp = :expires WHERE id = :id")
    suspend fun claimVoucher(id: String, code: String, now: Long, expires: Long)

    @Query("UPDATE reward_vouchers SET isRedeemed = 1 WHERE id = :id")
    suspend fun markRedeemed(id: String)
}
