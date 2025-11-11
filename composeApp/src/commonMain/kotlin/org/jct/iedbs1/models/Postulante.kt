package org.jct.iedbs1.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class Postulante(
    var id: String,
    var cargoId: String? = null, // Will be set when the cargo is saved
    var nombre: String,
    var apellidos: String,
    var grupo: String,
    var genero: String,
    var color: String, // Storing color as a Hex String
    var votos: Int = 0,
)


// Helper to convert hex string to Color
fun String.toColor(): Color {
    if (this.isBlank()) return Color.Transparent

    // KMP-safe parser for a 64-bit unsigned hex string.
    // This avoids platform-specific bugs like the one in the JVM's toULong implementation.
    var ulongValue: ULong = 0uL
    this.forEach { char ->
        val digit = char.digitToInt(16).toULong()
        ulongValue = (ulongValue shl 4) or digit
    }
    return Color(ulongValue)
}

// Helper to convert Color to hex string
fun Color.toHexString(): String {
    // Pad to 16 characters to ensure the full 64-bit ARGB value is represented.
    return this.value.toString(16).uppercase().padStart(16, '0')
}

// Function to create a placeholder for the UI
fun getPostulantesDeEjemplo(): List<Postulante> {
    return listOf(
        Postulante("1", null, "JUAN", "PEREZ MAMANI", "Grupo de embajadores", "Masculino", "FFFFa726", 0),
        Postulante("2", null, "CAMILA", "ESTRADA RIOS", "Grupo de jóvenes", "Femenino", "FF66BB6A", 0),
        Postulante("3", null, "KEVIN AMIR", "RIOS SANCHEZ", "Grupo de jóvenes", "Masculino", "FFEF5350", 0),
        Postulante("4", null, "MERCEDES", "LLUSCO PEREZ", "Grupo de jóvenes", "Femenino", "FFAB47BC", 0),
        Postulante("5", null, "CARLOS", "MAMANI HUZ", "Grupo de adultos", "Masculino", "FFFFCA28", 0)
    )
}
