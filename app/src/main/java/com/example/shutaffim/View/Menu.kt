package com.example.shutaffim

//import com.example.shutaffim.View.Login
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.shutaffim.Model.Picture
import com.example.shutaffim.Model.UserType
import com.example.shutaffim.ViewModel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Menu(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val currUser by authViewModel.currentUser.observeAsState(null)
    authViewModel.updateUser()

    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }

    var pic by remember {
        mutableStateOf(Picture())
    }
    val context = LocalContext.current

    firstName = currUser?.fName ?: ""
    lastName = currUser?.lName ?: ""
    pic = currUser?.picture ?: Picture()



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ShutaffI'm",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        authViewModel.logout(context)
                        navController.navigate(Screen.LoginScreen.route)
                    }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Welcome, $firstName $lastName!",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(50.dp))

                if (pic.pictureUrl == "") {
                    Image(
                        painter = painterResource(id = R.drawable.pngwing_com),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .wrapContentSize()
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .clickable(onClick = {
                                authViewModel.insertIsMyPost(true)
                                navController.navigate(Screen.ProfileScreen.route)
                            }
                            )
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(currUser?.picture?.pictureUrl ?: ""),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .wrapContentSize()
                            .size(200.dp)
                            .shadow(elevation = 10.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .clickable(onClick = {
                                authViewModel.insertIsMyPost(true)
                                navController.navigate(Screen.ProfileScreen.route)
                            }
                            )


                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                if (currUser?.type == UserType.Publisher.type) {
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
                                text = "My Posts",
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
                } else if (currUser?.type == UserType.Consumer.type) {

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
                                text = "Search",
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

                Spacer(modifier = Modifier.height(50.dp))

                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.ForumScreen.route)
                    },
                    icon = {
                        Icon(
                            Icons.Default.MailOutline,
                            "Forum",
                            modifier = Modifier.size(32.dp) // Adjust the size as needed
                        )
                    },
                    text = {
                        Text(
                            text = "Forum",
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
            }//content

        })
}

@Composable
fun MenuScreen(navController: NavController, authViewModel: AuthViewModel) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .align(Alignment.TopCenter)
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = -150.dp, y = -150.dp)
        )
        Menu(navController, authViewModel)
    }
}


