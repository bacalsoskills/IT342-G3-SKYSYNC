package com.frontend.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frontend.mobile.ui.loginpage.LoginScreen
import com.frontend.mobile.ui.loginpage.RegisterScreen
import com.frontend.mobile.ui.theme.MobileTheme
import com.frontend.mobile.ui.activitypage.HomePage
import com.frontend.mobile.ui.profile.UserProfileScreen
import com.frontend.mobile.ui.settings.SettingsScreen
import com.frontend.mobile.ui.aboutus.AboutUsScreen
import com.frontend.mobile.ui.activitypage.WeatherActivityPage
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // Login Screen
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register") // Navigate to the register screen
                }
            )
        }

        // Register Screen
        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack() // Navigate back to the login screen
                }
            )
        }

        // Home Page
        composable("home") {
            HomePage(navController = navController)
        }

        // User Profile Screen
        composable("user_profile") {
            UserProfileScreen(navController = navController)
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // About Us Screen
        composable("about_us") {
            AboutUsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Weather Activities Screen
        composable(
            "weather_activities/{weatherCode}",
            arguments = listOf(navArgument("weatherCode") { defaultValue = 0 })
        ) { backStackEntry ->
            val weatherCode = backStackEntry.arguments?.getString("weatherCode")?.toIntOrNull() ?: 0
            WeatherActivityPage(
                weatherCode = weatherCode,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
