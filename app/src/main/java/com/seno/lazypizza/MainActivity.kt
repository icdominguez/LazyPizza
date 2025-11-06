package com.seno.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.lazypizza.navigation.NavigationRoot
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            LazyPizzaTheme {
                val state by mainViewModel.state.collectAsStateWithLifecycle()
                val navHostController = rememberNavController()

                NavigationRoot(
                    state = state,
                    navHostController = navHostController,
                )
            }
        }
    }
}
