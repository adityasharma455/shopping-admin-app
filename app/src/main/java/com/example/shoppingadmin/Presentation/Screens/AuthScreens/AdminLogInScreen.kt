package com.example.shoppingadmin.Presentation.Screens.AuthScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun AdminLogInScreen(
    navController: NavController,
    viewModel: MyViewModel = koinViewModel<MyViewModel>()

){

    var Email by remember { mutableStateOf("") }
    var Password by remember { mutableStateOf("") }
    var context = LocalContext.current

    val authState by viewModel.authScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = authState) {
        when(authState){
            is AuthScreenState.LoginSuccess -> {
                navController.navigate(Routes.HomeScreenRoutes){
                    popUpTo(Routes.AdminLogInScreenRoutes) { inclusive = true }
                }
            }
            is AuthScreenState.Error -> {
                Toast.makeText(context, "UserName or password is incorrect", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🌟 UPDATED ATTRACTIVE LOGIN HEADING 🌟
        Text(
            text = "Admin Login",
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = Email,
            onValueChange = { Email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = Password,
            onValueChange = { Password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    val data = AdminDataModel(
                        email = Email.trim(),
                        password = Password.trim()
                    )
                    try {
                        viewModel.logInAdmin(data)
                    } catch (e: Exception){
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = Email.isNotBlank() && Password.isNotBlank()
            ){
                Text("Log In")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            TextButton(onClick = {
                navController.navigate(Routes.AdminSignUpScreenRoutes)
            }) {
                Text(text = "Don't have an account? Sign Up")
            }
        }
    }
}
