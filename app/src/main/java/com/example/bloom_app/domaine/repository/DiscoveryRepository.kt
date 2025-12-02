package com.example.bloom_app.domaine.repository

import com.example.bloom_app.domaine.model.Discovery
import kotlinx.coroutines.flow.Flow

interface DiscoveryRepository {

    fun getDiscoveriesForUser(userId: String): Flow<List<Discovery>>

    suspend fun addDiscovery(discovery: Discovery)

    suspend fun deleteDiscovery(discoveryId: Long, userId: String)
}
