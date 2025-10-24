package org.jct.iedbs1.screens.new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jct.iedbs1.models.Cargo
import org.jct.iedbs1.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoCargo(cargos: List<Cargo>, viewModel: HomeViewModel) {
    Scaffold(
        containerColor = Color(0xFF1E1E1E),
        topBar = { Header(viewModel) },
        floatingActionButton = { // 👇 Aquí agregás tu FAB
            FloatingActionButton(
                onClick = { /* acción del botón */ },
                containerColor = Color(0xFF0D47A1),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar cargo"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End // opcional: End o Center
    ) { innerPadding ->

    }
}

// --- Header ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(viewModel: HomeViewModel) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1)),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(35.dp)
            )
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ELECCIONES IEDB SANTIAGO I  |  2026-2027",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 5.sp
                )
                Text(
                    text = "Nuevo cargo",
                    color = Color(0xFFBFE0FF),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Guardar",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
                    .clickable { viewModel.cargarCargos() }
            )
        }
    )
}