package com.example.bloom_app.util

import com.example.bloom_app.data.local.entity.DiscoveryEntity
import com.example.bloom_app.domaine.model.Discovery

// Discovery → DiscoveryEntity (pour insert/delete)
fun Discovery.toEntity(): DiscoveryEntity {
    return DiscoveryEntity(
        id = this.id,
        userId = this.userId,
        name = this.name,
        summary = this.summary,
        imagePath = this.imagePath,
        timestamp = this.timestamp
    )
}

// DiscoveryEntity → Discovery (pour affichage)
fun DiscoveryEntity.toDomain(): Discovery {
    return Discovery(
        id = this.id,
        userId = this.userId,
        name = this.name,
        summary = this.summary,
        imagePath = this.imagePath,
        timestamp = this.timestamp
    )
}

// List<DiscoveryEntity> → List<Discovery>
fun List<DiscoveryEntity>.toDomain(): List<Discovery> {
    return this.map { it.toDomain() }
}
