package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.project0.unboxwstyle.ui.navigation.BottomNavItem
import com.project0.unboxwstyle.ui.theme.Background
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project0.unboxwstyle.ui.screens.HowItWorksScreen
import com.project0.unboxwstyle.ui.navigation.Routes

@Composable
fun MainScreen(rootNavController: NavController) {

    val navController =
        rememberNavController()

    val items = listOf(

        BottomNavItem.Home,
        BottomNavItem.Wardrobe,
        BottomNavItem.AI,
        BottomNavItem.Profile
    )

    Scaffold(

        containerColor = Background,

        bottomBar = {

            NavigationBar(
                containerColor =
                    Color(0xFF121212)
            ) {

                val currentRoute =
                    navController
                        .currentBackStackEntryAsState()
                        .value
                        ?.destination
                        ?.route

                items.forEach { item ->

                    NavigationBarItem(

                        selected =
                            currentRoute == item.route,

                        onClick = {

                            navController.navigate(
                                item.route
                            ) {

                                popUpTo(
                                    navController
                                        .graph
                                        .startDestinationId
                                )

                                launchSingleTop = true
                            }
                        },

                        icon = {

                            Icon(
                                item.icon,
                                contentDescription = null
                            )
                        },

                        label = {

                            Text(item.title)
                        },

                        colors =
                            NavigationBarItemDefaults
                                .colors(

                                    selectedIconColor =
                                        Color.White,

                                    selectedTextColor =
                                        Color.White,

                                    indicatorColor =
                                        Color.DarkGray
                                )
                    )
                }
            }
        }
    ) { padding ->

        NavHost(

            navController = navController,

            startDestination = "home",

            modifier = Modifier
                .padding(padding)
                .background(Background)
        ) {

            composable("home") {
                HomeScreen(navController)
            }
            composable("how") {

                HowItWorksScreen(navController)
            }

            composable("wardrobe") {
                WardrobeScreen(navController)
            }

            composable("recommendation") {
                RecommendationHomeScreen(navController)
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

            composable("upload") {
                UploadScreen(navController)
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    onLogout = {
                        rootNavController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}