package com.example.shutaffim.View

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.Model.UserType
import com.example.shutaffim.R
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Register(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    var fName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    //var phoneNumber by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    //var type by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        ElevatedCard {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = fName,
                onValueChange = { fName = it },
                label = { Text(text = "First Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )



            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    .padding(16.dp),
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


//
//            OutlinedTextField(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                value = phoneNumber,
//                onValueChange = { phoneNumber = it },
//                label = { Text(text = "Phone Number") },
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = MaterialTheme.colorScheme.primary,
//                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
//                    cursorColor = MaterialTheme.colorScheme.primary
//                ),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Phone,
//                )
//            )



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



            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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

            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf(UserType.Consumer.type) }

            val options = listOf(UserType.Consumer.type, UserType.Publisher.type)

            Box(modifier = Modifier
                .wrapContentSize()
                .padding(start = 16.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                .width(80.dp)
                .height(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(selectedOption, modifier = Modifier.clickable { expanded = true })
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    options.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedOption = label
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    authViewModel.signUp(
                        navController = navController,
                        email = email,
                        password = password,
                        firstName = fName,
                        lastName = lName,
                        about = about,
                        type = selectedOption
                    )


                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = fName.isNotBlank() &&lName.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            ) {
                Text(text = "Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
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
        Register(navController,authViewModel = authViewModel )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    val navController = rememberNavController()
    val  authViewModel = AuthViewModel()

    RegisterScreen(navController = navController,
        authViewModel = authViewModel)
}
