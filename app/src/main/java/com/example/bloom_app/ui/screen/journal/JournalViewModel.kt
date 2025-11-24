// ui/screen/journal/JournalViewModel.kt
package com.example.bloom_app.ui.screen.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.util.toDomain
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(
    private val discoveryDao: DiscoveryDao
) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "temp_user_id"

    val discoveries = discoveryDao.getAllForUser(currentUserId)
        .map { it.toDomain() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getDiscoveryById(id: Long): Discovery? {
        return discoveries.value.find { it.id == id }
    }

    fun deleteDiscovery(id: Long) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            discoveryDao.deleteById(id, userId)  // ← maintenant 2 paramètres
        }
    }
}