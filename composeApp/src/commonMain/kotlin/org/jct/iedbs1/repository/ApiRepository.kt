package org.jct.iedbs1.repository


import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.network.httpClient

class ApiRepository(
    private val apiKey: String,
    private val bearerToken: String
) {
    private val client = httpClient()
    private val baseUrl = "https://rxrdxsqykqlkymbfqypp.supabase.co/rest/v1"

    suspend fun getCargos(): List<Cargo> {
        val response: String = client.get("$baseUrl/Cargo") {
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
}