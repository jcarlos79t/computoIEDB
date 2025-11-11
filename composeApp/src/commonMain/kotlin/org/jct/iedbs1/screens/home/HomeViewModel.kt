package org.jct.iedbs1.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.Votos
import org.jct.iedbs1.repository.ApiRepository


// --- States ---
sealed class SaveState {
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
    object Idle : SaveState()
}

data class VotosUiState(
    val postulantes: List<Postulante> = emptyList(),
    val votos: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val totalVotos: Int = 0
)

// --- ViewModel ---
class HomeViewModel(
    apiKey: String,
    bearerToken: String
) : ViewModel() {
    private val repository = ApiRepository(apiKey, bearerToken)

    // Home Screen State
    private val _cargos = MutableStateFlow<List<Cargo>>(emptyList())
    val cargos: StateFlow<List<Cargo>> = _cargos.asStateFlow()

    // RegistrarVotos & Detail Screen State
    private val _votosUiState = MutableStateFlow(VotosUiState())
    val votosUiState = _votosUiState.asStateFlow()

    init {
        cargarCargos()
    }

    // --- Home Screen Functions ---
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

    // --- RegistrarVotos & Detail Screen Functions ---
    fun cargarDatosVotacion(cargoId: String) {
        viewModelScope.launch {
            _votosUiState.update { it.copy(isLoading = true) }
            try {
                // Cargar postulantes y votos existentes en paralelo
                val postulantesDeferred = async(Dispatchers.IO) { repository.getPostulantes(cargoId) }
                val votosPreviosDeferred = async(Dispatchers.IO) { repository.getVotosForCargo(cargoId) }

                val postulantes = postulantesDeferred.await()
                val votosPrevios = votosPreviosDeferred.await()

                // Crear el mapa inicial de votos a partir de los datos de la tabla Votos
                val initialVotosMap = postulantes.associate { p -> p.id to (votosPrevios.find { it.postulanteId == p.id }?.votos ?: 0) }
                val initialTotalVotos = initialVotosMap.values.sum()

                // Ordenar la lista de postulantes por votos (de mayor a menor)
                val postulantesOrdenados = postulantes.sortedByDescending { initialVotosMap[it.id] ?: 0 }

                _votosUiState.update { state ->
                    state.copy(
                        postulantes = postulantesOrdenados,
                        votos = initialVotosMap,
                        totalVotos = initialTotalVotos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                println("❌ Error cargando datos de votación: ${e.message}")
                _votosUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onVotoChange(postulanteId: String, votos: String) {
        val newVotos = votos.toIntOrNull() ?: 0
        _votosUiState.update { state ->
            val updatedVotos = state.votos.toMutableMap()
            updatedVotos[postulanteId] = newVotos
            val total = updatedVotos.values.sum()
            state.copy(votos = updatedVotos, totalVotos = total)
        }
    }

    fun guardarVotos(cargo: Cargo) {
        viewModelScope.launch {
            _votosUiState.update { it.copy(isSaving = true) }
            try {
                val state = _votosUiState.value

                // 1. Preparar la lista de objetos Votos para enviar a Supabase
                val votosToUpsert = state.postulantes.map { postulante ->
                    Votos(
                        // Usar una clave compuesta y predecible para el upsert
                        id = "${cargo.id}_${postulante.id}", 
                        cargoId = cargo.id,
                        postulanteId = postulante.id,
                        votos = state.votos[postulante.id] ?: 0
                    )
                }

                if (votosToUpsert.isNotEmpty()) {
                    withContext(Dispatchers.IO) {
                        repository.upsertVotos(votosToUpsert)
                    }
                }

                // 2. Encontrar al ganador (esta lógica no cambia)
                val ganadorId = state.votos.maxByOrNull { it.value }?.key
                val postulanteGanador = state.postulantes.find { it.id == ganadorId }

                // 3. Actualizar el objeto Cargo principal
                val updatedCargo = cargo.copy(
                    estado = "FINALIZADO",
                    votosEmitidos = state.totalVotos,
                    ganador = postulanteGanador?.let { "${it.nombre} ${it.apellidos}" },
                    colorGanador = postulanteGanador?.color ?: ""
                )

                withContext(Dispatchers.IO) {
                    repository.updateCargo(updatedCargo)
                }

                // 4. Actualizar el estado de la UI y recargar la home
                _votosUiState.update { it.copy(isSaving = false, saveSuccess = true) }
                cargarCargos()

            } catch (e: Exception) {
                println("❌ Error guardando votos: ${e.message}")
                _votosUiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun resetVotosState() {
        _votosUiState.value = VotosUiState()
    }
}