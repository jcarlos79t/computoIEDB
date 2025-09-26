package network
import io.ktor.client.*
import io.ktor.client.engine.java.*

fun httpClient(): HttpClient = createHttpClient(Java)