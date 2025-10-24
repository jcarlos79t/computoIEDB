package org.jct.iedbs1.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Cargo(
    var id: String="",
    var cargo: String = "",
    var fecha: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    var votosEmitidos: Int = 0,
    var ganador: String? = null,
    var colorGanador: String = "", // Cambiado a String
    var estado: String = "PENDIENTE",
    var fechaCadena: String = "",
)

enum class EstadoEleccion {
    PENDIENTE,
    ENPROGRESO,
    FINALIZADO
}
