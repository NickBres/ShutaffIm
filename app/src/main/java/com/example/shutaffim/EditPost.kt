import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.sharp.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.shutaffim.PostItem
import com.google.type.Money

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*

import androidx.compose.material3.Surface

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.io.InputStream
import java.util.Locale
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSecreenView() {

    var city by remember {
        mutableStateOf("")
    }
    var street by remember {
        mutableStateOf("")
    }
    var nuber_house by remember {
        mutableStateOf("")
    }
    var num_partner by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var about_apartment by remember {
        mutableStateOf("")
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Edit Post") }, navigationIcon = {
                IconButton(onClick = { /* TODO Handle back button click here */ }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary
            )
            )
        }, floatingActionButton = {

        }, content = {




            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {


                ElevatedCard {

                    //city
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        value = city,
                        onValueChange = { city = it },
                        label = { Text(text = "city") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    /* street*/
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = street,
                        onValueChange = { street = it },
                        label = { Text(text = "street") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.Home,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    /* nuber_house*/
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = nuber_house,
                        onValueChange = { nuber_house = it },
                        label = { Text(text = "nuber's house") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    //num_partner
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = num_partner,
                        onValueChange = { num_partner = it },
                        label = { Text(text = "number of partner") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Sharp.Face,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    //price
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = price,
                        onValueChange = { price = it.filter { it.isDigit() } }, // Only allow numeric input
                        label = { Text(text = "price") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector  = Icons.Default.Add,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //Information about the apartment
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value =  about_apartment,
                        onValueChange = { about_apartment = it },
                        label = { Text(text = "Information about the apartment") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.Create,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },

                        singleLine = false,  // Allow multiple lines
                        keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,  // Allow any text input
                        imeAction = ImeAction.Done,  // Change to another action if needed

                        ),
                        maxLines = 3
                    )



                }



                Spacer(modifier = Modifier.height(64.dp))

                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "Save",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }



        }//content
    )//Scaffold
}









@Preview(showBackground = true)
@Composable
fun EditSecreenViewPreview() {
    EditSecreenView()
}