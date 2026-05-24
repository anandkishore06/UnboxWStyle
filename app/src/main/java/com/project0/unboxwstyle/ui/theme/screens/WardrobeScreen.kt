package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project0.unboxwstyle.model.WardrobeItem
import com.project0.unboxwstyle.ui.components.WardrobeShimmerItem
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@Composable
fun WardrobeScreen(
    navController: NavController
) {

    val currentUser =
        FirebaseAuth.getInstance().currentUser

    val wardrobeItems = remember {
        mutableStateOf<List<Pair<String, WardrobeItem>>>(
            emptyList()
        )
    }

    var loading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {

        FirebaseFirestore
            .getInstance()
            .collection("wardrobe")

            .whereEqualTo(
                "userId",
                currentUser?.uid
            )

            .get()

            .addOnSuccessListener { result ->

                wardrobeItems.value =

                    result.documents.map {

                        Pair(

                            it.id,

                            WardrobeItem(

                                imageUrl =
                                    it.getString("imageUrl")
                                        ?: "",

                                category =
                                    it.getString("category")
                                        ?: ""
                            )
                        )
                    }

                loading = false
            }

            .addOnFailureListener {

                loading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 18.dp)
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "My Wardrobe ✨",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Your AI-powered premium fashion collection.",

            color = Color.Gray,

            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        if (loading) {

            LazyVerticalGrid(

                columns = GridCells.Fixed(2),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp),

                horizontalArrangement =
                    Arrangement.spacedBy(16.dp),

                modifier = Modifier.fillMaxSize()
            ) {

                items(6) {

                    WardrobeShimmerItem()
                }
            }

        } else {

            LazyVerticalGrid(

                columns = GridCells.Fixed(2),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp),

                horizontalArrangement =
                    Arrangement.spacedBy(16.dp),

                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    wardrobeItems.value
                ) { (docId, item) ->

                    Card(

                        shape = RoundedCornerShape(28.dp),

                        colors = CardDefaults.cardColors(
                            containerColor =
                                Color(0xFF171717)
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {

                        Column {

                            Box {

                                AsyncImage(

                                    model =
                                        item.imageUrl,

                                    contentDescription = null,

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),

                                    contentScale =
                                        ContentScale.Crop
                                )

                                // CATEGORY TAG

                                Surface(

                                    modifier = Modifier
                                        .padding(12.dp),

                                    shape =
                                        RoundedCornerShape(50.dp),

                                    color =
                                        Color.Black.copy(
                                            alpha = 0.55f
                                        )
                                ) {

                                    Text(

                                        text =
                                            item.category,

                                        color = Color.White,

                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 7.dp
                                        ),

                                        fontSize = 12.sp,

                                        fontWeight =
                                            FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 14.dp,
                                        vertical = 12.dp
                                    ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text(

                                        text =
                                            item.category,

                                        color = Color.White,

                                        fontSize = 16.sp,

                                        fontWeight =
                                            FontWeight.Bold
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(4.dp)
                                    )

                                    Text(

                                        text =
                                            "AI Styled",

                                        color = Accent,

                                        fontSize = 12.sp
                                    )
                                }

                                IconButton(

                                    onClick = {

                                        FirebaseFirestore
                                            .getInstance()
                                            .collection(
                                                "wardrobe"
                                            )
                                            .document(docId)
                                            .delete()

                                        wardrobeItems.value =

                                            wardrobeItems.value
                                                .filterNot {

                                                    it.first == docId
                                                }
                                    }
                                ) {

                                    Icon(

                                        Icons.Default.Delete,

                                        contentDescription =
                                            null,

                                        tint = Color.Red
                                    )
                                }
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
}