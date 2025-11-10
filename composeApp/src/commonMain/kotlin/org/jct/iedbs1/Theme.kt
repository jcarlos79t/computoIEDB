package org.jct.iedbs1

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val lightColorScheme = lightColorScheme(
    primary = Color(0xFFB71C1C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    onPrimaryContainer = Color(0xFF410002),
    secondary = Color(0xFF757575),
    onSecondary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F)
)

val darkColorScheme = darkColorScheme(
    primary = Color(0xFFFFEB3B),
    onPrimary = Color(0xFF3A3000),
    primaryContainer = Color(0xFF534600),
    onPrimaryContainer = Color(0xFFFFEE9A),
    secondary = Color(0xFFC8C6C0),
    onSecondary = Color(0xFF33302A),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5)
)

val typography = Typography(
    bodySmall = TextStyle( fontSize = 10.sp),
    bodyMedium = TextStyle( fontSize = 14.sp),
    bodyLarge = TextStyle( fontSize = 18.sp),
)

val shapes = Shapes(
    extraSmall = RoundedCornerShape(16.dp, 8.dp, 16.dp, 8.dp),
)

@Composable
fun MyTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = if (isDark) darkColorScheme else lightColorScheme
    MaterialTheme(
        content = content,
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes
    )

}
