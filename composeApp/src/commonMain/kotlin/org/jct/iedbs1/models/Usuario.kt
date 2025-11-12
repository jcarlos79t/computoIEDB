package org.jct.iedbs1.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val user: String,
    val password: String,
    val valido: Boolean = false
)
