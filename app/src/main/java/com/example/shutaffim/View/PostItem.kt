package com.example.shutaffim

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.shutaffim.Model.Post
import com.example.shutaffim.ViewModel.PostsVM
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(post: Post, navController: NavController, screen: Screen, postsVM: PostsVM) {
    val size = 100.dp

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Date(post.date)
    val dateString = sdf.format(date)

    val imagePainter = if (post.pictures.isNotEmpty()) {
        val pictureURL = post.pictures[0].pictureUrl
        rememberAsyncImagePainter(pictureURL)
    } else {
        painterResource(id = R.drawable.logo_background) // replace with your default image resource id
    }


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
        onClick = {
            postsVM.loadPost(post.id)
            postsVM.viewModelScope.launch {
                postsVM.getInterestedInPost(post.id)
            }
            navController.navigate(Screen.PostScreen.route)
        }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(size)
        ) {
            Image(
                painter = imagePainter,
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
                        text = sdf.format(Date(post.date.toLong()))
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
                        text = "${post.city}, ${post.street}, ${post.house_num}",
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