package com.project0.unboxwstyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.project0.unboxwstyle.ui.navigation.AppNavGraph
import com.project0.unboxwstyle.ui.theme.UnboxTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnboxApp()
        }
    }
}

@Composable
fun UnboxApp() {

    val navController = rememberNavController()

    UnboxTheme {
        AppNavGraph(navController)
    }
}