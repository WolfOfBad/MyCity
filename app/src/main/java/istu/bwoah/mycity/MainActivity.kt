package istu.bwoah.mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import istu.bwoah.mycity.ui.theme.MyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCityTheme {
                CityGuideApp()
            }
        }
    }
}

@Composable
fun CityGuideApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("category/{name}") { backStackEntry ->
            CategoryScreen(navController, backStackEntry.arguments?.getString("name") ?: "")
        }
        composable("place/{category}/{place}") { backStackEntry ->
            PlaceDetailScreen(
                navController,
                backStackEntry.arguments?.getString("place") ?: ""
            )
        }
    }
}

data class Place(val name: String, val description: String, val imageRes: Int)

sealed class Category(val name: String, val places: List<Place>, val imageRes: Int) {
    object Cafes : Category(
        "Кафе", listOf(
            Place("Кафе 1", "Описание Кафе 1", R.drawable.cafe),
            Place("Кафе 2", "Описание Кафе 2", R.drawable.cafe),
            Place("Кафе 3", "Описание Кафе 3", R.drawable.cafe)
        ),
        R.drawable.mall
    )

    object Parks : Category(
        "Парки", listOf(
            Place("Парк 1", "Описание Парка 1", R.drawable.cafe),
            Place("Парк 2", "Описание Парка 2", R.drawable.cafe),
            Place("Парк 3", "Описание Парка 3", R.drawable.cafe)
        ),
        R.drawable.mall
    )

    object Malls : Category(
        "Торговые центры", listOf(
            Place("ТЦ 1", "Описание ТЦ 1", R.drawable.cafe),
            Place("ТЦ 2", "Описание ТЦ 2", R.drawable.cafe),
            Place("ТЦ 3", "Описание ТЦ 3", R.drawable.cafe)
        ),
        R.drawable.mall
    )
}

@Composable
fun HomeScreen(navController: NavHostController) {
    val categories = listOf(Category.Cafes, Category.Parks, Category.Malls)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Виды досуга", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            HorizontalDivider(thickness = 1.5.dp, color = Color.Black)
            LazyColumn {
                itemsIndexed(categories) { index, category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("category/${category.name}") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = category.name,
                            fontSize = 24.sp,
                        )
                    }
                    if (index < categories.lastIndex) {
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryScreen(navController: NavHostController, categoryName: String) {
    val category =
        listOf(Category.Cafes, Category.Parks, Category.Malls).find { it.name == categoryName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(
                            painterResource(id = R.drawable.back),
                            contentDescription = "Назад",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            LazyColumn {
                itemsIndexed(category?.places ?: emptyList()) { index, place ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("place/$categoryName/${place.name}") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = place.imageRes),
                            contentDescription = place.name,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = place.name,
                            fontSize = 20.sp
                        )
                    }
                    if (index < (category?.places?.lastIndex ?: 0)) {
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceDetailScreen(navController: NavHostController, placeName: String) {
    val place = listOf(Category.Cafes, Category.Parks, Category.Malls)
        .flatMap { it.places }
        .find { it.name == placeName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = placeName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(
                            painterResource(id = R.drawable.back),
                            contentDescription = "Назад",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            place?.let {
                Image(
                    painter = painterResource(id = it.imageRes),
                    contentDescription = it.name,
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it.description, fontSize = 18.sp)
            } ?: Text("Информация отсутствует")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    MyCityTheme {
        CityGuideApp()
    }
}
