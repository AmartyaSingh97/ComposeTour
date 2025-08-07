package com.amartya.onboardingtour

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amartya.onboardingtour.ui.theme.OnboardingTourDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            OnboardingTourDemoTheme {
                OnboardingTourScreen()
            }
        }
    }
}

val tourSteps = listOf(
    TourStep(
        id = "welcome_text",
        title = "Welcome!",
        message = "This is the main screen of our app. Let's take a quick look around."
    ),
    TourStep(
        id = "profile_button",
        title = "Your Profile",
        message = "You can view and edit your profile details by tapping here."
    ),
    TourStep(
        id = "settings_button",
        title = "App Settings",
        message = "Access the application settings from this button."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingTourScreen() {
    val coroutineScope = rememberCoroutineScope()
    val tourState = rememberTourState(
        steps = tourSteps,
        coroutineScope = coroutineScope
    ) {
        Log.d("Tour", "Tour finished!")
    }

    TourHost(tourState = tourState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "My App",
                            modifier = Modifier.tourTarget(tourState, "welcome_text")
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.tourTarget(tourState, id = "profile_button")
                ) {
                    Text("View Profile")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.tourTarget(tourState, id = "settings_button")
                ) {
                    Text("Settings")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        tourState.start()
    }
}