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

// for test only
data class Post(
    val image: Int,
    val price: Int,
    val location: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(post: Post) {
    val size = 100.dp

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
        onClick = { /* TODO Enter to the item screen*/ }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(size)
        ) {
            Image(
                painter = painterResource(id = post.image),
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
                        text = post.date
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
                        text = post.location,
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