package com.frontend.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
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
import com.example.wanderways.ui.pages.NotificationPage
import com.frontend.mobile.viewactivities.ViewAllRecommendedActivities
import com.frontend.mobile.ui.activitypage.MyActivityPage
import com.frontend.mobile.viewactivities.ViewAllRecommendedWardrobes
import com.frontend.mobile.viewactivities.ViewWeeklyForecast
import com.frontend.mobile.viewactivities.ScheduleActivity

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


        // My Activities Screen
        composable("my_activities") {
            MyActivityPage(
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("my_notification") {
            NotificationPage(
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            "view_all_activities/{city}",
            arguments = listOf(navArgument("city") { defaultValue = "Cebu" })
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Cebu"
            ViewAllRecommendedActivities(
                city = city,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            "view_all_wardrobe/{city}",
            arguments = listOf(navArgument("city") { defaultValue = "Cebu" })
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Cebu"
            ViewAllRecommendedWardrobes(
                city = city,
                navController = navController,
                onBackClick = { navController.popBackStack() }
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

        // View Weekly Forecast Screen
        composable(
            "view_weekly_forecast/{city}",
            arguments = listOf(navArgument("city") { defaultValue = "Cebu" })
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Cebu"
            ViewWeeklyForecast(city = city, navController = navController)
        }

        // Schedule Activity Screen
        composable(
            "schedule_activity/{activityId}/{activityName}/{activityDescription}",
            arguments = listOf(
                navArgument("activityId") { type = NavType.LongType },
                navArgument("activityName") { type = NavType.StringType },
                navArgument("activityDescription") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getLong("activityId") ?: -1L
            val activityName = backStackEntry.arguments?.getString("activityName") ?: ""
            val activityDescription = backStackEntry.arguments?.getString("activityDescription") ?: ""
            ScheduleActivity(
                activityId = activityId,
                activityName = activityName,
                activityDescription = activityDescription,
                navController = navController
            )
        }
    }
}

