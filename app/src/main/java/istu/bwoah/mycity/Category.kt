package istu.bwoah.mycity

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

sealed class Category(val name: String, val places: List<Place>, val imageRes: Int) {
    object Cafes : Category(
        "Кофейни", listOf(
            Place("Кафе 1", "Описание Кафе 1", R.drawable.cafe),
            Place("Кафе 2", "Описание Кафе 2", R.drawable.cafe),
            Place("Кафе 3", "Описание Кафе 3", R.drawable.cafe)
        ),
        R.drawable.coffee
    )

    object Parks : Category(
        "Парки", listOf(
            Place("Парк 1", "Описание Парка 1", R.drawable.cafe),
            Place("Парк 2", "Описание Парка 2", R.drawable.cafe),
            Place("Парк 3", "Описание Парка 3", R.drawable.cafe)
        ),
        R.drawable.park
    )

    object Mall : Category(
        "Торговые центры", listOf(
            Place("ТЦ 1", "Описание ТЦ 1", R.drawable.cafe),
            Place("ТЦ 2", "Описание ТЦ 2", R.drawable.cafe),
            Place("ТЦ 3", "Описание ТЦ 3", R.drawable.cafe)
        ),
        R.drawable.mall
    )

    object Saloon : Category(
        "Бары", listOf(
            Place("Бар 1", "Описание бара 1", R.drawable.cafe),
            Place("Бар 2", "Описание бара 2", R.drawable.cafe),
            Place("Бар 3", "Описание бара 3", R.drawable.cafe),
        ),
        R.drawable.saloon
    )

    object Gym : Category(
        "Спортивные залы", listOf(
            Place("Сопртивный зал 1", "Описание сопртивного зала 1", R.drawable.cafe),
            Place("Сопртивный зал 2", "Описание сопртивного зала 2", R.drawable.cafe),
            Place("Сопртивный зал 3", "Описание сопртивного зала 3", R.drawable.cafe),
        ),
        R.drawable.gym
    )
}

@Composable
fun CategoryScreen(navController: NavHostController, categoryName: String) {
    val category =
        listOf(
            Category.Cafes,
            Category.Parks,
            Category.Mall,
            Category.Saloon,
            Category.Gym
        ).find { it.name == categoryName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                },
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
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = place.name,
                            fontSize = 28.sp
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
