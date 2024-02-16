package com.example.shutaffim.View

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.shutaffim.R
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ui.theme.surface


@OptIn(ExperimentalFoundationApi::class)
@Composable

private fun ShowPic(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageHeight: Dp = 200.dp,
    imageWidth: Dp = 200.dp,
) {


    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = modifier
                .height(imageHeight)
                .width(imageWidth)
                .clip(MaterialTheme.shapes.medium) // Apply circular shape
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .scale(Scale.FILL)
                    .data(imageUrl)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.pngwing_com),
                error = painterResource(id = R.drawable.pngwing_com),
                modifier = modifier
                    .height(imageHeight)
                    .width(imageWidth)

            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController, authVM: AuthViewModel) {


    val pic = remember {

        "https://www.gstatic.com/webp/gallery/5.webp"
    }

    val user by authVM.currentUser.observeAsState()
    val userintersted by authVM.currentintersted.observeAsState()

    var EditClick by remember {
        mutableStateOf(false)
    }
    var FirstName by remember {
        mutableStateOf("")
    }
    var LastName by remember {
        mutableStateOf("")
    }
    var Email by remember {
        mutableStateOf("")
    }
    var about_user by remember {
        mutableStateOf("")
    }
    if (authVM.isMyPost.value == true){
        FirstName = user?.fName ?: ""
        LastName = user?.lName ?: ""
        Email = user?.email ?: ""
        about_user = user?.about ?: ""

    }else{
        FirstName = userintersted?.fName ?: ""
        LastName = userintersted?.lName ?: ""
        Email = userintersted?.email ?: ""
        about_user = userintersted?.about ?: ""

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = {
            TopAppBar(

                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.TypeScreen.route)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    if (authVM.isMyPost.value == true) {
                        IconButton(onClick = { navController.navigate(Screen.EditProfileScreen.route) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(innerPadding)


        ) {

            ShowPic(imageUrl = pic)//in rows
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier
                    .size(height = 60.dp, width = 380.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                ) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart

                ) {

                    Text(
                        text = "First Name:  $FirstName",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .size(height = 60.dp, width = 380.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                ) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart

                ) {

                    Text(
                        text = "Last Name:  $LastName",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .size(height = 60.dp, width = 380.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                ) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart

                ) {

                    Text(
                        text = "Email:  $Email",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 0.dp, end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About Me:",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Card(
                modifier = Modifier
                    .size(height = 150.dp, width = 400.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = about_user,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    Profile(navController = NavController(LocalContext.current), authVM = AuthViewModel())

}

