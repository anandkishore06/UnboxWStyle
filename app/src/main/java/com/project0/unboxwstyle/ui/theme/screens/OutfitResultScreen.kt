package com.project0.unboxwstyle.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project0.unboxwstyle.BuildConfig
import com.project0.unboxwstyle.model.WardrobeItem
import com.project0.unboxwstyle.network.OpenAIImageService
import com.project0.unboxwstyle.network.OpenAIService
import com.project0.unboxwstyle.ui.theme.Accent

@Composable
fun OutfitResultScreen(
    navController: NavController
) {
    var loading by remember { mutableStateOf(true) }
    var recommendation by remember { mutableStateOf("") }
    var isImageGenerating by remember { mutableStateOf(true) }
    
    var generatedImageUrl by remember { mutableStateOf<String?>(null) }
    var generatedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    
    var selectedWardrobeItems by remember { mutableStateOf<List<WardrobeItem>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser

    val fallbackImages = listOf(
        "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1496747611176-843222e1e57c?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1483985988355-763728e1935b?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1539109136881-3be0616acf4b?q=80&w=1000&auto=format&fit=crop"
    )

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("OutfitResult", "Current User ID: ${user?.uid}")
        Log.d("OutfitResult", "Is User Anonymous: ${user?.isAnonymous}")

        FirebaseFirestore.getInstance()
            .collection("wardrobe")
            .whereEqualTo("userId", user?.uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("OutfitResult", "Firestore Result Count: ${result.size()}")
                
                var allItems = result.documents.mapNotNull { doc ->
                    val url = doc.getString("imageUrl")
                    val cat = doc.getString("category") ?: "Unknown"
                    
                    // CRITICAL: Double check that we are not getting the category name as the URL
                    if (url != null && url.startsWith("http")) {
                        WardrobeItem(url, cat)
                    } else {
                        Log.w("OutfitResult", "Skipping invalid item: $cat (URL was $url)")
                        null
                    }
                }
                
                Log.d("OutfitResult", "Valid items in wardrobe: ${allItems.size}")
                allItems.forEachIndexed { index, item -> 
                    Log.d("OutfitResult", "Item $index: ${item.category} -> ${item.imageUrl}")
                }

                val wardrobeContext = if (allItems.isNotEmpty()) {
                    allItems.mapIndexed { index, item -> 
                        "ID_$index: ${item.category} | URL: ${item.imageUrl}"
                    }.joinToString("\n")
                } else {
                    "WARDROBE IS EMPTY."
                }

                val finalPrompt = """
                    You are a premium luxury fashion stylist.
                    
                    USER WARDROBE:
                    $wardrobeContext
                    
                    USER REQUEST: ${OutfitResultData.prompt}
                    
                    TASK:
                    Select one TOP and one BOTTOM from the list above.
                    
                    OUTPUT FORMAT (STRICT):
                    TOP_ID: [ID from the list]
                    BOTTOM_ID: [ID from the list]
                    
                    Styling Logic:
                    [Explain the premium look in one paragraph.]
                """.trimIndent()

                OpenAIService.generateRecommendation(
                    apiKey = BuildConfig.OPENAI_API_KEY,
                    prompt = finalPrompt
                ) { aiResponse ->
                    Log.d("OutfitResult", "AI RAW Response: $aiResponse")
                    recommendation = aiResponse.replace("**", "") 
                    loading = false
                    isImageGenerating = false

                    val lines = recommendation.lines()
                    val topId = lines.find { it.contains("TOP_ID:", ignoreCase = true) }?.substringAfter(":")?.trim()
                    val bottomId = lines.find { it.contains("BOTTOM_ID:", ignoreCase = true) }?.substringAfter(":")?.trim()
                    
                    Log.d("OutfitResult", "Parsed IDs -> Top: $topId, Bottom: $bottomId")
                    
                    val pickedItems = mutableListOf<WardrobeItem>()
                    
                    // Match by ID
                    try {
                        if (!topId.isNullOrBlank() && topId.startsWith("ID_")) {
                            val index = topId.substringAfter("ID_").toIntOrNull()
                            if (index != null && index < allItems.size) {
                                pickedItems.add(allItems[index])
                                Log.d("OutfitResult", "Matched Top by ID")
                            }
                        }
                        if (!bottomId.isNullOrBlank() && bottomId.startsWith("ID_")) {
                            val index = bottomId.substringAfter("ID_").toIntOrNull()
                            if (index != null && index < allItems.size) {
                                pickedItems.add(allItems[index])
                                Log.d("OutfitResult", "Matched Bottom by ID")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("OutfitResult", "ID Parsing failed", e)
                    }

                    // Fallback to searching allItems if IDs failed
                    if (pickedItems.size < 2 && allItems.isNotEmpty()) {
                        Log.d("OutfitResult", "ID matching incomplete, falling back to first 2 items")
                        val currentUrls = pickedItems.map { it.imageUrl }
                        val fallbacks = allItems.filter { it.imageUrl !in currentUrls }.take(2 - pickedItems.size)
                        pickedItems.addAll(fallbacks)
                    }

                    selectedWardrobeItems = pickedItems
                    selectedWardrobeItems.forEach { 
                        Log.d("OutfitResult", "FINAL URL FOR RENDER: ${it.imageUrl}")
                    }
                }
            }
            .addOnFailureListener {
                loading = false
                isImageGenerating = false
                Log.e("OutfitResult", "Firestore fetch failed", it)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.Black, Color(0xFF121212))))
            .verticalScroll(rememberScrollState())
    ) {
        // Top Section: Show Selected Outfit Images instead of generated image
        Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
            if (selectedWardrobeItems.isNotEmpty()) {
                // Show the selected pair side-by-side or in a row
                Row(modifier = Modifier.fillMaxSize()) {
                    selectedWardrobeItems.forEach { item ->
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(0xFF252525)), // Visible background if loading
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Accent)
                }
            } else {
                 Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No wardrobe items found", color = Color.Gray)
                }
            }
            
            // Overlay for aesthetic
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Luxury Selection ✨",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            if (loading) {
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(color = Accent)
            }

            if (recommendation.isNotEmpty()) {
                Text(
                    text = "Recommended From Your Wardrobe ✨",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )

                if (selectedWardrobeItems.isEmpty()) {
                    Text(
                        text = "Add more items to your wardrobe to see them here.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(selectedWardrobeItems) { item ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.size(180.dp)
                                ) {
                                    AsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = item.category, color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Text(
                        text = recommendation,
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 32.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
