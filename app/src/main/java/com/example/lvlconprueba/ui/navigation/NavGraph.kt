package com.example.lvlconprueba.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lvlconprueba.ui.home.HomeScreen
import com.example.lvlconprueba.ui.login.LoginScreen
import com.example.lvlconprueba.ui.splash.SplashScreen
import com.example.lvlconprueba.ui.profile.ProfileScreen
import com.example.lvlconprueba.ui.profile.EditPhotoScreen

sealed class AppScreen(val route: String) {
    object Splash : AppScreen("splash")
    object Login : AppScreen("login")
    object Home : AppScreen("home")
    object Profile : AppScreen("profile")
    object EditPhoto : AppScreen("edit_photo")
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Splash.route
    ) {
        composable(route = AppScreen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(AppScreen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(AppScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = AppScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(AppScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = AppScreen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(AppScreen.Profile.route)
                }
            )
        }
        composable(route = AppScreen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEditPhoto = { navController.navigate(AppScreen.EditPhoto.route) }
            )
        }
        composable(route = AppScreen.EditPhoto.route) {
            EditPhotoScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
