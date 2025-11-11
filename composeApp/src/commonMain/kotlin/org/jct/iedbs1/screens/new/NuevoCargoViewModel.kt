package org.jct.iedbs1.screens.new

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toHexString
import kotlin.random.Random

data class NuevoCargoUiState(
    val cargoNombre: String = "",
    val postulantes: List<Postulante> = emptyList(),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val showDialog: Boolean = false,
    // Dialog fields
    val dialogNombre: String = "",
    val dialogApellidos: String = "",
    val dialogColor: Color = Color.Yellow,
    val dialogGrupo: String = "",
    val dialogGenero: String = ""
)

class NuevoCargoViewModel(apiKey: String, bearerToken: String) : ViewModel() {

    private val _uiState = MutableStateFlow(NuevoCargoUiState())
    val uiState = _uiState.asStateFlow()

    // Options for dropdowns
    val grupos = listOf("Grupo de jóvenes", "Grupo de adultos", "Grupo de embajadores")
    val generos = listOf("Masculino", "Femenino")

    fun onCargoNombreChange(nombre: String) {
        _uiState.update { it.copy(cargoNombre = nombre) }
    }

    fun onDialogNombreChange(nombre: String) {
        _uiState.update { it.copy(dialogNombre = nombre) }
    }

    fun onDialogApellidosChange(apellidos: String) {
        _uiState.update { it.copy(dialogApellidos = apellidos) }
    }

    fun onDialogColorChange(color: Color) {
        _uiState.update { it.copy(dialogColor = color) }
    }

    fun onDialogGrupoChange(grupo: String) {
        _uiState.update { it.copy(dialogGrupo = grupo) }
    }

    fun onDialogGeneroChange(genero: String) {
        _uiState.update { it.copy(dialogGenero = genero) }
    }

    fun showAddPostulanteDialog(show: Boolean) {
        _uiState.update { it.copy(showDialog = show) }
    }

    fun addPostulante() {
        val state = _uiState.value
        if (state.dialogNombre.isBlank() || state.dialogApellidos.isBlank() || state.dialogGrupo.isBlank() || state.dialogGenero.isBlank()) {
            // Handle error - maybe show a toast or a message
            return
        }

        val newPostulante = Postulante(
            id = Random.nextInt().toString(), // Temp ID
            nombre = state.dialogNombre.uppercase(),
            apellidos = state.dialogApellidos.uppercase(),
            grupo = state.dialogGrupo,
            genero = state.dialogGenero,
            color = state.dialogColor.toHexString() // Guardar como String
        )

        _uiState.update { it.copy(
            postulantes = it.postulantes + newPostulante,
            // Reset dialog fields
            showDialog = false,
            dialogNombre = "",
            dialogApellidos = "",
            dialogColor = Color.Yellow,
            dialogGrupo = "",
            dialogGenero = ""
        ) }
    }

    fun removePostulante(postulante: Postulante) {
        _uiState.update {
            it.copy(postulantes = it.postulantes.filterNot { p -> p.id == postulante.id })
        }
    }

    fun guardarCargo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            // TODO: Here you would call your repository to save the cargo and the postulantes
            // val cargoId = repository.saveCargo(uiState.value.cargoNombre)
            // uiState.value.postulantes.forEach { postulante ->
            //     repository.savePostulante(postulante.copy(cargoId = cargoId))
            // }
            kotlinx.coroutines.delay(2000) // Simulate network call
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
    
    fun resetState() {
        _uiState.value = NuevoCargoUiState()
    }
}