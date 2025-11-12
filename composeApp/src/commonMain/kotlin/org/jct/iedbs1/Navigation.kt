package org.jct.iedbs1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jct.iedbs1.screens.detail.DetailRoute
import org.jct.iedbs1.screens.home.HomeRoute
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.screens.login.LoginRoute
import org.jct.iedbs1.screens.new.NuevoCargoRoute
import org.jct.iedbs1.screens.new.NuevoCargoViewModel
import org.jct.iedbs1.screens.votos.RegistrarVotosRoute


@Composable
fun AppNavigation(apikey: String, bearerToken: String) {
    val navController = rememberNavController()
    val homeViewModel = remember { HomeViewModel(apikey, bearerToken) }
    val nuevoCargoViewModel = remember { NuevoCargoViewModel(apikey, bearerToken) }
    val loggedInUser by homeViewModel.loggedInUser.collectAsState()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeRoute(
                homeViewModel = homeViewModel,
                onNavigateToNuevoCargo = { navController.navigate("nuevo_cargo") },
                onNavigateToVotos = {
                    if (loggedInUser?.valido == true) {
                        navController.navigate("registrar_votos/${it.id}")
                    } else {
                        // Optionally, show a toast or message
                    }
                },
                onNavigateToDetail = {
                     navController.navigate("detail/${it.id}")
                },
                onNavigateToLogin = { navController.navigate("login") }
            )
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
