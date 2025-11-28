package org.jct.iedbs1.screens.votos

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toColor
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.ui.theme.AppDimens


@Composable
fun RegistrarVotosRoute(
    cargo: Cargo,
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.votosUiState.collectAsState()

    LaunchedEffect(key1 = cargo.id) {
        // Cambiado a la nueva función de carga
        viewModel.cargarDatosVotacion(cargo.id)
    }

    LaunchedEffect(key1 = uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onNavigateBack()
        }
    }

    // Reset state when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetVotosState()
        }
    }

    RegistrarVotosScreen(
        cargo = cargo,
        uiState = uiState,
        onVotoChange = viewModel::onVotoChange,
        onSave = { viewModel.guardarVotos(cargo) },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarVotosScreen(
    cargo: Cargo,
    uiState: org.jct.iedbs1.screens.home.VotosUiState,
    onVotoChange: (String, String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = { VotosHeader(onSaveClick = onSave, onNavigateBack = onNavigateBack) }
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
                    .padding(16.dp)
            ) {
                TotalVotosHeader(totalVotos = uiState.totalVotos)
                Spacer(Modifier.height(16.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.postulantes, key = { it.id }) { postulante ->
                        VotoPostulanteCard(
                            postulante = postulante,
                            votos = uiState.votos[postulante.id] ?: 0,
                            totalVotos = uiState.totalVotos,
                            onVotoChange = { onVotoChange(postulante.id, it) }
                        )
                    }
                }
            }
        }

        if (uiState.isSaving) {
            Dialog(onDismissRequest = {}) {
                Card(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Guardando resultados...")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotosHeader(
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
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
        TopAppBar(
            title = {
                Text(
                    text = "REGISTRAR VOTOS",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title
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
                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // El color viene del Surface
            )
        )
    }
}


@Composable
fun TotalVotosHeader(totalVotos: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Total votos:", fontSize = AppDimens.headline)
        Spacer(Modifier.width(8.dp))
        Text(totalVotos.toString(), fontSize = AppDimens.headline, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text("|", fontSize = AppDimens.headline, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Spacer(Modifier.width(16.dp))
        Text("100%", fontSize = AppDimens.headline, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun VotoPostulanteCard(
    postulante: Postulante,
    votos: Int,
    totalVotos: Int,
    onVotoChange: (String) -> Unit
) {
    val porcentaje = if (totalVotos > 0) (votos * 100f / totalVotos) else 0f

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(postulante.color.toColor()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${porcentaje.toInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title
                )
            }
            Spacer(Modifier.width(12.dp))
            OutlinedTextField(
                value = if (votos == 0) "" else votos.toString(),
                onValueChange = onVotoChange,
                label = { Text("Registrar votos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "${postulante.nombre} ${postulante.apellidos}".uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = AppDimens.card_title
        )
        Text(
            text = postulante.grupo,
            fontSize = AppDimens.subtitle,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

