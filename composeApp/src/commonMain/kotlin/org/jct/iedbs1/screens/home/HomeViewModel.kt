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
import org.jct.iedbs1.models.Usuario
import org.jct.iedbs1.models.Votos
import org.jct.iedbs1.repository.ApiRepository


// --- States ---
data class VotosUiState(
    val postulantes: List<Postulante> = emptyList(),
    val votos: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val totalVotos: Int = 0
)

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String? = null
)

data class ReportChartData(
    val ganadoresPorGrupo: Map<String, Int> = emptyMap(),
    val ganadoresPorGenero: Map<String, Int> = emptyMap(),
    val participacionPorCargo: List<Pair<String, Int>> = emptyList(),
    val postulantesPorCargo: List<Pair<String, Int>> = emptyList()
)

data class ReportUiState(
    val isLoading: Boolean = true,
    val chartData: ReportChartData = ReportChartData()
)

// --- ViewModel ---
class HomeViewModel(
    apiKey: String,
    bearerToken: String
) : ViewModel() {
    private val repository = ApiRepository(apiKey, bearerToken)

    // --- State Holders ---
    private val _cargos = MutableStateFlow<List<Cargo>>(emptyList())
    val cargos: StateFlow<List<Cargo>> = _cargos.asStateFlow()

    private val _votosUiState = MutableStateFlow(VotosUiState())
    val votosUiState = _votosUiState.asStateFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _loggedInUser = MutableStateFlow<Usuario?>(null)
    val loggedInUser: StateFlow<Usuario?> = _loggedInUser.asStateFlow()

    private val _reportUiState = MutableStateFlow(ReportUiState())
    val reportUiState: StateFlow<ReportUiState> = _reportUiState.asStateFlow()

    init {
        cargarCargos()
    }

    // --- Home Screen ---
    fun cargarCargos() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { repository.getCargos() }
                _cargos.value = result
            } catch (e: Exception) {
                println("❌ Error cargando cargos: ${e.message}")
            }
        }
    }

    // --- Votos & Detail Screens ---
    fun cargarDatosVotacion(cargoId: String) {
        viewModelScope.launch {
            _votosUiState.update { it.copy(isLoading = true) }
            try {
                val postulantesDeferred = async(Dispatchers.IO) { repository.getPostulantes(cargoId) }
                val votosPreviosDeferred = async(Dispatchers.IO) { repository.getVotosForCargo(cargoId) }
                val postulantes = postulantesDeferred.await()
                val votosPrevios = votosPreviosDeferred.await()
                val initialVotosMap = postulantes.associate { p -> p.id to (votosPrevios.find { it.postulanteId == p.id }?.votos ?: 0) }
                val initialTotalVotos = initialVotosMap.values.sum()
                val postulantesOrdenados = postulantes //.sortedByDescending { initialVotosMap[it.id] ?: 0 }

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
                val votosToUpsert = state.postulantes.map {
                    Votos(id = "${cargo.id}_${it.id}", cargoId = cargo.id, postulanteId = it.id, votos = state.votos[it.id] ?: 0)
                }
                if (votosToUpsert.isNotEmpty()) {
                    withContext(Dispatchers.IO) { repository.upsertVotos(votosToUpsert) }
                }
                val ganadorId = state.votos.maxByOrNull { it.value }?.key
                val postulanteGanador = state.postulantes.find { it.id == ganadorId }
                val updatedCargo = cargo.copy(
                    estado = "FINALIZADO",
                    votosEmitidos = state.totalVotos,
                    ganador = postulanteGanador?.let { "${it.nombre} ${it.apellidos}" },
                    colorGanador = postulanteGanador?.color ?: ""
                )
                withContext(Dispatchers.IO) { repository.updateCargo(updatedCargo) }
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

    // --- Login Screen ---
    fun onUsernameChange(username: String) {
        _loginUiState.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _loginUiState.update { it.copy(password = password) }
    }

    fun login() {
        viewModelScope.launch {
            _loginUiState.update { it.copy(isLoading = true, loginError = null) }
            try {
                val state = _loginUiState.value
                val userFromDb = withContext(Dispatchers.IO) { repository.getUserByUsername(state.username) }

                if (userFromDb != null && userFromDb.password == state.password) {
                    _loggedInUser.value = userFromDb.copy(valido = true)
                    _loginUiState.update { it.copy(isLoading = false, loginSuccess = true) }
                } else {
                    _loginUiState.update { it.copy(isLoading = false, loginError = "Usuario o contraseña incorrectos") }
                }
            } catch (e: Exception) {
                println("❌ Error en el login: ${e.message}")
                _loginUiState.update { it.copy(isLoading = false, loginError = "Error de conexión") }
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
    }

    fun resetLoginState() {
        _loginUiState.value = LoginUiState()
    }

    // --- Reports Screen ---
    private fun limitMapData(data: Map<String, Int>, limit: Int = 19): Map<String, Int> {
        if (data.size <= limit + 1) return data

        val sortedData = data.toList().sortedByDescending { it.second }
        val mainData = sortedData.take(limit)
        val otherData = sortedData.drop(limit)
        val otherSum = otherData.sumOf { it.second }

        return mainData.toMap() + ("Otros" to otherSum)
    }

    private fun limitPairData(data: List<Pair<String, Int>>, limit: Int = 19): List<Pair<String, Int>> {
        if (data.size <= limit + 1) return data

        val mainData = data.take(limit)
        val otherData = data.drop(limit)
        val otherSum = otherData.sumOf { it.second }

        return mainData + ("Otros" to otherSum)
    }

    fun loadReportData() {
        viewModelScope.launch {
            _reportUiState.update { it.copy(isLoading = true) }
            try {
                val allCargos = _cargos.value
                val allPostulantesDeferred = async(Dispatchers.IO) { allCargos.flatMap { repository.getPostulantes(it.id) } }
                val allVotosDeferred = async(Dispatchers.IO) { allCargos.flatMap { repository.getVotosForCargo(it.id) } }

                val allPostulantes = allPostulantesDeferred.await()
                val allVotos = allVotosDeferred.await()
                val votosMap = allVotos.associate { it.postulanteId to it.votos }

                // --- Find Winners ---
                val finishedCargos = allCargos.filter { it.estado == "FINALIZADO" }
                val postulantesByCargoId = allPostulantes.groupBy { it.cargoId }
                val winners = finishedCargos.mapNotNull { cargo ->
                    postulantesByCargoId[cargo.id]?.maxByOrNull { postulante ->
                        votosMap[postulante.id] ?: 0
                    }
                }

                // 1. Ganadores por Grupo
                val ganadoresPorGrupo = winners.groupBy { it.grupo }.mapValues { it.value.size }

                // 2. Ganadores por Género
                val ganadoresPorGenero = winners.groupBy { it.genero }.mapValues { it.value.size }

                // 3. Participación por Cargo (votos emitidos por cargo)
                val participacionPorCargo = allCargos
                    .map { it.cargo to (it.votosEmitidos ?: 0) }
                    .sortedByDescending { it.second }

                // 4. Postulantes por Cargo (número de candidatos por cargo)
                val postulantesPorCargo = allPostulantes.groupBy { it.cargoId }
                    .mapKeys { (cargoId, _) -> allCargos.find { it.id == cargoId }?.cargo ?: "Desconocido" }
                    .map { it.key to it.value.size }
                    .sortedByDescending { it.second }

                _reportUiState.update {
                    it.copy(
                        isLoading = false,
                        chartData = ReportChartData(
                            ganadoresPorGrupo = limitMapData(ganadoresPorGrupo),
                            ganadoresPorGenero = limitMapData(ganadoresPorGenero),
                            participacionPorCargo = limitPairData(participacionPorCargo),
                            postulantesPorCargo = limitPairData(postulantesPorCargo)
                        )
                    )
                }

            } catch (e: Exception) {
                println("❌ Error loading report data: ${e.message}")
                _reportUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}