package org.jct.iedbs1.services

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import org.jct.iedbs1.models.Cargo

class CargoService(
    private val client: HttpClient,
    private val baseUrl: String
) {
    suspend fun getCargos(): List<Cargo> {
        return client.get("$baseUrl/cargos") {
            header("apikey", "TU_SUPABASE_API_KEY")
            header("Authorization", "Bearer TU_SUPABASE_API_KEY")
        }.body()
    }
}
