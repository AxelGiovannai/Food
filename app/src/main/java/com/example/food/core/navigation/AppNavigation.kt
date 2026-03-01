package com.example.food.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food.feature.detail.DetailScreen
import com.example.food.feature.list.ListScreen


sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Detail : Screen("detail/{mealId}") {
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.List.route) {

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