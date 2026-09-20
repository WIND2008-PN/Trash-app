package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FeedPostEntity
import com.example.data.model.RewardVoucherEntity
import com.example.data.model.WasteScanEntity

@Database(
    entities = [
        WasteScanEntity::class,
        FeedPostEntity::class,
        RewardVoucherEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wasteScanDao(): WasteScanDao
    abstract fun feedPostDao(): FeedPostDao
    abstract fun voucherDao(): VoucherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trash_safe_world.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
