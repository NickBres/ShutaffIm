package com.example.shutaffim.View

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forum(navController: NavController) {

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

@Preview(showBackground = true)
@Composable
fun ForumPreview() {
    Forum(navController = NavController(context = androidx.compose.ui.platform.LocalContext.current))
}