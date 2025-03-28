package istu.bwoah.mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityGuideApp()
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
                backStackEntry.arguments?.getString("category") ?: "",
                backStackEntry.arguments?.getString("place") ?: ""
            )
        }
    }
}

data class Place(val name: String, val description: String, val imageRes: Int)

sealed class Category(val name: String, val places: List<Place>) {
    object Cafes : Category("Кафе", listOf(
        Place("Кафе 1", "Описание Кафе 1", R.drawable.cafe),
        Place("Кафе 2", "Описание Кафе 2", R.drawable.cafe),
        Place("Кафе 3", "Описание Кафе 3", R.drawable.cafe)
    ))
    object Parks : Category("Парки", listOf(
        Place("Парк 1", "Описание Парка 1", R.drawable.cafe),
        Place("Парк 2", "Описание Парка 2", R.drawable.cafe),
        Place("Парк 3", "Описание Парка 3", R.drawable.cafe)
    ))
    object Malls : Category("Торговые центры", listOf(
        Place("ТЦ 1", "Описание ТЦ 1", R.drawable.cafe),
        Place("ТЦ 2", "Описание ТЦ 2", R.drawable.cafe),
        Place("ТЦ 3", "Описание ТЦ 3", R.drawable.cafe)
    ))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val categories = listOf(Category.Cafes, Category.Parks, Category.Malls)

    Scaffold(topBar = { TopAppBar(title = { Text("Виды досуга") }) }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(categories) { category ->
                Text(
                    text = category.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("category/${category.name}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavHostController, categoryName: String) {
    val category = listOf(Category.Cafes, Category.Parks, Category.Malls).find { it.name == categoryName }

    Scaffold(topBar = { TopAppBar(title = { Text(categoryName) }) }) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(category?.places ?: emptyList()) { place ->
                Text(
                    text = place.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { navController.navigate("place/$categoryName/${place.name}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(category: String, placeName: String) {
    val place = listOf(Category.Cafes, Category.Parks, Category.Malls)
        .flatMap { it.places }
        .find { it.name == placeName }

    Scaffold(topBar = { TopAppBar(title = { Text(placeName) }) }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            place?.let {
                Image(painter = painterResource(id = it.imageRes), contentDescription = it.name, modifier = Modifier.size(250.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it.description, fontSize = 18.sp)
            } ?: Text("Информация отсутствует")
        }
    }
}
