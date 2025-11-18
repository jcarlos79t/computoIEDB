package org.jct.iedbs1.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toColor
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.ui.theme.AppDimens
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Black
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Bold
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Medium
import votacion_iedbs1.composeapp.generated.resources.Montserrat_SemiBold
import votacion_iedbs1.composeapp.generated.resources.Res

@Composable
fun DetailRoute(
    cargo: Cargo,
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.votosUiState.collectAsState()

    LaunchedEffect(key1 = cargo.id) {
        viewModel.cargarDatosVotacion(cargo.id)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetVotosState()
        }
    }

    DetailScreen(
        cargoNombre = cargo.cargo,
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSync = { viewModel.cargarDatosVotacion(cargo.id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cargoNombre: String,
    uiState: org.jct.iedbs1.screens.home.VotosUiState,
    onNavigateBack: () -> Unit,
    onSync: () -> Unit
) {
    Scaffold(
        topBar = { DetailHeader(title = cargoNombre.uppercase(), onNavigateBack = onNavigateBack, onSync = onSync) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TotalVotosHeader(totalVotos = uiState.totalVotos)
                Spacer(Modifier.height(24.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.postulantes, key = { it.id }) { postulante ->
                        val votos = uiState.votos[postulante.id] ?: 0
                        ResultBar(
                            postulante = postulante,
                            votos = votos,
                            totalVotos = uiState.totalVotos
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHeader(title: String, onNavigateBack: () -> Unit, onSync: () -> Unit) {
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
                    fontFamily = FontFamily( Font( Res.font.Montserrat_Black))
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
            actions = {
                IconButton(onClick = onSync) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Sincronizar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // el color lo da el Surface
            )
        )
    }
}


@Composable
fun TotalVotosHeader(totalVotos: Int) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val animatedTotalVotos by animateIntAsState(
        targetValue = if (visible) totalVotos else 0,
        animationSpec = tween(durationMillis = 1500, delayMillis = 200)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ELECCIONES 2026-2027",
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title,
                    textAlign = TextAlign.Center,
                    lineHeight = 5.sp,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                )
                Text(
                    text = "Iglesia Evangelica de Dios Boliviana - Santiago I",
                    fontSize = AppDimens.subtitle,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
                )
                Text(
                    text = "Dep. Sistemas ©2025 Area AudioVisual",
                    fontSize = AppDimens.tiny,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Medium))
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 200))
        ) {
            Row {
                Text(
                    "Total votos:",
                    fontSize = AppDimens.headline,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    animatedTotalVotos.toString(),
                    fontSize = AppDimens.headline,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "|",
                    fontSize = AppDimens.headline,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "100%",
                    fontSize = AppDimens.headline,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Bold))
                )
            }
        }
    }
}

@Composable
fun ResultBar(postulante: Postulante, votos: Int, totalVotos: Int) {
    val porcentaje = if (totalVotos > 0) votos.toFloat() / totalVotos.toFloat() else 0f

    var animationPlayed by remember { mutableStateOf(false) }

    val animatedFraction by animateFloatAsState(
        targetValue = if (animationPlayed) porcentaje else 0f,
        animationSpec = tween(durationMillis = 2000, delayMillis = 600)
    )

    val animatedVotos by animateIntAsState(
        targetValue = if (animationPlayed) votos else 0,
        animationSpec = tween(durationMillis = 2000, delayMillis = 600)
    )

    val animatedPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) (porcentaje * 100) else 0f,
        animationSpec = tween(durationMillis = 2000, delayMillis = 600)
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = animationPlayed,
            enter = fadeIn(animationSpec = tween(durationMillis = 800, delayMillis = 400))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedFraction)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(postulante.color.toColor())
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = animatedVotos.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = AppDimens.display,
                        fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                    )
                    Text(
                        text = "${animatedPercentage.toInt()}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = AppDimens.display,
                        fontFamily = FontFamily(Font(Res.font.Montserrat_Bold))
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        AnimatedVisibility(
            visible = animationPlayed,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 1000))
        ) {
            Column {
                Text(
                    text = "${postulante.nombre} ${postulante.apellidos}",
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.card_title,
                    modifier = Modifier.padding(start = 4.dp),
                    fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
                )
                Text(
                    text = postulante.grupo,
                    fontSize = AppDimens.card_subtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp),
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Medium))
                )
            }
        }
    }
}
