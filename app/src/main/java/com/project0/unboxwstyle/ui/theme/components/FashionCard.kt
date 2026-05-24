package com.project0.unboxwstyle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project0.unboxwstyle.ui.theme.CardColor

@Composable
fun FashionCard(
    title: String,
    subtitle: String
) {

    Card(
        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),

        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF7B61FF),
                            Color(0xFF5B8CFF)
                        )
                    )
                )
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Column {

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp
                )
            }
        }
    }
}