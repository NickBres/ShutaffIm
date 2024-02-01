package com.example.shutaffim.Screens

import android.provider.ContactsContract.CommonDataKinds.Email
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.shutaffim.R
import com.example.shutaffim.ui.theme.surface


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showPic(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val pic = remember {

        "https://www.gstatic.com/webp/gallery/5.webp"
    }

    val fName = "Moshe"
    val lName = "nahshon"
    val Email = "ofdihbfi@gmail.com"
    val phoneNumber = "054-1234567"
    val hasFurniture: () -> String = {
        val isFurnished = true // Replace with your actual condition
        if (isFurnished) {
            "Furnished"
        } else {
            "Unfurnished"
        }
    }
    val hasInternet: () -> String = {
        val isFurnished = true // Replace with your actual condition
        if (isFurnished) {
            "Wifi"
        } else {
            "No Wifi"
        }
    }
    val About =
        "Lorem ipsum dolor sit amet, consectetur , nisl nisl aliquet nisl, nec aliquam nisl" +
                "Lorem ipsum dolor sit amet, consectetur aliquam nisl "
    val location = "Tel-Aviv, Dizengoff, 42"
    var EditClick by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {EditClick = !EditClick}) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        if (EditClick) {
                            ModalBottomSheet(onDismissRequest = { EditClick = false },
                                content = {

                                }
                            )
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

            showPic(imageUrl = pic)//in rows
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = surface,// Set your desired background color
                        shape = RoundedCornerShape(8.dp) // Set your desired corner radius
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "First Name:  " + fName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                             // Set your desired text color
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = surface,// Set your desired background color
                        shape = RoundedCornerShape(8.dp) // Set your desired corner radius
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Last Name:  " + lName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = surface,// Set your desired background color
                        shape = RoundedCornerShape(8.dp) // Set your desired corner radius
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Email:  " + Email,
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
                Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = surface,// Set your desired background color
                        shape = RoundedCornerShape(8.dp) // Set your desired corner radius
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Phone Number:  " + phoneNumber,

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
                BasicTextField(
                    value = About,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
   ProfileScreen()

}

