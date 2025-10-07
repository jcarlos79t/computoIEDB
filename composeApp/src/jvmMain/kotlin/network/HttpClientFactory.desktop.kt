package network

import io.ktor.client.*
import io.ktor.client.engine.java.*

actual fun httpClient(): HttpClient = HttpClient(Java)