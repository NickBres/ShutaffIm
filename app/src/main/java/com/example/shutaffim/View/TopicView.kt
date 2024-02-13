package com.example.shutaffim.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Model.Comment
import com.example.shutaffim.Model.Topic
import com.example.shutaffim.Screen
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicView(navController: NavController) {

    val dummyTopic = Topic(
        id = "1",
        title = "Incredible Idea",
        description = "This is an incredible idea that explores various aspects of software development. It's a great place to share your thoughts and learn from others.",
        email = "email1@example.com",
        date = System.currentTimeMillis(),
        userName = "Developer123"
    )

    val dummyComments = listOf(
        Comment(
            id = "1",
            text = "Great topic!",
            email = "email1@example.com",
            date = System.currentTimeMillis(),
            userName = "User1"
        ),
        Comment(
            id = "2",
            text = "I totally agree with you.",
            email = "email2@example.com",
            date = System.currentTimeMillis(),
            userName = "User2"
        ),
        Comment(
            id = "3",
            text = "Interesting perspective.",
            email = "email3@example.com",
            date = System.currentTimeMillis(),
            userName = "User3"
        ),
        Comment(
            id = "4",
            text = "I have a different opinion.",
            email = "email4@example.com",
            date = System.currentTimeMillis(),
            userName = "User4"
        ),
        Comment(
            id = "5",
            text = "Can you elaborate more?",
            email = "email5@example.com",
            date = System.currentTimeMillis(),
            userName = "User5"
        ),
        Comment(
            id = "6",
            text = "This is very insightful.",
            email = "email6@example.com",
            date = System.currentTimeMillis(),
            userName = "User6"
        ),
        Comment(
            id = "7",
            text = "Thanks for sharing this.",
            email = "email7@example.com",
            date = System.currentTimeMillis(),
            userName = "User7"
        ),
        Comment(
            id = "8",
            text = "I learned something new today.",
            email = "email8@example.com",
            date = System.currentTimeMillis(),
            userName = "User8"
        ),
        Comment(
            id = "9",
            text = "This is a controversial topic.",
            email = "email9@example.com",
            date = System.currentTimeMillis(),
            userName = "User9"
        ),
        Comment(
            id = "10",
            text = "Let's keep the discussion going.",
            email = "email10@example.com",
            date = System.currentTimeMillis(),
            userName = "User10"
        )
    )

    val owner = true;

    var newComment by remember { mutableStateOf("") }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = dummyTopic.title) },
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
                    if (owner) {
                        IconButton(onClick = {
                            // delete topic
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete topic"
                            )
                        }
                    }

                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                            .height(108.dp),
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text("Add a comment") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    if (newComment.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = {
                                // add comment
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send"
                            )
                        }
                    }
                }

            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dummyTopic.userName,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
                        Text(
                            text = sdf.format(dummyTopic.date)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = dummyTopic.description,
                        style = TextStyle(
                            fontSize = 14.sp
                        )
                    )
                }
                LazyColumn(
                ) {
                    items(dummyComments) { comment ->
                        CommentItem(comment = comment)
                    }
                }
            }

        }
    )

}

@Composable
fun CommentItem(comment: Comment) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = comment.userName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(comment.date),
                    style = TextStyle(
                        fontSize = 10.sp

                    )
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = comment.text,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopicView() {
    TopicView(navController = NavController(LocalContext.current))
}