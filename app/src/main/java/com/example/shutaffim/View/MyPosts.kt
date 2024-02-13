package com.example.shutaffim

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPosts(navController: NavController, postsVM: PostsVM , authVM: AuthViewModel) {

//    val posts by postsVM.posts.observeAsState(emptyList())
    val user by authVM.currentUser.observeAsState()

    postsVM.applyFilter(user?.email ?: "")

    val posts by postsVM.postsUser.observeAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "My Posts") }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(Screen.TypeScreen.route)
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary
            )
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    postsVM.resetPost()
                    navController.navigate(Screen.NewPostScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }, content = {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp),
            ) {
                items(posts) { post ->
                    PostItem(post, navController, Screen.EditPostScreen, postsVM)
                }
            }

        })
}


@Preview(showBackground = true)
@Composable
fun PostsSearchViewPreview() {
//    MyPosts(navController = NavController(LocalContext.current), postsVM = PostsVM())
}
