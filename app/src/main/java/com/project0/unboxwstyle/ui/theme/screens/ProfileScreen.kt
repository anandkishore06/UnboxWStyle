package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit = {}
) {

    val auth = FirebaseAuth.getInstance()

    val user = auth.currentUser

    var wardrobeCount by remember {
        mutableStateOf(0)
    }

    var aiGeneratedCount by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {

        FirebaseFirestore
            .getInstance()
            .collection("wardrobe")

            .whereEqualTo(
                "userId",
                user?.uid
            )

            .get()

            .addOnSuccessListener {

                wardrobeCount =
                    it.documents.size
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Profile",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // PREMIUM PROFILE CARD

        Card(

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(28.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {

            Box(

                modifier = Modifier
                    .background(

                        brush = Brush.linearGradient(

                            listOf(
                                Color(0xFF232526),
                                Color(0xFF414345)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {

                Column {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // PROFILE IMAGE

                        if (
                            !user?.photoUrl.toString()
                                .isNullOrEmpty()
                        ) {

                            AsyncImage(

                                model =
                                    user?.photoUrl,

                                contentDescription = null,

                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape),

                                contentScale =
                                    ContentScale.Crop
                            )

                        } else {

                            Surface(

                                modifier = Modifier
                                    .size(90.dp),

                                shape = CircleShape,

                                color = Accent
                            ) {

                                Box(
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Icon(

                                        Icons.Default.Person,

                                        contentDescription =
                                            null,

                                        tint = Color.White,

                                        modifier =
                                            Modifier.size(42.dp)
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.width(18.dp)
                        )

                        Column {

                            Text(

                                text =
                                    user?.displayName
                                        ?: "Fashion Explorer",

                                color = Color.White,

                                fontSize = 24.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(

                                text =
                                    user?.phoneNumber
                                        ?: user?.email
                                        ?: "",

                                color =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    ),

                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Icon(

                                    Icons.Default.Star,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFFFFD54F),

                                    modifier =
                                        Modifier.size(18.dp)
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(6.dp)
                                )

                                Text(

                                    text =
                                        "Premium AI Stylist",

                                    color = Color.White,

                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(28.dp)
                    )

                    // STATS

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        PremiumStatCard(
                            title = "Wardrobe",
                            value = wardrobeCount.toString(),
                            icon = Icons.Default.Collections
                        )

                        PremiumStatCard(
                            title = "AI Looks",
                            value = "24",
                            icon = Icons.Default.AutoAwesome
                        )

                        PremiumStatCard(
                            title = "Favorites",
                            value = "12",
                            icon = Icons.Default.Favorite
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // STYLE DNA

        Text(
            text = "Your Style DNA ✨",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyRow(
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            item {

                StyleChip("Luxury")
            }

            item {

                StyleChip("Streetwear")
            }

            item {

                StyleChip("Minimal")
            }

            item {

                StyleChip("Korean")
            }

            item {

                StyleChip("Modern")
            }
        }

        Spacer(
            modifier = Modifier.height(34.dp)
        )

        // MENU SECTION

        PremiumMenuItem(

            title = "My Wardrobe",

            subtitle =
                "Manage your uploaded outfits",

            icon = Icons.Default.Collections
        ) {

            navController.navigate(
                "wardrobe"
            )
        }

        PremiumMenuItem(

            title = "AI Styling History",

            subtitle =
                "View generated outfit ideas",

            icon = Icons.Default.AutoAwesome
        ) {

        }

        PremiumMenuItem(

            title = "Appearance",

            subtitle =
                "Dark mode & personalization",

            icon = Icons.Default.DarkMode
        ) {

        }

        PremiumMenuItem(

            title = "Settings",

            subtitle =
                "Privacy, account & app settings",

            icon = Icons.Default.Settings
        ) {

        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(

            onClick = {

                FirebaseAuth
                    .getInstance()
                    .signOut()

                onLogout()
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),

            shape = RoundedCornerShape(18.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Accent
            )
        ) {

            Icon(
                Icons.Default.Logout,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Text(
                text = "Logout",
                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun PremiumStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {

    Card(

        modifier = Modifier.width(105.dp),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                Color.White.copy(alpha = 0.08f)
        )
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = Accent
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = title,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun StyleChip(
    text: String
) {

    Surface(

        shape = RoundedCornerShape(50.dp),

        color = Accent.copy(alpha = 0.15f)
    ) {

        Text(

            text = text,

            color = Accent,

            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),

            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PremiumMenuItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                Color.White.copy(alpha = 0.05f)
        )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(

                shape = CircleShape,

                color = Accent.copy(alpha = 0.15f)
            ) {

                Box(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Accent
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}