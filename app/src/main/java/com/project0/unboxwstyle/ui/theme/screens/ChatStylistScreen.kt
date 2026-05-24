package com.project0.unboxwstyle.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project0.unboxwstyle.BuildConfig
import com.project0.unboxwstyle.model.ChatMessage
import com.project0.unboxwstyle.network.OpenAIService
import com.project0.unboxwstyle.ui.navigation.Routes
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatStylistScreen(
    navController: NavController
) {
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    var wardrobeContext by remember { mutableStateOf("User wardrobe is empty.") }

    // Fetch wardrobe data for context
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            FirebaseFirestore.getInstance()
                .collection("wardrobe")
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    val items = result.documents.mapNotNull { doc ->
                        val cat = doc.getString("category") ?: "Unknown"
                        val url = doc.getString("imageUrl") ?: ""
                        if (url.isNotEmpty()) "Item: $cat (Image: $url)" else null
                    }
                    if (items.isNotEmpty()) {
                        wardrobeContext = "USER FULL WARDROBE:\n${items.joinToString("\n")}"
                    }
                    Log.d("ChatStylist", "Full Wardrobe Context Loaded: $wardrobeContext")
                }
        }
    }

    // Add initial welcome message
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages.add(ChatMessage("Hello! I am your AI Fashion Stylist. I've analyzed your wardrobe and I'm ready to help you style the perfect outfit!", false))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // ... rest of the UI code ...
        TopAppBar(
            title = { Text("AI Stylist Chat ✨", color = Color.White, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
            if (isLoading) {
                item {
                    Text("Stylist is thinking...", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Surface(
            color = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Type a message...", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userMsg = ChatMessage(inputText, true)
                            messages.add(userMsg)
                            val currentInput = inputText
                            inputText = ""
                            isLoading = true
                            
                            scope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }

                            // LOG FOR DEBUGGING
                            Log.d("ChatStylist", "USER WARDROBE DATA BEING SENT: $wardrobeContext")

                            OpenAIService.generateChatResponse(
                                BuildConfig.OPENAI_API_KEY,
                                messages,
                                wardrobeContext
                            ) { aiResponse ->
                                isLoading = false
                                
                                // Clean the AI response to remove markdown and labels
                                val cleanAiResponse = aiResponse
                                    .replace("**", "")
                                    .replace("Assistant:", "")
                                    .replace("Stylist:", "")
                                    .trim()
                                    
                                messages.add(ChatMessage(cleanAiResponse, false))
                                scope.launch {
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .background(Accent, RoundedCornerShape(50.dp))
                        .size(48.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isUser) Accent else Color(0xFF2C2C2C)
    val textColor = Color.White

    // Extract URLs from text
    val urlRegex = "(https?://[^\\s]+)".toRegex()
    val urls = urlRegex.findAll(message.text).map { it.value }.toList()
    val cleanText = message.text.replace(urlRegex, "").trim()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                color = bubbleColor,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isUser) 16.dp else 0.dp,
                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    if (cleanText.isNotEmpty()) {
                        Text(
                            text = cleanText,
                            color = textColor,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                    }

                    if (urls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        urls.forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Outfit Item",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}
