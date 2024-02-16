package com.example.shutaffim.Model.Screen


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shutaffim.Model.Picture
import com.example.shutaffim.Model.UserType
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    var fName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var about by remember { mutableStateOf("") }
    var birthYear by remember {
        mutableStateOf("")
    }


    val sexOptions = listOf("Male", "Female", "Other")
    var selectedSexOption by remember { mutableStateOf("Select Sex") }
    var sexExpanded by remember { mutableStateOf(false) }


    // ----------------- Image Picker -----------------
    val img: Bitmap =
        BitmapFactory.decodeResource(
            Resources.getSystem(),
            android.R.drawable.ic_menu_report_image
        )//default image
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf(img) }
    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if (it != null) {
            bitmap.value = it

        }
    }

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = uri?.let { it1 -> ImageDecoder.createSource(context.contentResolver, it1) }
            bitmap.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }!!
        }

    }


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        // Update the selectedImageUri when an image is selected
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //------------------- Card -------------------

        ElevatedCard {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center, // Align items in the center horizontally

                    verticalAlignment = Alignment.CenterVertically
                ) {

                        // Replace Image with IconButton
                        IconButton(
                            onClick = {
                                activityResultLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .size(130.dp)
                                .border(
                                    BorderStroke(8.dp, MaterialTheme.colorScheme.primary),
                                    CircleShape
                                )
                                .padding(5.dp)
                                .clip(CircleShape)
                        ) {
                            if (selectedImageUri == null) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Upload Image"
                            )
                            }
                            selectedImageUri?.let { uri ->
                                Image(
                                    painter = rememberImagePainter(uri),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(150.dp)
                                        .border(
                                            BorderStroke(5.dp, Color.White),
                                            CircleShape
                                        )
                                        .padding(5.dp)
                                        .clip(CircleShape)

                                )

                            }
                        }

                    }

           /** fname   * */
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = fName,
                onValueChange = { fName = it },
                label = { Text(text = "First Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            /** lname   * */
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = lName,
                onValueChange = { lName = it },
                label = { Text(text = "Last Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = birthYear,
                onValueChange = {
                    birthYear = it
                }, // Only allow numeric input
                label = { Text(text = "Birth Year") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                )
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
//                    .height(90.dp),
                value = about,
                onValueChange = { about = it },
                label = {
                    Text(
                        text = "About",
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        // You can add an image resource for the visibility icon here if needed
                        // Image(painter = painterResource(id = R.drawable.your_visibility_icon), contentDescription = null)
                        // or simply remove the IconButton
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                expanded = sexExpanded,
                onExpandedChange = { sexExpanded = it },
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    readOnly = true,
                    value = selectedSexOption,
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexExpanded) },
                    label = { Text(text = "sex") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary,)
                )
                ExposedDropdownMenu(
                    expanded = sexExpanded,
                    onDismissRequest = { sexExpanded = false },
                ) {
                    sexOptions.forEach { sexOption ->
                        DropdownMenuItem(
                            text = { Text(sexOption) },
                            onClick = {
                                selectedSexOption = sexOption
                                sexExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
//***********************************************************************
            val options = listOf(UserType.Consumer.type, UserType.Publisher.type)
            var userType by remember { mutableStateOf("Select User Type") }
            var expanded by remember { mutableStateOf(false) }
// We want to react on tap/press on TextField to show menu
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(end = 8.dp),

                    readOnly = true,
                    value = userType,
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    label = { Text(text = "user type") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                userType = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    authViewModel.signUp(
                        navController = navController,
                        email = email,
                        password = password,
                        firstName = fName,
                        lastName = lName,
                        about = about,
                        type = userType,
                        pictureName = bitmap.value.toString(),
                        picture = Picture(), // TODO : add picture
                        bitmap = bitmap.value,
                        birthYear = birthYear.toInt(),
                        sex = selectedSexOption
                    )


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = fName.isNotBlank() &&
                        lName.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        selectedSexOption != "Select Sex" &&
                        userType != "Select User Type" &&
                        birthYear.isNotEmpty() &&
                        birthYear.toInt() <= LocalDate.now().year &&
                        birthYear.toInt() >= 1920
            ) {
                Text(text = "Register")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Already have an account?")
        TextButton(onClick = {
            navController.navigate(Screen.LoginScreen.route)
        }) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

