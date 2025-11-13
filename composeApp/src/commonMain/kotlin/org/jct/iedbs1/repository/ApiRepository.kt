package org.jct.iedbs1.repository


import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.Usuario
import org.jct.iedbs1.models.Votos
import org.jct.iedbs1.network.httpClient

class ApiRepository(
    private val apiKey: String,
    private val bearerToken: String
) {
    private val client = httpClient()
    private val baseUrl = "https://rxrdxsqykqlkymbfqypp.supabase.co/rest/v1"

    suspend fun getCargos(): List<Cargo> {
        val response: String = client.get("$baseUrl/Cargo?order=fecha.desc") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
            }
        }.body()

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<Cargo>>(response)
    }

    suspend fun insertCargo(cargo: Cargo): Cargo {
        val response: String = client.post("$baseUrl/Cargo") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
                append("Prefer", "return=representation") // para que devuelva el registro creado
            }
            contentType(ContentType.Application.Json)
            setBody(cargo)
        }.body()

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<Cargo>>(response).first()
    }

    suspend fun getPostulantes(cargoId: String): List<Postulante> {
        val response: String = client.get("$baseUrl/Postulante?cargoId=eq.$cargoId") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
            }
        }.body()

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<Postulante>>(response)
    }

    suspend fun getVotosForCargo(cargoId: String): List<Votos> {
        val response: String = client.get("$baseUrl/Votos?cargoId=eq.$cargoId") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
            }
        }.body()
        return Json { ignoreUnknownKeys = true }.decodeFromString(response)
    }

    suspend fun upsertVotos(votosToSave: List<Votos>) {
        client.post("$baseUrl/Votos") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
                // Tell supabase to merge duplicates instead of failing
                append("Prefer", "resolution=merge-duplicates")
            }
            contentType(ContentType.Application.Json)
            setBody(votosToSave)
        }
    }

    suspend fun updateCargo(cargo: Cargo) {
        client.patch("$baseUrl/Cargo?id=eq.${cargo.id}") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
                append("Prefer", "return=minimal")
            }
            contentType(ContentType.Application.Json)
            setBody(cargo)
        }
    }

    suspend fun insertPostulante(postulante: Postulante) {
        postulante.nombre.uppercase()
        postulante.apellidos.uppercase()
        val response: String = client.post("$baseUrl/Postulante") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
                append("Prefer", "return=representation")
            }
            contentType(ContentType.Application.Json)
            setBody(postulante)
        }.body()
    }

    suspend fun getUserByUsername(username: String): Usuario? {
        val response: String = client.get("$baseUrl/usuario?user=eq.$username") {
            headers {
                append("apikey", apiKey)
                append(HttpHeaders.Authorization, "Bearer $bearerToken")
            }
        }.body()

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<Usuario>>(response).firstOrNull()
    }
}