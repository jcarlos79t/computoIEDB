package org.jct.iedbs1.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Postulante(
    val id: String,
    val nombre: String,
    val apellidos: String,
    val grupo: String,
    val genero: String,
    val color: Long, // Storing color as a Long (ARGB)
    var cargoId: String? = null
)

// Function to create a placeholder for the UI
fun getPostulantesDeEjemplo(): List<Postulante> {
    return listOf(
        Postulante("1", "JUAN", "PEREZ MAMANI", "Grupo de embajadores", "Masculino", 0xFFFFA726),
        Postulante("2", "CAMILA", "ESTRADA RIOS", "Grupo de jóvenes", "Femenino", 0xFF66BB6A),
        Postulante("3", "KEVIN AMIR", "RIOS SANCHEZ", "Grupo de jóvenes", "Masculino", 0xFFEF5350),
        Postulante("4", "MERCEDES", "LLUSCO PEREZ", "Grupo de jóvenes", "Femenino", 0xFFAB47BC),
        Postulante("5", "CARLOS", "MAMANI HUZ", "Grupo de adultos", "Masculino", 0xFFFFCA28)
    )
}
