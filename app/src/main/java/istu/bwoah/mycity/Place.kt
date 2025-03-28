package istu.bwoah.mycity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

data class Place(val name: String, val description: String, val imageRes: Int)

@Composable
fun PlaceDetailScreen(navController: NavHostController, placeName: String) {
    val place = listOf(Category.Cafes, Category.Parks, Category.Malls)
        .flatMap { it.places }
        .find { it.name == placeName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = placeName, fontWeight = FontWeight.Bold, fontSize = 32.sp) },
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