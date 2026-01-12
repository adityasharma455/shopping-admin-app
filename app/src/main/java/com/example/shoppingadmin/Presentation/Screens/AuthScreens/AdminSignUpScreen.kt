package com.example.shoppingadmin.Presentation.Screens.AuthScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Domain.Models.AdminDataModel
import com.example.shoppingadmin.Presentation.Navigation.Routes
import com.example.shoppingadmin.Presentation.ViewModel.AuthScreenState
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminSignUpScreen(
    navController: NavController,
    viewModel: MyViewModel = koinViewModel()
) {

    var Email by remember { mutableStateOf("") }
    var Password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    // new fields
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var BrandName by remember { mutableStateOf("") }
    var gstNumber by remember { mutableStateOf("") }

    val context = LocalContext.current

    val authState by viewModel.authScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthScreenState.RegistrationSuccess -> {
                navController.navigate(Routes.HomeScreenRoutes) {
                    popUpTo(Routes.AdminSignUpScreenRoutes) { inclusive = true }
                }
                Toast.makeText(context, "Sign up successful!", Toast.LENGTH_LONG).show()
            }

            is AuthScreenState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthScreenState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            // ************** HEADER NAME HERE ******************
            Text(
                text = "Admin Sign Up",
                modifier = Modifier.padding(bottom = 24.dp),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = Email,
                onValueChange = { Email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = Password,
                onValueChange = { Password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = BrandName,
                onValueChange = { BrandName = it },
                label = { Text("Brand Name") }, // FIXED LABEL
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = gstNumber,
                onValueChange = { gstNumber = it },
                label = { Text("GST Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val data = AdminDataModel(
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        email = Email.trim(),
                        password = Password.trim(),
                        phoneNumber = phoneNumber.trim(),
                        address = address.trim(),
                        BrandName = BrandName.trim(),
                        gstNumber = gstNumber.trim()
                    )

                    if (Password == confirmPassword.trim()) {
                        viewModel.createAdmin(data)
                    } else {
                        Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        Email.isNotBlank() &&
                        Password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        address.isNotBlank() &&
                        BrandName.isNotBlank() &&
                        gstNumber.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = { navController.navigate(Routes.AdminLogInScreenRoutes) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Already have an account? LogIn")
            }
        }
    }
}
