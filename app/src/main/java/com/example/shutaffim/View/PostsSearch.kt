import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shutaffim.FilterAndSearch
import com.example.shutaffim.Post
import com.example.shutaffim.PostItem
import com.example.shutaffim.R
import com.example.shutaffim.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsSearch(navController: NavController) {

    val stam = 9000
    val pic = listOf(R.drawable.test_image)

    val dummyList = listOf<Post>(
        Post(pic, 5000, "Tel-Aviv, Dizengoff, 42", "18.01.2023"),
        Post(pic, 7500, "Jerusalem, Yafo, 30", "20.02.2023"),
        Post(pic, 6200, "Haifa, Herzl, 15", "05.03.2023"),
        Post(pic, 4300, "Eilat, Shalom, 8", "12.04.2023"),
        Post(pic, 8000, "Tel-Aviv, Ben Yehuda, 21", "25.05.2023"),
        Post(pic, 5400, "Ashdod, HaAtzmaut, 33", "10.06.2023"),
        Post(pic, 4700, "Be'er Sheva, HaNevi'im, 7", "15.07.2023"),
        Post(pic, 6900, "Netanya, Herzl, 45", "22.08.2023"),
        Post(pic, 5100, "Rishon LeZion, Rothschild, 18", "30.09.2023"),
        Post(pic, 5600, "Haifa, Allenby, 9", "11.10.2023")
    )
    var showSearch by remember {
        mutableStateOf(false)
    }
    var assignedPostsOnly by remember {
        mutableStateOf(true)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Posts") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { assignedPostsOnly = !assignedPostsOnly }) {
                        if (assignedPostsOnly) {
                            Icon(
                                painter = painterResource(id = R.drawable.task_icon),
                                contentDescription = "Assigned Posts",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.desc_icon),
                                contentDescription = "Assigned Posts",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
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
                    PostItem(post,navController, Screen.PostScreen)
                }
            }



            if (showSearch) {
                ModalBottomSheet(
                    onDismissRequest = { showSearch = false },
                    content = {
                        FilterAndSearch()
                    }
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PostsSearchViewPreview() {
    PostsSearch(navController = NavController(LocalContext.current))
}

