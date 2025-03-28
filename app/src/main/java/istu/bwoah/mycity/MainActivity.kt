package istu.bwoah.mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
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
    val categories = listOf(
        Category.Cafes,
        Category.Parks,
        Category.Malls,
        Category.Saloons,
        Category.Gyms
    )
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, categories) }
        composable("category/{name}") { backStackEntry ->
            CategoryScreen(
                navController,
                categories.find {
                    it.name == (backStackEntry.arguments?.getString("name") ?: "")
                } ?: categories.first())
        }
        composable("place/{category}/{place}") { backStackEntry ->
            PlaceDetailScreen(
                navController,
                categories.flatMap { it.places }
                    .find { it.name == (backStackEntry.arguments?.getString("place") ?: "") }
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, categories: List<Category>) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "My City",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            })
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
                                .size(64.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = category.name,
                            fontSize = 28.sp,
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

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    MyCityTheme {
        CityGuideApp()
    }
}
