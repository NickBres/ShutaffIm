package com.example.shutaffim.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Model.Topic
import com.example.shutaffim.R
import com.example.shutaffim.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forum(navController: NavController) {

    val dummyTopics = listOf(
        Topic(
            id = "1",
            title = "Incredible Idea",
            description = "",
            email = "email1@example.com",
            date = System.currentTimeMillis(),
            userName = "Developer123"
        ),
        Topic(
            id = "2",
            title = "Fantastic Discussion",
            description = "",
            email = "email2@example.com",
            date = System.currentTimeMillis(),
            userName = "Coder456"
        ),
        Topic(
            id = "3",
            title = "Amazing Topic",
            description = "",
            email = "email3@example.com",
            date = System.currentTimeMillis(),
            userName = "Programmer789"
        ),
        Topic(
            id = "4",
            title = "Prodigious Conversation",
            description = "",
            email = "email4@example.com",
            date = System.currentTimeMillis(),
            userName = "Engineer321"
        ),
        Topic(
            id = "5",
            title = "Magnificent Debate",
            description = "",
            email = "email5@example.com",
            date = System.currentTimeMillis(),
            userName = "User654"
        ),
        Topic(
            id = "6",
            title = "Astounding Thought",
            description = "",
            email = "email6@example.com",
            date = System.currentTimeMillis(),
            userName = "Developer987"
        ),
        Topic(
            id = "7",
            title = "Remarkable Subject",
            description = "",
            email = "email7@example.com",
            date = System.currentTimeMillis(),
            userName = "Coder231"
        ),
        Topic(
            id = "8",
            title = "Impressive Theme",
            description = "",
            email = "email8@example.com",
            date = System.currentTimeMillis(),
            userName = "Programmer564"
        ),
        Topic(
            id = "9",
            title = "Extraordinary Matter",
            description = "",
            email = "email9@example.com",
            date = System.currentTimeMillis(),
            userName = "Engineer897"
        ),
        Topic(
            id = "10",
            title = "Spectacular Point",
            description = "",
            email = "email10@example.com",
            date = System.currentTimeMillis(),
            userName = "User345"
        )
    )

    var sortMenu by remember { mutableStateOf(false) }
    val sortOptions = listOf("Newest", "Oldest", "Most Popular", "Mine")
    var selectedOption by remember { mutableStateOf("Newest") }

    var createWindow by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Forum") },
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
                    Box() {
                        IconButton(onClick = {
                            sortMenu = true
                        }) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Sort"
                            )
                        }
                        DropdownMenu(expanded = sortMenu,
                            onDismissRequest = { sortMenu = false }) {
                            sortOptions.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedOption = label
                                        sortMenu = false
                                    }
                                )
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createWindow = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Search And Filter")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                items(dummyTopics) { topic ->
                    ForumItem(topic)
                }
            }
        }
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    if (createWindow) {
        ModalBottomSheet(
            onDismissRequest = { createWindow = false },
            content = {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    text = "Create New Topic",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .fillMaxWidth(),
                    value = title,
                    label = { Text(text = "Title") },
                    onValueChange = { title = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Box {

                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(256.dp),
                            value = description,
                            onValueChange = { description = it },
                            label = { Text(text = "Description") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }

                    FloatingActionButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .align(androidx.compose.ui.Alignment.BottomEnd)
                            .offset(y = 32.dp),
                        onClick = {
                            if (title.isNotEmpty() && description.isNotEmpty()) {
                                createWindow = false
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
                Spacer(modifier = Modifier.height(128.dp))

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumItem(topic: Topic) {

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {

        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(64.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = topic.title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Text(
                        text = sdf.format(Date(topic.date)),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
                    )
                }
                Text(
                    text = "By: ${topic.userName}"
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun ForumPreview() {
    Forum(navController = NavController(LocalContext.current))
}