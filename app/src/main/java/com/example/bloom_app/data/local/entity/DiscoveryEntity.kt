package com.example.bloom_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discoveries")
data class DiscoveryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: String,                    // UID Firebase
    val name: String,                      // Nom identifié par l'IA
    val summary: String,                   // Résumé/Fait IA (2 phrases fun)
    val imagePath: String,                 // Chemin interne de l'image
    val timestamp: Long = System.currentTimeMillis()
)