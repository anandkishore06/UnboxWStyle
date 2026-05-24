
package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@Composable
fun HowItWorksScreen(
    navController: NavController
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(20.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "How It Works ✨",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Your AI fashion assistant in 4 simple steps.",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            HowCard(
                icon = {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.White
                    )
                },

                title = "Upload Your Outfits",

                description =
                    "Upload tops, bottoms, footwear, accessories, and more into your personal wardrobe."
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            HowCard(
                icon = {
                    Icon(
                        Icons.Default.Checkroom,
                        contentDescription = null,
                        tint = Color.White
                    )
                },

                title = "Organize Your Wardrobe",

                description =
                    "Your outfits are automatically stored in categories for smarter styling recommendations."
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            HowCard(
                icon = {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White
                    )
                },

                title = "Get AI Recommendations",

                description =
                    "Our AI suggests outfits based on mood, weather, occasion, trends, and your wardrobe."
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            HowCard(
                icon = {
                    Icon(
                        Icons.Default.Style,
                        contentDescription = null,
                        tint = Color.White
                    )
                },

                title = "Build Your Style",

                description =
                    "Create your own fashion identity with curated looks and AI styling insights."
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Button(
                onClick = {
                    navController.popBackStack()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(22.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent
                )
            ) {

                Text(
                    text = "Got It",
                    fontSize = 18.sp
                )
            }

            Spacer(
                modifier = Modifier.height(100.dp)
            )
        }
    }
}

@Composable
fun HowCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {

    Card(
        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF1E1E1E),
                            Color(0xFF121212)
                        )
                    )
                )
                .padding(22.dp)
        ) {

            Surface(
                modifier = Modifier.size(52.dp),

                shape = RoundedCornerShape(16.dp),

                color = Color.DarkGray
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = description,
                color = Color.Gray,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}
