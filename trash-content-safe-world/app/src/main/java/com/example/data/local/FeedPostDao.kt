package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FeedPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedPostDao {
    @Query("SELECT * FROM feed_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<FeedPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: FeedPostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<FeedPostEntity>)

    @Update
    suspend fun updatePost(post: FeedPostEntity)

    @Query("UPDATE feed_posts SET inspireCount = inspireCount + :delta, hasUserInspire = :reacted WHERE id = :id")
    suspend fun toggleInspire(id: Long, delta: Int, reacted: Boolean)

    @Query("UPDATE feed_posts SET heartCount = heartCount + :delta, hasUserHeart = :reacted WHERE id = :id")
    suspend fun toggleHeart(id: Long, delta: Int, reacted: Boolean)

    @Query("UPDATE feed_posts SET sproutCount = sproutCount + :delta, hasUserSprout = :reacted WHERE id = :id")
    suspend fun toggleSprout(id: Long, delta: Int, reacted: Boolean)
}
