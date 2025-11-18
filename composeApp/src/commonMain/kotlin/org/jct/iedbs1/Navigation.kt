package org.jct.iedbs1

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jct.iedbs1.screens.detail.DetailRoute
import org.jct.iedbs1.screens.home.HomeRoute
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.screens.info.InfoScreen
import org.jct.iedbs1.screens.login.LoginRoute
import org.jct.iedbs1.screens.new.NuevoCargoRoute
import org.jct.iedbs1.screens.new.NuevoCargoViewModel
import org.jct.iedbs1.screens.report.ReportScreen
import org.jct.iedbs1.screens.votos.RegistrarVotosRoute

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Reports : Screen("reports", "Reportes", Icons.Default.Assessment)
    object Info : Screen("info", "Info", Icons.Default.Info)
}

val items = listOf(
    Screen.Home,
    Screen.Reports,
    Screen.Info,
)

@Composable
fun AppNavigation(apikey: String, bearerToken: String) {
    val navController = rememberNavController()
    val homeViewModel = remember { HomeViewModel(apikey, bearerToken) }
    val nuevoCargoViewModel = remember { NuevoCargoViewModel(apikey, bearerToken) }
    val loggedInUser by homeViewModel.loggedInUser.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarVisibleRoutes = listOf(Screen.Home.route, Screen.Reports.route, Screen.Info.route)
    val shouldShowBottomBar = currentDestination?.route in bottomBarVisibleRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .height(95.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,

                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label, modifier = Modifier.size(17.dp)) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                    // Fourth item for Login/Logout
                    val isUserLoggedIn = loggedInUser?.valido == true
                    NavigationBarItem(
                        icon = {
                            if (isUserLoggedIn) {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Salir", modifier = Modifier.size(17.dp))
                            } else {
                                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "Login", modifier = Modifier.size(17.dp))
                            }
                        },
                        label = { Text(if (isUserLoggedIn) "Salir" else "Login") },
                        selected = currentDestination?.route == "login",
                        onClick = {
                            if (isUserLoggedIn) {
                                homeViewModel.logout()
                            } else {
                                navController.navigate("login")
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeRoute(
                    homeViewModel = homeViewModel,
                    onNavigateToNuevoCargo = { navController.navigate("nuevo_cargo") },
                    onNavigateToVotos = {
                        if (loggedInUser?.valido == true) {
                            navController.navigate("registrar_votos/${it.id}")
                        }
                    },
                    onNavigateToDetail = {
                        navController.navigate("detail/${it.id}")
                    },
                    onNavigateToLogin = { navController.navigate("login") } // This is still used by the header
                )
            }
            composable(Screen.Reports.route) {
                ReportScreen(viewModel = homeViewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Info.route) {
                InfoScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable("login") {
                LoginRoute(
                    viewModel = homeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("nuevo_cargo") {
                NuevoCargoRoute(
                    viewModel = nuevoCargoViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "registrar_votos/{cargoId}",
                arguments = listOf(navArgument("cargoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cargoId = backStackEntry.savedStateHandle.get<String>("cargoId")
                val cargo = homeViewModel.cargos.value.find { it.id == cargoId }

                if (cargo != null) {
                    RegistrarVotosRoute(
                        cargo = cargo,
                        viewModel = homeViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }
            composable(
                route = "detail/{cargoId}",
                arguments = listOf(navArgument("cargoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cargoId = backStackEntry.savedStateHandle.get<String>("cargoId")
                val cargo = homeViewModel.cargos.value.find { it.id == cargoId }

                if (cargo != null) {
                    DetailRoute(
                        cargo = cargo,
                        viewModel = homeViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }
        }
    }
}
