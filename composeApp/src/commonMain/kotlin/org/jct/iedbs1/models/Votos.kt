package org.jct.iedbs1.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class Votos(
    val id: String,
    val cargoId: String,
    val postulanteId: String,
    @EncodeDefault val votos: Int = 0,
)
