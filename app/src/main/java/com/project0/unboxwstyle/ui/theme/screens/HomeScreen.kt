package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

data class PinterestFashionItem(
    val title: String,
    val subtitle: String,
    val height: Int,
    val image: String
)

@Composable
fun HomeScreen(
    navController: NavController
) {

    val user =
        FirebaseAuth.getInstance().currentUser

    val styles = remember {

        listOf(

            PinterestFashionItem(
                "Luxury Korean",
                "Streetwear",
                320,
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f"
            ),

            PinterestFashionItem(
                "Minimal Black",
                "Modern Fit",
                250,
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c"
            ),

            PinterestFashionItem(
                "Old Money",
                "Elegant",
                360,
                "https://images.unsplash.com/photo-1483985988355-763728e1935b"
            ),

            PinterestFashionItem(
                "Vintage Aesthetic",
                "Retro Mood",
                270,
                "https://images.unsplash.com/photo-1529139574466-a303027c1d8b"
            ),

            PinterestFashionItem(
                "Luxury Winter",
                "Premium Style",
                350,
                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1"
            ),

            PinterestFashionItem(
                "Streetwear",
                "Oversized",
                260,
                "https://images.unsplash.com/photo-1509631179647-0177331693ae"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // TOP SECTION

        Column(
            modifier = Modifier.padding(
                horizontal = 18.dp
            )
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Discover ✨",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "AI curated fashion universe",

                        color = Color.Gray,

                        fontSize = 15.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFF1D1D1D)
                        )
                        .clickable {

                            navController.navigate(
                                "profile"
                            )
                        },

                    contentAlignment =
                        Alignment.Center
                ) {

                    if (
                        user?.photoUrl != null
                    ) {

                        AsyncImage(

                            model =
                                user.photoUrl,

                            contentDescription =
                                null,

                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    CircleShape
                                ),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        Icon(

                            Icons.Default.Person,

                            contentDescription =
                                null,

                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // STYLE CHIPS

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp),

                modifier = Modifier
                    .horizontalScroll(
                        rememberScrollState()
                    )
            ) {

                PinterestChip("Luxury")
                PinterestChip("Korean")
                PinterestChip("Streetwear")
                PinterestChip("Old Money")
                PinterestChip("Minimal")
                PinterestChip("Vintage")
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // ACTION BUTTONS

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                Button(

                    onClick = {

                        navController.navigate(
                            "upload"
                        )
                    },

                    modifier = Modifier.weight(1f),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent
                    )
                ) {

                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Upload"
                    )
                }

                OutlinedButton(

                    onClick = {

                        navController.navigate(
                            "recommendation"
                        )
                    },

                    modifier = Modifier.weight(1f),

                    shape =
                        RoundedCornerShape(20.dp)
                ) {

                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "AI Stylist"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }

        // MAIN FEED

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            verticalArrangement =
                Arrangement.spacedBy(16.dp),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp),

            contentPadding = PaddingValues(
                horizontal = 18.dp,
                vertical = 8.dp
            )
        ) {

            // FEATURED CARD

            item(
                span = {
                    androidx.compose.foundation.lazy.grid.GridItemSpan(
                        maxLineSpan
                    )
                }
            ) {

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),

                    shape = RoundedCornerShape(34.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {

                    Box(

                        modifier = Modifier
                            .fillMaxSize()

                            .background(

                                brush = Brush.linearGradient(

                                    listOf(
                                        Color(0xFF141E30),
                                        Color(0xFF243B55)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Surface(

                                shape =
                                    RoundedCornerShape(50.dp),

                                color =
                                    Color.White.copy(
                                        alpha = 0.12f
                                    )
                            ) {

                                Text(

                                    text =
                                        "✨ AI STYLE OF THE DAY",

                                    color = Color.White,

                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    ),

                                    fontSize = 12.sp
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Text(

                                text =
                                    "Luxury Monochrome Evening Fit",

                                color = Color.White,

                                fontSize = 30.sp,

                                lineHeight = 38.sp,

                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            Text(

                                text =
                                    "Minimal black layers with premium sneakers and silver accessories.",

                                color =
                                    Color.White.copy(
                                        alpha = 0.82f
                                    ),

                                fontSize = 15.sp,

                                lineHeight = 24.sp
                            )

                            Spacer(
                                modifier = Modifier.weight(1f)
                            )

                            Button(

                                onClick = {

                                    navController.navigate(
                                        "recommendation"
                                    )
                                },

                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color.White
                                )
                            ) {

                                Text(
                                    text = "Generate Similar",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // MOODS

            item(
                span = {
                    androidx.compose.foundation.lazy.grid.GridItemSpan(
                        maxLineSpan
                    )
                }
            ) {

                Column {

                    Text(

                        text = "Trending Moods ✨",

                        color = Color.White,

                        fontSize = 26.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp),

                        modifier = Modifier
                            .horizontalScroll(
                                rememberScrollState()
                            )
                    ) {

                        MoodCard(
                            "Dark Academia",
                            "☕"
                        )

                        MoodCard(
                            "Luxury Core",
                            "✨"
                        )

                        MoodCard(
                            "Coastal Fit",
                            "🌊"
                        )

                        MoodCard(
                            "Cyber Street",
                            "⚡"
                        )
                    }
                }
            }

            // PINTEREST CARDS

            items(styles) { item ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(item.height.dp),

                    shape = RoundedCornerShape(30.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF1A1A1A)
                    )
                ) {

                    Box {

                        AsyncImage(

                            model =
                                item.image,

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxSize(),

                            contentScale =
                                ContentScale.Crop
                        )

                        Box(

                            modifier = Modifier
                                .fillMaxSize()

                                .background(

                                    brush =
                                        Brush.verticalGradient(

                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(
                                                    alpha = 0.85f
                                                )
                                            )
                                        )
                                )
                        )

                        Column(

                            modifier = Modifier
                                .align(
                                    Alignment.BottomStart
                                )
                                .padding(18.dp)
                        ) {

                            Surface(

                                shape =
                                    RoundedCornerShape(
                                        50.dp
                                    ),

                                color =
                                    Accent.copy(
                                        alpha = 0.2f
                                    )
                            ) {

                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(

                                        Icons.Default.Star,

                                        contentDescription =
                                            null,

                                        tint = Accent,

                                        modifier =
                                            Modifier.size(14.dp)
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(6.dp)
                                    )

                                    Text(

                                        text = "Trending",

                                        color = Accent,

                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(

                                text =
                                    item.title,

                                color = Color.White,

                                fontSize = 22.sp,

                                lineHeight = 28.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(

                                text =
                                    item.subtitle,

                                color =
                                    Color.White.copy(
                                        alpha = 0.8f
                                    ),

                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(120.dp)
                )
            }
        }
    }
}

@Composable
fun PinterestChip(
    text: String
) {

    Surface(

        shape = RoundedCornerShape(50.dp),

        color = Color(0xFF1C1C1C)
    ) {

        Text(

            text = text,

            color = Color.White,

            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),

            fontSize = 14.sp
        )
    }
}

@Composable
fun MoodCard(
    title: String,
    emoji: String
) {

    Card(

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                Color(0xFF1B1B1B)
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 16.dp
            )
        ) {

            Text(
                text = emoji,
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(

                text = title,

                color = Color.White,

                fontWeight = FontWeight.SemiBold
            )
        }
    }
}