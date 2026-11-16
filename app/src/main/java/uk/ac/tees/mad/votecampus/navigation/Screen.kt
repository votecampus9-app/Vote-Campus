package uk.ac.tees.mad.votecampus.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Results : Screen("results/{pollId}") {
        fun createRoute(pollId: String) = "results/$pollId"
    }
    object Settings : Screen("settings")
}