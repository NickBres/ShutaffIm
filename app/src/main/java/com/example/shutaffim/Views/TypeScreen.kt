package com.example.shutaffim

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeScreenView(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text(text = "Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        androidx.compose.material3.Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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

//            Button(
//                onClick = { /* TODO: Handle registration logic */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Text(text = "Looking")
//                    Image(
//                        painter = painterResource(id = R.drawable.looking),
//                        contentDescription = "Icon",
//                        modifier = Modifier
//                            .size(24.dp) // Adjust the size as needed
//                    )
//                }
//            }

        }

        Spacer(modifier = Modifier.height(90.dp))

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
                .size(280.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))

        ExtendedFloatingActionButton(
            onClick = { /* TODO: Navigate to login screen */
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

        Spacer(modifier = Modifier.height(70.dp))

        TextButton(onClick = { /*TODO*/
            navController.navigate(Screen.EditProfileScreen.route)
        }) {
            Text(
                text = "Edit Profile",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
})}

@Composable
fun TypeScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .graphicsLayer { alpha = 0.5f }
                .offset(x = -128.dp, y = -128.dp)
        )
        TypeScreenView(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun TypeScreenViewPreview() {
    TypeScreen(navController = NavController(LocalContext.current))
}
