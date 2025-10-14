package com.seno.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.lazypizza.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            LazyPizzaTheme {
                val navHostController = rememberNavController()

                NavigationRoot(navHostController = navHostController)
            }
        }
    }
}