package com.example.bloom_app.data.repository

import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.repository.DiscoveryRepository
import com.example.bloom_app.util.toDomain
import com.example.bloom_app.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiscoveryRepositoryImpl(
    private val discoveryDao: DiscoveryDao
) : DiscoveryRepository {

    override fun getDiscoveriesForUser(userId: String): Flow<List<Discovery>> {
        return discoveryDao.getAllForUser(userId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun addDiscovery(discovery: Discovery) {
        discoveryDao.insert(discovery.toEntity())
    }

    override suspend fun deleteDiscovery(discoveryId: Long, userId: String) {
        discoveryDao.deleteById(discoveryId, userId)
    }
}
