package com.project0.unboxwstyle.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(

    val route: String,

    val title: String,

    val icon: ImageVector
) {

    object Home : BottomNavItem(
        "home",
        "Home",
        Icons.Default.Home
    )

    object Wardrobe : BottomNavItem(
        "wardrobe",
        "Wardrobe",
        Icons.Default.Checkroom
    )

    object AI : BottomNavItem(
        "recommendation",
        "AI",
        Icons.Default.AutoAwesome
    )

    object Profile : BottomNavItem(
        "profile",
        "Profile",
        Icons.Default.Person
    )
}