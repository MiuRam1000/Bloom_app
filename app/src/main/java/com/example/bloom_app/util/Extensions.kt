package com.example.bloom_app.util

import com.example.bloom_app.data.local.entity.DiscoveryEntity
import com.example.bloom_app.domaine.model.Discovery


// Convertit une Entity Room → Modèle domaine propre (utilisé partout dans les ViewModels)
fun DiscoveryEntity.toDomain() = Discovery(
    id = id,
    userId = userId,
    name = name,
    summary = summary,
    imagePath = imagePath,
    timestamp = timestamp
)

// Convertit une liste d’Entity → liste de modèle domaine (pratique avec les Flow<List<…>>)
fun List<DiscoveryEntity>.toDomain() = map { it.toDomain() }