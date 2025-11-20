package com.example.bloom_app.data.local.dao

import androidx.room.*
import com.example.bloom_app.data.local.entity.DiscoveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(discovery: DiscoveryEntity)

    @Query("SELECT * FROM discoveries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllForUser(userId: String): Flow<List<DiscoveryEntity>>

    @Delete
    suspend fun delete(discovery: DiscoveryEntity)

    @Query("DELETE FROM discoveries WHERE id = :id AND userId = :userId")
    suspend fun deleteById(id: Long, userId: String)

    @Query("DELETE FROM discoveries WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)
}