package org.jct.iedbs1.models

import kotlinx.serialization.Serializable

@Serializable
data class Postulante(
    var id: String="",
    var nombre: String="",
    var apellido: String="",
    var color: String="",
    var clase: String="",
    var sexo: String="",
    )
