@file:JvmName("ProfileKt")

package com.example.shutaffim

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.shutaffim.Model.User
import com.example.shutaffim.ViewModel.AuthViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfile(navController: NavController, authVM: AuthViewModel) {


    val img: Bitmap =
        BitmapFactory.decodeResource(Resources.getSystem(),
            android.R.drawable.ic_menu_report_image)//default image

    var fName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var picture by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }

    val user = authVM.currentUser.observeAsState()
    fName = user.value?.fName ?: ""
    lName = user.value?.lName ?: ""
    about = user.value?.about ?: ""
    picture = user.value?.pictureUrl ?: ""



    //---------------image variables----------------
    var picHasChange by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf(img) }
    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if (it != null) {
            bitmap.value = it
            picHasChange = true
        }
    }

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (Build.VERSION.SDK_INT < 34) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = uri?.let { it1 -> ImageDecoder.createSource(context.contentResolver, it1) }
            bitmap.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }!!
        }
        picHasChange = true

    }
    age =  user.value?.age.toString() ?: "0"
    sex = user.value?.sex ?: ""


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        androidx.compose.material3.Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                    .padding(it)
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    contentAlignment = Alignment.Center
                ) {
                    if (picHasChange) {
                        Image(
                            bitmap = bitmap.value.asImageBitmap(),
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

                                    }
                                )
                        )
                    } else if (picture == "") {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.pngwing_com),
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

                                }
                                )
                        )
                    } else
                    Image(
                       painter = rememberAsyncImagePainter(picture),
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

                            }
                            )
                    )

                    var expanded1 by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .padding(top = 100.dp, start = 170.dp)
                            .background(Color.Black, CircleShape)

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic__baseline_photo_camera),
                            contentDescription = "Add a photo",
                            colorFilter = ColorFilter.tint(color = Color.White),
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(Color.Black, CircleShape)
                                .clickable {
                                    expanded1 = true
                                }
                        )

                        var selectedOption1 by remember { mutableStateOf("") }

                        val options1 = listOf("Camera", "Gallery")
                        DropdownMenu(expanded = expanded1,
                            onDismissRequest = { expanded1 = false }) {
                            options1.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedOption1 = label
                                        expanded1 = false
                                        when (selectedOption1) {
                                            "Camera" -> {
                                                launcherCamera.launch()
                                            }
                                            "Gallery" -> {
                                                launchImage.launch("image/*")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                }

//------------------------------- Card -------------------------
                ElevatedCard {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = fName,
                        onValueChange = { fName = it },
                        label = { Text(text = "First Name : ") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = lName,
                        onValueChange = { lName = it },
                        label = { Text(text = "Last Name : ") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(100.dp),
                        value = about,
                        onValueChange = { about = it },
                        label = {
                            Text(
                                text = "About",
                                style = TextStyle(
                                    fontSize = 18.sp
                                )
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val newUser =
                                User(
                                    user.value?.email!!,
                                    fName,
                                    lName,
                                    about,
                                    pictureName = bitmap.value.toString(),
                                    pictureUrl = user.value?.pictureUrl!!,
                                    user.value?.type!!
                                    age = age.toInt(),
                                    sex = sex
                                )
                            authVM.updateInfo(newUser)
                            authVM.uploadImageToFirebase(bitmap.value, user.value?.email!!, user.value?.pictureName!!)
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 64.dp, end = 64.dp, bottom = 8.dp),
                    ) {
                        Text(text = "Save")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                }

            }
        }

    )}

@Composable
fun EditProfileScreen(navController: NavController,authVM: AuthViewModel ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .graphicsLayer { alpha = 0.5f }
                .offset(x = -128.dp, y = -128.dp)
        )
        EditProfile(navController,  authVM )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenViewPreview() {
    EditProfileScreen(navController = NavController(LocalContext.current), authVM = AuthViewModel())
}
