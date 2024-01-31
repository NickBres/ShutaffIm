package com.example.shutaffim


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreenView(navController: NavController) {
    var message by remember {
        mutableStateOf("booo")
    }
    var name by remember {
        mutableStateOf("Yair Turgeman")
    }
    var about by remember {
        mutableStateOf("Yair Turgeman")
    }
    var phone_number by remember {
        mutableStateOf("0123546")
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text(text = "Profile View") },
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


                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Optional padding to control the spacing
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Adjust spacing as needed
                    ) {
                        Spacer(modifier = Modifier.padding(50.dp))
                        Image(
                            painter =
                            painterResource
                                (id = R.drawable.connor_jalbert_5b1mb7sdbg0_unsplash),
                            contentDescription = "profile Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp) // Optional padding to control the space between the image and the circle
                                .graphicsLayer(
                                    scaleX = 1f, // Scale factors to maintain the original aspect ratio
                                    scaleY = 1f
                                )
                        )
//                        Image(
//                            painter = imagePainter,
//                            contentDescription = "profile Image",
//                            modifier = Modifier
//                                .size(150.dp)
//                                .clip(CircleShape)
//                                .background(MaterialTheme.colorScheme.background)
//                                .padding(8.dp)
//                                .graphicsLayer(
//                                    scaleX = 1f,
//                                    scaleY = 1f
//                                )
//                        )
                    }//rowScope

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = name,
                        fontSize = 24.sp, // You can adjust the font size as needed
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { /* No-op since it's read-only */ },
                        label = { Text(text = "message") },
                        readOnly = true, // Set to true to make it read-only
                        modifier = Modifier
                            .padding(4.dp)
                            .size(400.dp, 150.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                    )//OutlinedTextField

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = about,
                        onValueChange = { /* No-op since it's read-only */ },
                        label = { Text(text = "about") },
                        readOnly = true, // Set to true to make it read-only
                        modifier = Modifier
                            .padding(4.dp)
                            .size(400.dp, 150.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                    )//OutlinedTextField
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "phone number: " + phone_number,
                        fontSize = 16.sp, // You can adjust the font size as needed
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))







                }






            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun ViewingProfileViewViewPreview() {

    ProfileViewScreenView(navController = NavController(LocalContext.current))
}
