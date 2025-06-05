package com.jacknguyen.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jacknguyen.readerapp.screen.ReaderSplashScreen
import com.jacknguyen.readerapp.screen.details.ReaderBookDetailsScreen
import com.jacknguyen.readerapp.screen.home.ReaderHomeScreen
import com.jacknguyen.readerapp.screen.login.LoginScreen
import com.jacknguyen.readerapp.screen.search.ReaderSearchScreen
import com.jacknguyen.readerapp.screen.stats.ReaderStatScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReaderScreen.SplashScreen.name
    ) {
        composable(ReaderScreen.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderHomeScreen.name) {
            ReaderHomeScreen(navController = navController)
        }
        composable(ReaderScreen.SearchScreen.name) {
            val viewModel =
                androidx.hilt.navigation.compose.hiltViewModel<com.jacknguyen.readerapp.screen.search.ReaderSearchViewModel>()
            ReaderSearchScreen(navController = navController, viewModel)
        }
        composable(ReaderScreen.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderStatsScreen.name) {
            ReaderStatScreen(navController = navController)
        }

        val detailName = ReaderScreen.DetailScreen.name
        composable("$detailName/{bookId}",
            arguments = listOf(
                navArgument("bookId") { type = androidx.navigation.NavType.StringType },
                )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId")?.let {
                ReaderBookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }
    }
}