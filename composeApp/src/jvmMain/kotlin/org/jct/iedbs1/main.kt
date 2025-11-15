package org.jct.iedbs1

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import votacion_iedbs1.composeapp.generated.resources.Res
import votacion_iedbs1.composeapp.generated.resources.app_icon

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Elecciones IEDB Santiago I",
        icon = painterResource(Res.drawable.app_icon)
    ) {
        App()
    }
}
