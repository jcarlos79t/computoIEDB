package org.jct.iedbs1.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jct.iedbs1.Utils
import org.jct.iedbs1.models.Cargo
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Black
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Bold
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Light
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Medium
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Regular
import votacion_iedbs1.composeapp.generated.resources.Montserrat_SemiBold
import votacion_iedbs1.composeapp.generated.resources.Res

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    onNavigateToNuevoCargo: () -> Unit,
    onNavigateToVotos: (Cargo) -> Unit,
    onNavigateToDetail: (Cargo) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val cargos by homeViewModel.cargos.collectAsState()
    val loggedInUser by homeViewModel.loggedInUser.collectAsState()

    HomeScreen(
        cargos = cargos,
        viewModel = homeViewModel,
        onNavigateToNuevoCargo = onNavigateToNuevoCargo,
        onNavigateToVotos = onNavigateToVotos,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToLogin = onNavigateToLogin,
        isUserLoggedIn = loggedInUser?.valido == true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cargos: List<Cargo>,
    viewModel: HomeViewModel,
    onNavigateToNuevoCargo: () -> Unit,
    onNavigateToVotos: (Cargo) -> Unit,
    onNavigateToDetail: (Cargo) -> Unit,
    onNavigateToLogin: () -> Unit,
    isUserLoggedIn: Boolean
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { Header(viewModel, onNavigateToLogin) },
        floatingActionButton = {
            if (isUserLoggedIn) {
                FloatingActionButton(
                    onClick = onNavigateToNuevoCargo,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar cargo"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            //SearchBar()

            if (cargos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cargos) { cargo ->
                        CargoCard(
                            cargo = cargo,
                            onNavigateToDetail = { onNavigateToDetail(cargo) },
                            onNavigateToVotos = { onNavigateToVotos(cargo) },
                            showAdminActions = isUserLoggedIn
                        )
                    }
                }
            }
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(viewModel: HomeViewModel, onNavigateToLogin: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = onNavigateToLogin) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Login",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(35.dp)
                )
            }
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ELECCIONES 2026-2027",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 5.sp

                )
                Text(
                    text = "Iglesia Evangelica de Dios Boliviana - Santiago I",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                )
                Text(
                    text = "Dep. Sistemas ©2025 Area AudioVisual",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sincronizar",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
                    .clickable { viewModel.cargarCargos() }
            )
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(viewModel: HomeViewModel, onNavigateToLogin: () -> Unit) {
    // Surface con esquinas inferiores redondeadas y sombra suave
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
        shadowElevation = 8.dp, // <-- sombra más marcada
        tonalElevation = 3.dp   // <-- añade un efecto de elevación más realista en Material3
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // usamos el color del Surface
            ),
            navigationIcon = {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Login",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(35.dp)
                    )
                }
            },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ELECCIONES 2026-2027",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 5.sp,
                        fontFamily = FontFamily( Font( Res.font.Montserrat_Black))
                    )
                    Text(
                        text = "Iglesia Evangelica de Dios Boliviana - Santiago I",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp,
                        fontFamily = FontFamily( Font( Res.font.Montserrat_SemiBold))
                    )
                    Text(
                        text = "Dep. Sistemas ©2025 Area AudioVisual",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily( Font( Res.font.Montserrat_Medium))
                    )
                }
            },
            actions = {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sincronizar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp)
                        .clickable { viewModel.cargarCargos() }
                )
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        label = { Text("Buscar cargo") },
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(34.dp)),
    )
}

@Composable
fun CargoCard(
    cargo: Cargo,
    onNavigateToDetail: () -> Unit,
    onNavigateToVotos: () -> Unit,
    showAdminActions: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigateToDetail),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Utils.formatFecha(cargo.fecha).uppercase(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontFamily = FontFamily( Font( Res.font.Montserrat_Regular))
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Utils.getColorEstado(cargo.estado).copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        cargo.estado.uppercase(),
                        color = Utils.getColorEstado(cargo.estado),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        fontFamily = FontFamily( Font( Res.font.Montserrat_SemiBold))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                cargo.cargo.uppercase(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily( Font( Res.font.Montserrat_Bold))
            )

            cargo.ganador?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text("🥇 GANADOR  $it",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily( Font( Res.font.Montserrat_SemiBold))
                )
            }

            if (showAdminActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onNavigateToVotos) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Ir a Votos",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}