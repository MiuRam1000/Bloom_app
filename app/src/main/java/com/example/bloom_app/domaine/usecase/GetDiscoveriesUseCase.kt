package com.example.bloom_app.domaine.usecase

import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.repository.DiscoveryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

class GetDiscoveriesUseCase(
    private val repository: DiscoveryRepository
) {

    fun invoke(): Flow<List<Discovery>> {  // ✅ fun (pas operator, pas suspend)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "temp_user_id"
        return repository.getDiscoveriesForUser(userId)
    }
}
