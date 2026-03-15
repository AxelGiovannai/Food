package com.example.food.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.food.core.ui.theme.OrangePrimary
import com.example.food.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val context = LocalContext.current
    val hasNavigated = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500)
        if (!hasNavigated.value) {
            hasNavigated.value = true
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangePrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.logo_food)
                    // Decode splash logo at target display size, not source dimensions.
                    .size(width = 512, height = 512)
                    .crossfade(true)
                    .build(),
                contentDescription = "Food Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )
        }

        TextButton(
            onClick = {
                if (!hasNavigated.value) {
                    hasNavigated.value = true
                    onSplashFinished()
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Passer")
        }
    }
}
