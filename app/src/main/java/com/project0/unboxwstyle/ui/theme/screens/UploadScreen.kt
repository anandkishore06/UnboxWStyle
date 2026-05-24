package com.project0.unboxwstyle.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    navController: NavController
) {

    val currentUser =
        FirebaseAuth.getInstance().currentUser

    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val categories = listOf(

        "Top",
        "Bottom",
        "Footwear",
        "Outerwear",
        "Accessories",
        "Ethnic",
        "Activewear",
        "Formal",
        "Casual",
        "Luxury"
    )

    var selectedCategory by remember {
        mutableStateOf("Top")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetMultipleContents()
        ) {

            imageUris = it
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp
            )
        ) {

            Text(
                text = "Upload Outfits ✨",
                color = Color.White,
                fontSize = 34.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "Build your AI-powered fashion wardrobe.",

                color = Color.Gray,

                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),

            shape = RoundedCornerShape(32.dp),

            colors = CardDefaults.cardColors(
                containerColor =
                    Color(0xFF1E1E1E)
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                if (imageUris.isNotEmpty()) {

                    Image(
                        painter =
                            rememberAsyncImagePainter(
                                imageUris.first()
                            ),

                        contentDescription = null,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(
                                RoundedCornerShape(24.dp)
                            ),

                        contentScale = ContentScale.Crop
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "${imageUris.size} outfit(s) selected",

                        color = Color.White,

                        fontSize = 15.sp
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(
                                RoundedCornerShape(24.dp)
                            )
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.DarkGray,
                                        Color.Black
                                    )
                                )
                            ),

                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,

                                tint = Color.White,

                                modifier = Modifier.size(50.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                text = "Select your outfits",
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // CATEGORY

                ExposedDropdownMenuBox(

                    expanded = expanded,

                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(

                        value = selectedCategory,

                        onValueChange = {},

                        readOnly = true,

                        label = {
                            Text("Category")
                        },

                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(expanded)
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(

                        expanded = expanded,

                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        categories.forEach { category ->

                            DropdownMenuItem(

                                text = {
                                    Text(category)
                                },

                                onClick = {

                                    selectedCategory =
                                        category

                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Button(
                    onClick = {

                        launcher.launch("image/*")
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
                        text = "Choose Outfits",
                        fontSize = 17.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Button(
                    onClick = {

                        if (imageUris.isEmpty()) {

                            Toast.makeText(
                                navController.context,
                                "Choose images first",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@Button
                        }

                        loading = true

                        imageUris.forEach { uri ->

                            val storageRef =
                                FirebaseStorage
                                    .getInstance()
                                    .reference
                                    .child(
                                        "wardrobe/${System.currentTimeMillis()}"
                                    )

                            storageRef.putFile(uri)

                                .continueWithTask {

                                    storageRef.downloadUrl
                                }

                                .addOnSuccessListener { downloadUri ->

                                    val imageUrl =
                                        downloadUri.toString()

                                    val data = hashMapOf(

                                        "imageUrl" to imageUrl,

                                        "category" to selectedCategory,

                                        "userId" to currentUser?.uid,

                                        "createdAt" to System.currentTimeMillis()
                                    )

                                    FirebaseFirestore
                                        .getInstance()
                                        .collection("wardrobe")
                                        .add(data)

                                    loading = false
                                }
                        }

                        imageUris = emptyList()

                        Toast.makeText(
                            navController.context,
                            "Outfits uploaded ✨",
                            Toast.LENGTH_SHORT
                        ).show()
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(22.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {

                    if (loading) {

                        CircularProgressIndicator(
                            color = Color.Black
                        )

                    } else {

                        Text(
                            text = "Upload Wardrobe",
                            color = Color.Black,

                            fontSize = 17.sp
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(120.dp)
        )
    }
}