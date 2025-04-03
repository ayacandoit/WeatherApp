import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.*
import com.example.weatherapp3.FavoriteLocation.FavoriteViewModel
import com.example.weatherapp3.R
import com.example.weatherapp3.data.LocalDataSource.AppDatabase
import com.example.weatherapp3.data.models.FavoriteLocation
import com.example.weatherapp3.data.repository.FavoriteRepository
import com.example.weatherapp3.data.repository.IFavoriteRepository
import kotlinx.coroutines.delay
@Composable
fun FavoriteScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FavoriteRepository(db.favoriteDao()) }
    val viewModel = remember { FavoriteViewModel(repository) }
    val state by viewModel.state.collectAsState()
    val locations by viewModel.locations.collectAsState()

    var showLoadingAnimation by remember { mutableStateOf(true) }

    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fff))
    val emptyListComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val loadingProgress by animateLottieCompositionAsState(loadingComposition)
    val emptyListProgress by animateLottieCompositionAsState(emptyListComposition)

    LaunchedEffect(Unit) {
        delay(3000)
        showLoadingAnimation = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC5E2EE))
    ) {
        if (showLoadingAnimation) {
            InitialLoadingAnimation(loadingComposition, loadingProgress)
        } else {
            when (state) {
                is FavoriteViewModel.UIState.Empty -> {
                    BackgroundImageWithAnimation(
                        emptyListComposition,
                        emptyListProgress
                    )
                }
                is FavoriteViewModel.UIState.Success -> {
                    BackgroundImageWithList(
                        locations = locations,
                        repository = repository,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                is FavoriteViewModel.UIState.Error -> {
                    // Handle error state
                }
                else -> {}
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("MapScreen") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-120).dp),
            containerColor = Color.Red
        ) {
            Icon(Icons.Default.Favorite, "Add Location", tint = Color.White)
        }


            BottomNavigationBa(navController, modifier = Modifier.align(Alignment.BottomCenter))

    }
}




@Composable
private fun InitialLoadingAnimation(
    composition: LottieComposition?,
    progress: Float
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
    }
}

@Composable
private fun BackgroundImageWithAnimation(
    composition: LottieComposition?,
    progress: Float
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.skky),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
        }
    }
}

@Composable
private fun BackgroundImageWithList(
    locations: List<FavoriteLocation>,
    repository: IFavoriteRepository,
    viewModel: FavoriteViewModel,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.skky),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        LocationsList(
            locations = locations,
            repository = repository,
            viewModel = viewModel,
            navController = navController
        )
    }
}

@Composable
private fun LocationsList(
    locations: List<FavoriteLocation>,
    repository: IFavoriteRepository,
    viewModel: FavoriteViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(locations) { location ->
            FavoriteLocationItem(
                location = location,
                onDelete = { viewModel.removeLocation(repository, it) },
                onClick = {
                    navController.navigate(
                        "weather/${location.latitude}/${location.longitude}/${location.name}"
                    )
                }
            )
        }
    }
}

@Composable
private fun FavoriteLocationItem(
    location: FavoriteLocation,
    onDelete: (FavoriteLocation) -> Unit,
    onClick: () -> Unit
) {
    val weatherIcon = when (location.name.lowercase()) {
        "new york" -> R.drawable.clearsky
        "london" -> R.drawable.heavyrain
        "cairo" -> R.drawable.clouds
        "paris" -> R.drawable.wind
        "tokyo" -> R.drawable.wind
        else -> R.drawable.snowflake
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xB3E0F7FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = weatherIcon),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(50.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${location.latitude}, ${location.longitude}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { onDelete(location) }) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun BottomNavigationBa(navController: NavController,modifier: Modifier=Modifier) {
    val currentRoute = remember { navController.currentDestination?.route }

    NavigationBar(
        containerColor = Color(0xFFC5E2EE),
        tonalElevation = 8.dp,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        val selectedItemColor = Color.Black
        val unselectedItemColor = Color.LightGray

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "Home", tint = Color.Black) },
            label = { Text("Home", color = Color.Black) },
            selected = currentRoute == "Home",
            onClick = { navController.navigate("Home") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, "Favorites", tint = Color.Black) },
            label = { Text("Favorites", color = Color.Black) },
            selected = currentRoute == "Favorite",
            onClick = { navController.navigate("Favorite") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Notifications, "Notifications", tint = Color.Black) },
            label = { Text("Notifications", color = Color.Black) },
            selected = currentRoute == "Alert",
            onClick = { navController.navigate("Alert") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, "Settings", tint = Color.Black) },
            label = { Text("Settings", color = Color.Black) },
            selected = currentRoute == "Setting",
            onClick = { navController.navigate("Setting") },
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
