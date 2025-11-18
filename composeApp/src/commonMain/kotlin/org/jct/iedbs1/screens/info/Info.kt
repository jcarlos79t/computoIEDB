package org.jct.iedbs1.screens.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jct.iedbs1.ui.theme.AppDimens
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Black
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Medium
import votacion_iedbs1.composeapp.generated.resources.Montserrat_SemiBold
import votacion_iedbs1.composeapp.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            StyledHeader(title = "INFORMACIÓN DE LA APP", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(Modifier.height(8.dp))
            ElectionTitle()
            Spacer(Modifier.height(16.dp))

            InfoCard {
                InfoRow(icon = Icons.Default.VerifiedUser, title = "Versión de la App", subtitle = "1.0.0 RC")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(icon = Icons.Default.People, title = "Autor", subtitle = "J.C.T.A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(icon = Icons.Default.Info, title = "Desarrollada por", subtitle = "Dep. Sistemas ©2025 Area AudioVisual")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledHeader(title: String, onNavigateBack: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ElectionTitle() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SISTEMA DE COMPUTO - ELECCIONES 2026-2027",
                fontWeight = FontWeight.Bold,
                fontSize = AppDimens.title,
                textAlign = TextAlign.Center,
                lineHeight = AppDimens.headline,
                fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
            )
            Text(
                text = "Iglesia Evangelica de Dios Boliviana - Santiago I",
                fontSize = AppDimens.subtitle,
                textAlign = TextAlign.Center,
                lineHeight = AppDimens.caption,
                fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
            )
        }
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = AppDimens.label)
            Text(subtitle, fontSize = AppDimens.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
