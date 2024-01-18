import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shutaffim.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsSearchView() {

    val dummyList = listOf<Post>(
        Post(R.drawable.test_image, 5000, "Tel-Aviv, Dizengoff, 42", "18.01.2023"),
        Post(R.drawable.test_image, 7500, "Jerusalem, Yafo, 30", "20.02.2023"),
        Post(R.drawable.test_image, 6200, "Haifa, Herzl, 15", "05.03.2023"),
        Post(R.drawable.test_image, 4300, "Eilat, Shalom, 8", "12.04.2023"),
        Post(R.drawable.test_image, 8000, "Tel-Aviv, Ben Yehuda, 21", "25.05.2023"),
        Post(R.drawable.test_image, 5400, "Ashdod, HaAtzmaut, 33", "10.06.2023"),
        Post(R.drawable.test_image, 4700, "Be'er Sheva, HaNevi'im, 7", "15.07.2023"),
        Post(R.drawable.test_image, 6900, "Netanya, Herzl, 45", "22.08.2023"),
        Post(R.drawable.test_image, 5100, "Rishon LeZion, Rothschild, 18", "30.09.2023"),
        Post(R.drawable.test_image, 5600, "Haifa, Allenby, 9", "11.10.2023")
    )
    var showSearch by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Posts") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO Handle back button click here */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSearch = !showSearch },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Search, contentDescription = "Add")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp),
            ) {
                items(dummyList) { post ->
                    PostItem(post)
                }
            }

            if (showSearch) {
                ModalBottomSheet(onDismissRequest = { showSearch = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Search",
                            style = TextStyle(
                                fontSize = 40.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(128.dp))
                        Text(
                            text = "Coming soon...",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(post: Post) {
    val size = 100.dp

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            focusedElevation = 256.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface

        ),
        onClick = { /* TODO Enter to the item screen*/ }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(size)
        ) {
            Image(
                painter = painterResource(id = post.image),
                contentDescription = "",
                modifier = Modifier
                    .height(size)
                    .width(size),
                contentScale = ContentScale.Crop
            )
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${post.price}₪",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = post.date
                    )


                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = post.location,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun PostsSearchViewPreview() {
    PostsSearchView()
}

// for test only
data class Post(
    val image: Int,
    val price: Int,
    val location: String,
    val date: String
)