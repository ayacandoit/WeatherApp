import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp3.R

@Composable
fun SettingsScreen() {
    var selectedLanguage by remember { mutableStateOf("ARABIC") }
    var selectedWindSpeed by remember { mutableStateOf("IMPARIAL") }
    var selectedTemperature by remember { mutableStateOf("KELVIN") }
    var selectedLocation by remember { mutableStateOf("GPS") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.skky),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.8f }
        )

        // المحتوى فوق الصورة
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Settings", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(32.dp))

                SettingItem("Language", listOf("ENGLISH", "ARABIC"), selectedLanguage) { selectedLanguage = it }

                Spacer(modifier = Modifier.height(32.dp))

                SettingItem("Wind speed", listOf("IMPARIAL", "METRIC"), selectedWindSpeed) { selectedWindSpeed = it }

                Spacer(modifier = Modifier.height(32.dp))

                SettingItem("Temperature", listOf("CELSIUS", "KELVIN", "FAHRENHEIT"), selectedTemperature) { selectedTemperature = it }

                Spacer(modifier = Modifier.height(32.dp))

                SettingItem("Location", listOf("MAP", "GPS"), selectedLocation) { selectedLocation = it }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Bottom()
        }
    }
}

@Composable
fun SettingItem(title: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    Text(title, fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.SemiBold)

    Row(modifier = Modifier.padding(top = 8.dp)) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onOptionSelected(option) }
                    .background(
                        if (option == selectedOption) Color(0xFF67A3FF) else Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (option == selectedOption) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun Bottom() {
    NavigationBar(
        containerColor = Color(0xFFC5E2EE),
        tonalElevation = 8.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        val selectedItemColor = Color.Black
        val unselectedItemColor = Color.LightGray

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Black) },
            label = { Text("Home", color = Color.Black) },
            selected = true,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites", tint = Color.Black) },
            label = { Text("Favorites", color = Color.Black) },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.Black) },
            label = { Text("Notifications", color = Color.Black) },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.Black) },
            label = { Text("Settings", color = Color.Black) },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
    }
}