package com.example.bloom_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.data.local.entity.DiscoveryEntity

@Database(
    entities = [DiscoveryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BloomDatabase : RoomDatabase() {
    abstract fun discoveryDao(): DiscoveryDao
}