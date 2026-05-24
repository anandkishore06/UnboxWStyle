package com.project0.unboxwstyle.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project0.unboxwstyle.ui.screens.HomeScreen
import com.project0.unboxwstyle.ui.screens.LoginScreen
import com.project0.unboxwstyle.ui.screens.MainScreen
import com.project0.unboxwstyle.ui.screens.RecommendationHomeScreen
import com.project0.unboxwstyle.ui.screens.ChatStylistScreen
import com.project0.unboxwstyle.ui.screens.OutfitResultScreen
import com.project0.unboxwstyle.ui.screens.UploadScreen
import com.project0.unboxwstyle.ui.screens.WardrobeScreen
import com.project0.unboxwstyle.ui.screens.HowItWorksScreen
import com.project0.unboxwstyle.ui.screens.RecommendationHomeScreen
import com.project0.unboxwstyle.ui.screens.ChatStylistScreen
import com.project0.unboxwstyle.ui.screens.OutfitResultScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("main") {

            MainScreen(navController)
        }
        composable("how") {

            HowItWorksScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable("wardrobe") {

            WardrobeScreen(
                navController
            )
        }

        composable("upload") {
            UploadScreen(navController)
        }

        composable(Routes.RECOMMENDATION_HOME) {
            RecommendationHomeScreen(navController)
        }

        composable(Routes.CHAT_STYLIST) {
            ChatStylistScreen(navController)
        }

        composable(Routes.OUTFIT_RESULT) {
            OutfitResultScreen(navController)
        }

        composable("recommendation") {
            RecommendationHomeScreen(navController)
        }
    }
}
