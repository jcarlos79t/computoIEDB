package org.jct.iedbs1.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toColor
import org.jct.iedbs1.screens.home.HomeViewModel

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
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cargoNombre: String,
    uiState: org.jct.iedbs1.screens.home.VotosUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = { DetailHeader(title = cargoNombre.uppercase(), onNavigateBack = onNavigateBack) }
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
fun DetailHeader(title: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Composable
fun TotalVotosHeader(totalVotos: Int) {
    Row {
        Text("Total votos:", fontSize = 20.sp)
        Spacer(Modifier.width(8.dp))
        Text(totalVotos.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text("|", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Spacer(Modifier.width(16.dp))
        Text("100%", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResultBar(postulante: Postulante, votos: Int, totalVotos: Int) {
    val porcentaje = if (totalVotos > 0) votos.toFloat() / totalVotos.toFloat() else 0f

    val animatedFraction by animateFloatAsState(
        targetValue = porcentaje,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(postulante.color.toColor())
            )
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = votos.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${(porcentaje * 100).toInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${postulante.nombre} ${postulante.apellidos}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = postulante.grupo,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
