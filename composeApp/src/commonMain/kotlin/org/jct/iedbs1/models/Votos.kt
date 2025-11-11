package org.jct.iedbs1.models

import kotlinx.serialization.Serializable

@Serializable
data class Votos(
    val id: String="",
    val cargoId: String="",
    val postulanteId: String="",
    val votos: Int=0,
)
