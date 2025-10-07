package org.jct.iedbs1.network

import io.ktor.client.*
import io.ktor.client.engine.android.*

actual fun httpClient(): HttpClient = HttpClient(Android)