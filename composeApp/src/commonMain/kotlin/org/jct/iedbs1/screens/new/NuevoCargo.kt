package org.jct.iedbs1.screens.new

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jct.iedbs1.models.Postulante
import org.jct.iedbs1.models.toColor
import org.jct.iedbs1.ui.theme.AppDimens
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Bold
import votacion_iedbs1.composeapp.generated.resources.Res

@Composable
fun NuevoCargoRoute(
    viewModel: NuevoCargoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
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
            Card(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Guardando...", fontSize = AppDimens.body)
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
            NuevoCargoHeader(onSaveClick = onSave, onNavigateBack = onNavigateBack)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onShowDialog,
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
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
                Text("Postulantes", style = typography.titleLarge, fontSize = AppDimens.display)
                HorizontalDivider(modifier = Modifier.weight(1f).padding(start = 8.dp))
            }
            Spacer(Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.postulantes, key = { it.id }) { postulante ->
                    PostulanteCard(postulante = postulante, onDelete = { onRemovePostulante(postulante) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoCargoHeader(onSaveClick: () -> Unit, onNavigateBack: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        color = colorScheme.primary,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "NUEVO CARGO",
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title
                )
            },
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
                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Transparent)
        )
    }
}

@Composable
fun PostulanteCard(postulante: Postulante, onDelete: () -> Unit) {
    val color = postulante.color.toColor()
    val gradient = Brush.horizontalGradient(
        colors = listOf(color.copy(alpha = 0.0f), color.copy(alpha = 0.5f), color),
        startX = 450f
    )

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${postulante.nombre} ${postulante.apellidos}",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppDimens.card_title,
                        color = colorScheme.onSurface,
                        fontFamily = FontFamily(Font(Res.font.Montserrat_Bold))
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = postulante.grupo,
                        fontSize = AppDimens.card_subtitle,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("NUEVO POSTULANTE", style = typography.titleLarge, fontSize = AppDimens.headline)
                Spacer(Modifier.height(20.dp))

                OutlinedTextFieldWithCounter(
                    value = uiState.dialogNombre,
                    onValueChange = onNombreChange,
                    label = "Escriba el nombre",
                    maxLength = 50,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextFieldWithCounter(
                    value = uiState.dialogApellidos,
                    onValueChange = onApellidosChange,
                    label = "Escriba el apellido",
                    maxLength = 50,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SELECCIONE EL COLOR", fontSize = AppDimens.body)
                    Box(Modifier.size(50.dp).background(uiState.dialogColor, CircleShape))
                }

                ColorPicker(selectedColor = uiState.dialogColor, onColorSelected = onColorChange)
                Spacer(Modifier.height(16.dp))

                Dropdown(label = "SOCIEDAD", options = grupos, selected = uiState.dialogGrupo, onSelected = onGrupoChange)
                Spacer(Modifier.height(16.dp))
                Dropdown(label = "GENERO", options = generos, selected = uiState.dialogGenero, onSelected = onGeneroChange)

                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCELAR", fontSize = AppDimens.body)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onAddPostulante,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {
                        Text("AGREGAR", fontSize = AppDimens.body)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithCounter(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(modifier = modifier) {

        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = shape,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )

        val isNearLimit = value.length >= maxLength - 5

        Text(
            text = "${value.length} / $maxLength",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.End,
            color = if (isNearLimit)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}


@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    //COLORES CANDIDATOS DEL PICKER
    val colors = listOf(
        Color(0xFFB30000), //ROJO
        Color(0xFF009B00),  //VERDE OSCURO
        Color(0xFF784036), //CAFE
        Color(0xFFFF71B8), //ROSADO FUERTE
        Color(0xFFF4F139), //AMARILLO MOSTAZA
        Color(0xFF7DBEFF), //CELESTE
        Color(0xFFA4FFA4),  //VERDE CLARO
        Color(0xFFFFC1E0),  //ROSADO CLARO
        Color(0xFFFB9A39),  //ANARANJADO
        Color(0xFFC0C0C0),  //PLOMO
        Color(0xFF0053A6),  //AZUL
        Color(0xFFA87BFB),  //MORADO

    )
    Spacer(Modifier.height(8.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
fun Dropdown(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, style = typography.bodyLarge, fontSize = AppDimens.body)

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.onSecondaryContainer
                )
            ) {
                Text(selected.ifEmpty { "Seleccione" }, fontSize = AppDimens.body)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Desplegar menú",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = colorScheme.onSecondaryContainer
                )
            }

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = AppDimens.body) },
                        onClick = { onSelected(option); expanded = false }
                    )
                }
            }
        }
    }
}
