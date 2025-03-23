package com.example.weatherapp3

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp3.R

@Composable
fun SplashScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val robotoFontFamily = FontFamily(
        Font(R.font.robot, FontWeight.Normal),
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.sky),
            contentDescription = "Sky Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(composition = composition)
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = "Sky Snap",
                fontSize = 50.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFontFamily
            )
        }
    }
}
