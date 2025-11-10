
package org.jct.iedbs1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jct.iedbs1.screens.home.HomeRoute
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.screens.new.NuevoCargoRoute
import org.jct.iedbs1.screens.new.NuevoCargoViewModel


@Composable
fun AppNavigation(apikey: String, bearerToken: String) {
    val navController = rememberNavController()
    val homeViewModel = remember { HomeViewModel(apikey, bearerToken) }
    val nuevoCargoViewModel = remember { NuevoCargoViewModel(apikey, bearerToken) }


    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeRoute(
                homeViewModel = homeViewModel,
                onNavigateToNuevoCargo = { navController.navigate("nuevo_cargo") }
            )
        }
        composable("nuevo_cargo") {
            NuevoCargoRoute(
                viewModel = nuevoCargoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
