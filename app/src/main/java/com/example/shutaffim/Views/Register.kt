package com.example.shutaffim

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.shutaffim.ui.theme.ShutaffImTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController) {
    var fName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(2.dp))

        Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(2.dp))

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

            Spacer(modifier = Modifier.height(2.dp))


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

            Spacer(modifier = Modifier.height(2.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

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


            Spacer(modifier = Modifier.height(2.dp))

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

            Button(
                onClick = { /* TODO: Handle registration logic */
                          navController.navigate(Screen.LoginScreen.route)
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = fName.isNotBlank() &&lName.isNotBlank() && email.isNotBlank() && password.isNotBlank()&& phoneNumber.isNotBlank() && about.isNotBlank()
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
fun RegisterScreen(navController: NavController) {
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
        RegisterView(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    RegisterScreen(navController = NavController(LocalContext.current))
}
