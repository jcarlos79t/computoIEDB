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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toColor
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.screens.home.VotosUiState
import org.jct.iedbs1.ui.theme.AppDimens
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Black
import votacion_iedbs1.composeapp.generated.resources.Montserrat_SemiBold
import votacion_iedbs1.composeapp.generated.resources.Res

@Composable
fun RegistrarVotosRoute2(
    cargo: Cargo,
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit
) {
   /* val uiState by viewModel.votosUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDatosVotacion(cargo.id)
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.resetVotosState()
            onNavigateBack()
        }
    }

    RegistrarVotosScreen2(
        cargo = cargo,
        uiState = uiState,
        onVotoChange = viewModel::onVotoChange,
        onSave = { viewModel.guardarVotos(cargo) },
        onNavigateBack = onNavigateBack
    )*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarVotosScreen2(
    cargo: Cargo,
    uiState: VotosUiState,
    onVotoChange: (String, String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
/*    Scaffold(
        topBar = { VotosHeader(cargo.cargo, onSave, onNavigateBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            TotalVotos(uiState.totalVotos)
            Spacer(Modifier.height(16.dp))
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.postulantes) { postulante ->
                        PostulanteVotoCard(
                            postulante = postulante,
                            votos = uiState.votos[postulante.id] ?: 0,
                            onVotoChange = { onVotoChange(postulante.id, it) }
                        )
                    }
                }
            }
        }
    }*/
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotosHeader(cargoNombre: String, onSave: () -> Unit, onNavigateBack: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        color = colorScheme.primary,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp
    ) {
        TopAppBar(
            title = { Text(cargoNombre, color = colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = AppDimens.headline) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun PostulanteVotoCard(postulante: Postulante, votos: Int, onVotoChange: (String) -> Unit) {
    val color = postulante.color.toColor()
    val gradient = Brush.horizontalGradient(colors = listOf(color.copy(alpha = 0.0f), color.copy(alpha = 0.5f), color), startX = 450f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradient, alpha = 0.9f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${postulante.nombre} ${postulante.apellidos}",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppDimens.headline,
                        color = colorScheme.onSurface,
                        fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = postulante.grupo,
                        fontSize = AppDimens.label,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
                    )
                }
                OutlinedTextField(
                    value = if (votos == 0) "" else votos.toString(),
                    onValueChange = onVotoChange,
                    modifier = Modifier.weight(0.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun TotalVotos(total: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("TOTAL DE VOTOS EMITIDOS", fontSize = AppDimens.label, color = colorScheme.primary)
        Text(
            text = total.toString(),
            fontSize = AppDimens.display,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}*/
