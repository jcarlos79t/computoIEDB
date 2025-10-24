package org.jct.iedbs1

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cmR4c3F5a3Fsa3ltYmZxeXBwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc2ODgxNjEsImV4cCI6MjA3MzI2NDE2MX0.73UpJNTgYs6C039iBm1nibwPQTIvENeFqexVn4Xg25o"
    val bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cmR4c3F5a3Fsa3ltYmZxeXBwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc2ODgxNjEsImV4cCI6MjA3MzI2NDE2MX0.73UpJNTgYs6C039iBm1nibwPQTIvENeFqexVn4Xg25o"
    MyTheme {
        AppNavigation(apiKey, bearerToken)
    }
}