package com.example.bloom_app.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.usecase.DeleteDiscoveryUseCase
import com.example.bloom_app.domaine.usecase.GetDiscoveriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDiscoveriesUseCase: GetDiscoveriesUseCase,
    private val deleteDiscoveryUseCase: DeleteDiscoveryUseCase
) : ViewModel() {

    private val _discovery = MutableStateFlow<Discovery?>(null)
    val discovery: StateFlow<Discovery?> = _discovery

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    /** Charge la bonne discovery PAR ID */
    fun loadDiscovery(id: Long) {
        viewModelScope.launch {
            getDiscoveriesUseCase.invoke().collectLatest { discoveries ->
                _discovery.value = discoveries.find { it.id == id }
            }
        }
    }

    /** Supprime et met à jour l'état */
    fun deleteDiscovery(discoveryId: Long) {
        viewModelScope.launch {
            deleteDiscoveryUseCase.invoke(discoveryId)
            _isDeleted.value = true
            _discovery.value = null  // ← Efface la discovery
        }
    }
}
