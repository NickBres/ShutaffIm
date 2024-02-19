
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Task
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shutaffim.FilterAndSearch
import com.example.shutaffim.PostItem
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsSearch(
    navController: NavController,
    postsVM: PostsVM = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {

    var showSearch by remember {
        mutableStateOf(false)
    }
    var assignedPostsOnly by remember {
        mutableStateOf(false)
    }

    val posts by postsVM.posts.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        postsVM.loadPosts()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Posts") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.TypeScreen.route)
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { assignedPostsOnly = !assignedPostsOnly }) {
                        if (assignedPostsOnly) {
                            postsVM.filterInterestedInPost(
                                authViewModel.currentUser.value?.email ?: ""
                            )
                            Icon(
                                imageVector = Icons.Filled.Task,
                                contentDescription = "Assigned Posts"
                            )
                        } else {
                            LaunchedEffect(posts) {
                                postsVM.loadPosts()
                            }
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = "Assigned Posts"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.surface,
                    actionIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    assignedPostsOnly = false
                    showSearch = !showSearch
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search And Filter")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp),
            ) {
                items(posts) { post ->
                    PostItem(post, navController, Screen.PostScreen, postsVM)
                }
            }



            if (showSearch) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSearch = false
                        postsVM.applyFilter()
                    },
                    content = {
                        FilterAndSearch(postsVM)
                    }
                )
            }
        }
    )
}

