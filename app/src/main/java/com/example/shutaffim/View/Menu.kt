package com.example.shutaffim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Model.UserType
import com.example.shutaffim.ViewModel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Menu(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var currUser = authViewModel.currentUser.observeAsState()
    authViewModel.updateUser()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text(text = "Login") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.LoginScreen.route)
                    }) {
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
        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(100.dp))


                ElevatedCard {

                    Spacer(modifier = Modifier.height(8.dp))

                }

                Spacer(modifier = Modifier.height(90.dp))
                if (currUser.value?.type == UserType.Publisher.type) {
                    ExtendedFloatingActionButton(
                        onClick = { /* TODO: Navigate to login screen */
                            navController.navigate(Screen.MyPostsScreen.route)
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Edit,
                                "Extended floating action button.",
                                modifier = Modifier.size(32.dp) // Adjust the size as needed
                            )
                        },
                        text = {
                            Text(
                                text = "Posting",
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        modifier = Modifier
                            .height(100.dp)
                            .size(280.dp),

                        )
                } else if (currUser.value?.type == UserType.Consumer.type) {

                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.PostsSearchScreen.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Search,
                                "Search icon",
                                modifier = Modifier.size(32.dp) // Adjust the size as needed
                            )
                        },
                        text = {
                            Text(
                                text = "Searching",
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        modifier = Modifier
                            .height(100.dp)
                            .size(280.dp)
                    )
                }

                Spacer(modifier = Modifier.height(70.dp))

                TextButton(onClick = {
                    navController.navigate(Screen.ProfileScreen.route)
                }) {
                    Text(
                        text = "Profile",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }
        })
}

@Composable
fun MenuScreen(navController: NavController, authViewModel: AuthViewModel) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Menu(navController, authViewModel)
    }
}


