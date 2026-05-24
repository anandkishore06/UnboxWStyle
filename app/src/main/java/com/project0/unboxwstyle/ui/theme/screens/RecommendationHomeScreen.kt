package com.project0.unboxwstyle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project0.unboxwstyle.ui.navigation.Routes
import com.project0.unboxwstyle.ui.theme.Accent
import com.project0.unboxwstyle.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationHomeScreen(
    navController: NavController
) {

    // OPTIONS FROM ORIGINAL UI
    val occasions = listOf("Date", "Wedding", "Party", "Office", "College", "Festival", "Casual")
    val moods = listOf("Minimal", "Luxury", "Streetwear", "Elegant", "Bold", "Vintage")
    val weatherOptions = listOf("Sunny", "Rainy", "Winter", "Cold", "Humid")
    val aesthetics = listOf("Modern", "Korean", "Luxury", "Monochrome", "Minimal", "Streetwear")

    var selectedOccasion by remember { mutableStateOf("Date") }
    var selectedMood by remember { mutableStateOf("Luxury") }
    var selectedWeather by remember { mutableStateOf("Sunny") }
    var selectedAesthetic by remember { mutableStateOf("Modern") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "AI Stylist ✨",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Luxury outfit recommendations from your wardrobe",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // PREMIUM CARD WITH ORIGINAL SELECTORS
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Quick Generate",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                StyledDropdownSelector(
                    title = "Occasion",
                    options = occasions,
                    selected = selectedOccasion
                ) {
                    selectedOccasion = it
                }

                StyledDropdownSelector(
                    title = "Mood",
                    options = moods,
                    selected = selectedMood
                ) {
                    selectedMood = it
                }

                StyledDropdownSelector(
                    title = "Weather",
                    options = weatherOptions,
                    selected = selectedWeather
                ) {
                    selectedWeather = it
                }

                StyledDropdownSelector(
                    title = "Aesthetic",
                    options = aesthetics,
                    selected = selectedAesthetic
                ) {
                    selectedAesthetic = it
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        OutfitResultData.prompt = """
                            I need a highly creative and stylish luxury outfit.
                            Occasion: $selectedOccasion
                            Mood: $selectedMood
                            Weather: $selectedWeather
                            Aesthetic: $selectedAesthetic
                            Please provide a detailed styling logic using only my wardrobe.
                        """.trimIndent()

                        navController.navigate(Routes.OUTFIT_RESULT)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent
                    )
                ) {
                    Text(
                        text = "Generate Luxury Look ✨",
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // CHAT ENTRY CARD
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.CHAT_STYLIST)
                }
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Chat With Stylist",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Get personalized fashion advice instantly",
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledDropdownSelector(
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
                .fillMaxWidth(),
            
            shape = RoundedCornerShape(16.dp),
            
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Accent,
                unfocusedBorderColor = Color.DarkGray,
                focusedLabelColor = Accent
            )
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
