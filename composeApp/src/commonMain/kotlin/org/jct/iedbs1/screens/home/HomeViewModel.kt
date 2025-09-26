package org.jct.iedbs1.screens.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.EstadoEleccion
import kotlin.uuid.Uuid


// --- ViewModel ---
class HomeViewModel : ViewModel() {
    private val _cargos = MutableStateFlow<List<Cargo>>(emptyList())
    val cargos: StateFlow<List<Cargo>> = _cargos

    init {
        cargarCargos()
    }

    private fun cargarCargos() {
        viewModelScope.launch {
            delay(1500) // Simula llamada a servicio
            val ahora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            _cargos.value = listOf(
                Cargo(
                    id = "1",
                    cargo = "Pastor",
                    fecha = ahora,
                    votosEmitidos = 0,
                    titulo = "Pastor",
                    ganador = "Junior Mamani Apellido",
                    colorGanador = 0xFF9E9E9E,
                    estado = "PENDIENTE",
                ),
                Cargo(
                    id = "1",
                    cargo = "Pastor",
                    fecha = ahora.date.plus(1, DateTimeUnit.DAY).atTime(12, 0, 0),
                    votosEmitidos = 0,
                    titulo = "Pastor",
                    ganador = "Junior Mamani Apellido",
                    colorGanador = 0xFF9E9E9E,
                    estado = "ENPROGRESO",
                ),
                Cargo(
                    id = "1",
                    cargo = "Pastor",
                    fecha = ahora,
                    votosEmitidos = 0,
                    titulo = "Pastor",
                    ganador = "Junior Mamani Apellido",
                    colorGanador = 0xFF9E9E9E,
                    estado = "FINALIZADO",
                )
            )
        }
    }


}