package com.example.bloom_app.ui.screen.journal


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.util.toDomain
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class JournalViewModel(
    private val discoveryDao: DiscoveryDao
) : ViewModel() {

    // Pour la Semaine 1 on utilise un userId temporaire
    // En Semaine 2 on injectera le vrai Firebase UID
    private val tempUserId = "temp_user_id"

    val discoveries = discoveryDao.getAllForUser(tempUserId)
        .map { it.toDomain() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<Discovery>()
        )
}