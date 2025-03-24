import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp3.R

@Composable
fun WeatherAlertsScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle click */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Alert", tint = Color.Black)
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.skky),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Alerts",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

                AlertItem(timeFrom = "1:00 PM", timeTo = "1:30 PM", location = "Cairo")
                AlertItem(timeFrom = "1:00 PM", timeTo = "1:30 PM", location = "Cairo")
                AlertItem(timeFrom = "1:00 PM", timeTo = "1:30 PM", location = "Cairo")
            }
        }
    }
}
@Composable
fun AlertItem(timeFrom: String, timeTo: String, location: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF64B5F6).copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "From $timeFrom To $timeTo", color = Color.White)
                Text(text = location, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Icon(
                painter = painterResource(id = R.drawable.bell),  // ضع هنا الصورة المناسبة من drawable
                contentDescription = "Alert Icon",
                Modifier.size(30.dp),
                tint = Color.Yellow
            )
        }
    }
}

@Composable
fun BottomNavigatioBar(navController: NavController) {
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
            onClick = {navController.navigate("Home")},
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
            onClick = {navController.navigate("Favorite")},
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
            onClick = {navController.navigate("Alert")},
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
            onClick = {navController.navigate("Setting")},
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