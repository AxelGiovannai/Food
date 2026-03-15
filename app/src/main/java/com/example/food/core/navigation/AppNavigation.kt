package com.example.food.core.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food.feature.detail.DetailScreen
import com.example.food.feature.list.ListScreen
import com.example.food.feature.splash.SplashScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object List : Screen("list")
    data object Detail : Screen("detail/{mealId}") {
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}

@androidx.compose.runtime.Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.List.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.List.route) {
            ListScreen(
                onNavigateToDetail = { mealId ->
                    navController.navigate(Screen.Detail.createRoute(mealId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
