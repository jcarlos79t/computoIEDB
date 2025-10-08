package org.jct.iedbs1

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jct.iedbs1.screens.home.HomeRoute
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cmR4c3F5a3Fsa3ltYmZxeXBwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc2ODgxNjEsImV4cCI6MjA3MzI2NDE2MX0.73UpJNTgYs6C039iBm1nibwPQTIvENeFqexVn4Xg25o"
        val bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cmR4c3F5a3Fsa3ltYmZxeXBwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc2ODgxNjEsImV4cCI6MjA3MzI2NDE2MX0.73UpJNTgYs6C039iBm1nibwPQTIvENeFqexVn4Xg25o"

        val homeViewModel = HomeViewModel(apiKey, bearerToken)
        HomeRoute(homeViewModel)
/*        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }*/
    }
}