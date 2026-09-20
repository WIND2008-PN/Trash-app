package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.WasteScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteScanDao {
    @Query("SELECT * FROM waste_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<WasteScanEntity>>

    @Query("SELECT COUNT(*) FROM waste_scans")
    fun getScansCount(): Flow<Int>

    @Query("SELECT SUM(calculatedPoints) FROM waste_scans")
    fun getTotalEarnedPoints(): Flow<Int?>

    @Query("SELECT SUM(co2OffsetKg) FROM waste_scans")
    fun getTotalCo2Offset(): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: WasteScanEntity): Long

    @Query("DELETE FROM waste_scans")
    suspend fun clearAll()
}
