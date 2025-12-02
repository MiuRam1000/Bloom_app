package com.example.bloom_app.ui.screen.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.usecase.DeleteDiscoveryUseCase
import com.example.bloom_app.domaine.usecase.GetDiscoveriesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(
    private val getDiscoveriesUseCase: GetDiscoveriesUseCase,
    private val deleteDiscoveryUseCase: DeleteDiscoveryUseCase
) : ViewModel() {

    val discoveries = getDiscoveriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()  // ✅ SIMPLE !
        )

    fun deleteDiscovery(id: Long) {
        viewModelScope.launch {
            deleteDiscoveryUseCase(id)
        }
    }

    fun getDiscoveryById(id: Long): Discovery? {
        return discoveries.value.find { it.id == id }
    }
}
