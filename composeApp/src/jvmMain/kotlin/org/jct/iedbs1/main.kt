package org.jct.iedbs1

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "votacion_iedbs1",
    ) {
        App()
    }
}