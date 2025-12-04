package com.example.bloom_app.domaine.usecase

import com.example.bloom_app.domaine.repository.DiscoveryRepository
import com.google.firebase.auth.FirebaseAuth

class DeleteDiscoveryUseCase(
    private val repository: DiscoveryRepository
) {

    suspend  fun invoke(discoveryId: Long) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        repository.deleteDiscovery(discoveryId, userId)
    }
}


