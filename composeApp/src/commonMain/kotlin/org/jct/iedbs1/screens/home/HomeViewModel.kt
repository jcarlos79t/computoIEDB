package org.jct.iedbs1.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.repository.ApiRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class SaveState {
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
    object Idle : SaveState()
}

// --- ViewModel ---
class HomeViewModel(
    apiKey: String,
    bearerToken: String
) : ViewModel() {
    private val repository = ApiRepository(apiKey, bearerToken)

    private val _cargos = MutableStateFlow<List<Cargo>>(emptyList())
    val cargos: StateFlow<List<Cargo>> = _cargos.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    init {
        cargarCargos()
    }

    fun cargarCargos() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getCargos()
                }
                _cargos.value = result
            } catch (e: Exception) {
                println("❌ Error cargando cargos: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun guardarCargo(cargo: Cargo) {
        viewModelScope.launch {
            _saveState.value = SaveState.Saving
            try {
                cargo.id = Uuid.random().toString()
                withContext(Dispatchers.IO) {
                    repository.insertCargo(cargo)
                }
                cargarCargos() // Recargar la lista de cargos
                _saveState.value = SaveState.Success
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Error desconocido")
                println("❌ Error insertando cargo: ${e.message}")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }
}