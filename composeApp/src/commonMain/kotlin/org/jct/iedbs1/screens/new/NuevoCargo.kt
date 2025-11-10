package org.jct.iedbs1.screens.new

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jct.iedbs1.models.Postulante

@Composable
fun NuevoCargoRoute(
    viewModel: NuevoCargoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.resetSaveSuccess()
            onNavigateBack()
        }
    }

    NuevoCargoScreen(
        uiState = uiState,
        onCargoNombreChange = viewModel::onCargoNombreChange,
        onShowDialog = { viewModel.showAddPostulanteDialog(true) },
        onRemovePostulante = viewModel::removePostulante,
        onSave = viewModel::guardarCargo,
        onNavigateBack = onNavigateBack
    )

    if (uiState.showDialog) {
        NuevoPostulanteDialog(
            uiState = uiState,
            onDismiss = { viewModel.showAddPostulanteDialog(false) },
            onNombreChange = viewModel::onDialogNombreChange,
            onApellidosChange = viewModel::onDialogApellidosChange,
            onColorChange = viewModel::onDialogColorChange,
            onGrupoChange = viewModel::onDialogGrupoChange,
            onGeneroChange = viewModel::onDialogGeneroChange,
            onAddPostulante = viewModel::addPostulante,
            grupos = viewModel.grupos,
            generos = viewModel.generos
        )
    }

    if (uiState.isSaving) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Guardando...")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoCargoScreen(
    uiState: NuevoCargoUiState,
    onCargoNombreChange: (String) -> Unit,
    onShowDialog: () -> Unit,
    onRemovePostulante: (Postulante) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NuevoCargoHeader(
                onSaveClick = onSave,
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onShowDialog,
                containerColor = Color(0xFF25D366),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Postulante")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.cargoNombre,
                onValueChange = onCargoNombreChange,
                label = { Text("Escriba el cargo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Postulantes", style = MaterialTheme.typography.titleLarge)
                HorizontalDivider(modifier = Modifier.weight(1f).padding(start = 8.dp))
            }


            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.postulantes, key = { it.id }) { postulante ->
                    PostulanteCard(
                        postulante = postulante,
                        onDelete = { onRemovePostulante(postulante) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoCargoHeader(onSaveClick: () -> Unit, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text("NUEVO CARGO") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(Icons.Default.Save, contentDescription = "Guardar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun PostulanteCard(postulante: Postulante, onDelete: () -> Unit) {
    val color = Color(postulante.color)
    val gradient = Brush.horizontalGradient(
        colors = listOf(color.copy(alpha = 0.0f), color.copy(alpha = 0.5f), color),
        startX = 350f,
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradient, alpha = 0.4f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${postulante.nombre} ${postulante.apellidos}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = postulante.grupo,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun NuevoPostulanteDialog(
    uiState: NuevoCargoUiState,
    onDismiss: () -> Unit,
    onNombreChange: (String) -> Unit,
    onApellidosChange: (String) -> Unit,
    onColorChange: (Color) -> Unit,
    onGrupoChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onAddPostulante: () -> Unit,
    grupos: List<String>,
    generos: List<String>
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("NUEVO POSTULANTE", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.dialogNombre,
                    onValueChange = onNombreChange,
                    label = { Text("Escriba el nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.dialogApellidos,
                    onValueChange = onApellidosChange,
                    label = { Text("Escriba el apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SELECCIONE EL COLOR")
                    Box(Modifier.size(50.dp).background(uiState.dialogColor, CircleShape))
                }

                ColorPicker(
                    selectedColor = uiState.dialogColor,
                    onColorSelected = onColorChange
                )
                Spacer(Modifier.height(16.dp))

                Dropdown(
                    label = "ELIJA EL GRUPO",
                    options = grupos,
                    selected = uiState.dialogGrupo,
                    onSelected = onGrupoChange
                )
                Spacer(Modifier.height(16.dp))
                Dropdown(
                    label = "GENERO",
                    options = generos,
                    selected = uiState.dialogGenero,
                    onSelected = onGeneroChange
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCELAR")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onAddPostulante,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Text("ACEPTAR")
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color(0xFFFFA726), Color(0xFF66BB6A), Color(0xFFEF5350),
        Color(0xFFAB47BC), Color(0xFFFFCA28), Color(0xFF29B6F6)
    )
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) }
                    .border(
                        width = 3.dp,
                        color = if (color.value == selectedColor.value) {
                            MaterialTheme.colorScheme.outline
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(selected.ifEmpty { "Seleccione" })
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Desplegar menú", // Evita usar "null" en la descripción para accesibilidad
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
