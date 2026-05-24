package com.project0.unboxwstyle.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WardrobeShimmerItem() {

    val transition =
        rememberInfiniteTransition(
            label = ""
        )

    val translateAnim by transition.animateFloat(

        initialValue = 0f,

        targetValue = 1000f,

        animationSpec = infiniteRepeatable(

            animation = tween(

                durationMillis = 1200,

                easing = LinearEasing
            ),

            repeatMode = RepeatMode.Restart
        ),

        label = ""
    )

    val brush = Brush.linearGradient(

        colors = listOf(

            Color(0xFF1A1A1A),

            Color(0xFF2A2A2A),

            Color(0xFF1A1A1A)
        ),

        start = Offset(
            translateAnim,
            translateAnim
        ),

        end = Offset(
            translateAnim + 300f,
            translateAnim + 300f
        )
    )

    Card(

        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(brush)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Box(

                modifier = Modifier
                    .width(120.dp)
                    .height(18.dp)
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .background(brush)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Box(

                modifier = Modifier
                    .width(80.dp)
                    .height(14.dp)
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .background(brush)
            )
        }
    }
}