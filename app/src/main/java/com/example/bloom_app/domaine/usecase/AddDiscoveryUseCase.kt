package com.example.bloom_app.domaine.usecase

import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.repository.DiscoveryRepository

class AddDiscoveryUseCase(
    private val repository: DiscoveryRepository
) {

    suspend fun invoke(discovery: Discovery) {
        repository.addDiscovery(discovery)
    }
}
