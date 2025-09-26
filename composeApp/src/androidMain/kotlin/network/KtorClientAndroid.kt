package network
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

fun httpClient(): HttpClient = createHttpClient(OkHttp)
