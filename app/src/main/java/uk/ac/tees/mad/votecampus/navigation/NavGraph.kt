package uk.ac.tees.mad.votecampus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
           // SplashScreen(navController = navController)
        }

        composable(route = Screen.Auth.route) {
          //  AuthScreen(navController = navController)
        }

        composable(route = Screen.Home.route) {
          //  HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Results.route,
            arguments = listOf(
                navArgument("pollId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
          //  ResultsScreen(navController = navController, pollId = pollId)
        }

        composable(route = Screen.Settings.route) {
           // SettingsScreen(navController = navController)
        }
    }
}