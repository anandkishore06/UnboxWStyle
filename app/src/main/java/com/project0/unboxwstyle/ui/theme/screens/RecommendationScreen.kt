package com.project0.unboxwstyle.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project0.unboxwstyle.BuildConfig
import com.project0.unboxwstyle.model.WardrobeItem
import com.project0.unboxwstyle.network.OpenAIImageService
import com.project0.unboxwstyle.network.OpenAIService
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    navController: NavController
) {

    val currentUser =
        FirebaseAuth.getInstance().currentUser

    val wardrobeItems = remember {
        mutableStateOf<List<WardrobeItem>>(emptyList())
    }

    var recommendation by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var imageLoading by remember {
        mutableStateOf(false)
    }

    var generatedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var chatPrompt by remember {
        mutableStateOf("")
    }

    // QUICK SELECT OPTIONS

    val occasions = listOf(
        "Date",
        "Wedding",
        "Party",
        "Office",
        "College",
        "Festival",
        "Casual"
    )

    val moods = listOf(
        "Minimal",
        "Luxury",
        "Streetwear",
        "Elegant",
        "Bold",
        "Vintage"
    )

    val weatherOptions = listOf(
        "Sunny",
        "Rainy",
        "Winter",
        "Cold",
        "Humid"
    )

    val aesthetics = listOf(
        "Modern",
        "Korean",
        "Luxury",
        "Monochrome",
        "Minimal",
        "Streetwear"
    )

    var selectedOccasion by remember {
        mutableStateOf("Date")
    }

    var selectedMood by remember {
        mutableStateOf("Luxury")
    }

    var selectedWeather by remember {
        mutableStateOf("Sunny")
    }

    var selectedAesthetic by remember {
        mutableStateOf("Modern")
    }

    // FETCH USER WARDROBE

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

                        WardrobeItem(

                            imageUrl =
                                it.getString("imageUrl")
                                    ?: "",

                            category =
                                it.getString("category")
                                    ?: ""
                        )
                    }
            }
    }

    // AI TEXT GENERATION

    fun generateAI(prompt: String) {

        loading = true

        generatedBitmap = null

        val wardrobeContext =

            wardrobeItems.value.joinToString(
                separator = "\n"
            ) {

                "- ${it.category}"
            }

        val finalPrompt =

            """
            You are a premium AI fashion stylist.

            User wardrobe:
            $wardrobeContext

            User request:
            $prompt

            IMPORTANT:
            - Recommend ONLY from available wardrobe.
            - Suggest premium outfit combinations.
            - Explain color matching.
            - Explain styling logic.
            - Keep response stylish and concise.
            """.trimIndent()

        OpenAIService.generateRecommendation(

            apiKey =
                BuildConfig.OPENAI_API_KEY,

            prompt =
                finalPrompt
        ) {

            recommendation = it

            loading = false
        }
    }

    // AI IMAGE GENERATION

    fun generateOutfitImage(prompt: String) {

        imageLoading = true

        val imagePrompt =

            """
            Luxury fashion editorial photography.

            Outfit description:
            $prompt

            Premium Korean fashion aesthetic.
            Realistic stylish outfit.
            Luxury Instagram fashion vibe.
            High quality clothing photography.
            """.trimIndent()

        OpenAIImageService.generateImage(

            apiKey =
                BuildConfig.OPENAI_API_KEY,

            prompt =
                imagePrompt
        ) {

            try {

                val imageBytes =

                    Base64.decode(
                        it,
                        Base64.DEFAULT
                    )

                generatedBitmap =

                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )

            } catch (e: Exception) {
                Log.e("AI_ERROR", "Image generation failed", e)
                recommendation = "Image generation failed: ${e.localizedMessage ?: e.toString()}"
            }

            imageLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "AI Stylist ✨",
            color = Color.White,
            fontSize = 34.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Generate premium outfit recommendations using your wardrobe.",

            color = Color.Gray,

            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // QUICK GENERATE

        Text(
            text = "Quick Generate",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        DropdownSelector(
            title = "Occasion",
            options = occasions,
            selected = selectedOccasion
        ) {
            selectedOccasion = it
        }

        DropdownSelector(
            title = "Mood",
            options = moods,
            selected = selectedMood
        ) {
            selectedMood = it
        }

        DropdownSelector(
            title = "Weather",
            options = weatherOptions,
            selected = selectedWeather
        ) {
            selectedWeather = it
        }

        DropdownSelector(
            title = "Aesthetic",
            options = aesthetics,
            selected = selectedAesthetic
        ) {
            selectedAesthetic = it
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                val prompt =

                    """
                    Occasion: $selectedOccasion
                    
                    Mood: $selectedMood
                    
                    Weather: $selectedWeather
                    
                    Aesthetic: $selectedAesthetic
                    """.trimIndent()

                generateAI(prompt)
            },

            enabled = !loading,

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Accent
            )
        ) {

            Text(
                text = "Generate Outfit"
            )
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // CHAT WITH AI

        Text(
            text = "Chat With Stylist",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(

            value = chatPrompt,

            onValueChange = {
                chatPrompt = it
            },

            label = {
                Text("Ask your stylist")
            },

            placeholder = {
                Text(
                    "Suggest luxury date outfit..."
                )
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(

            onClick = {

                generateAI(chatPrompt)
            },

            enabled = !loading,

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {

            Text(
                text = "Ask AI Stylist"
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(

            onClick = {

                generateOutfitImage(
                    chatPrompt
                )
            },

            enabled = !imageLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {

            Text(
                text = "Generate Outfit Image ✨"
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // LOADING

        if (loading) {

            CircularProgressIndicator(
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        // IMAGE LOADING

        if (imageLoading) {

            CircularProgressIndicator(
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        // TEXT RESPONSE

        if (recommendation.isNotEmpty()) {

            Card(

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF1E1E1E)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "AI Recommendation ✨",
                        color = Color.White,
                        fontSize = 22.sp
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = recommendation,

                        color =
                            Color.White.copy(alpha = 0.9f),

                        fontSize = 17.sp,

                        lineHeight = 28.sp
                    )
                }
            }
        }

        // GENERATED IMAGE

        generatedBitmap?.let { bitmap ->

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Card {

                Image(

                    bitmap =
                        bitmap.asImageBitmap(),

                    contentDescription = null,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(100.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    title: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    ExposedDropdownMenuBox(

        expanded = expanded,

        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(

            value = selected,

            onValueChange = {},

            readOnly = true,

            label = {
                Text(title)
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

            options.forEach { option ->

                DropdownMenuItem(

                    text = {
                        Text(option)
                    },

                    onClick = {

                        onSelected(option)

                        expanded = false
                    }
                )
            }
        }
    }
}