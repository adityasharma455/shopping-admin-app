package com.example.shoppingadmin.Presentation.Screens.ProfileScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Presentation.Navigation.Routes
import com.example.shoppingadmin.Presentation.ViewModel.AuthScreenState
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    navController: NavController,
    viewModel: MyViewModel = koinViewModel(),
) {
    var isEditing by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val getAdminState by viewModel.getCurrentAdminState.collectAsStateWithLifecycle()
    val updateAdminState by viewModel.updateAdminState.collectAsStateWithLifecycle()
    val authState by viewModel.authScreenState.collectAsStateWithLifecycle()

    var adminData by remember(getAdminState) {
        mutableStateOf(getAdminState.isSuccess)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showSignOutDialog by remember { mutableStateOf(false) }

    // Load admin once
    LaunchedEffect(Unit) {
        viewModel.getCurrentAdmin()
    }

    // Handle auth state change
    LaunchedEffect(authState) {
        when (authState) {
            is AuthScreenState.SignedOut -> {
                navController.navigate(Routes.AdminLogInScreenRoutes) {
                    popUpTo(Routes.HomeScreenRoutes) { inclusive = true }
                }
            }
            is AuthScreenState.Error -> {
                Toast.makeText(context, (authState as AuthScreenState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    // Update admin state
    LaunchedEffect(updateAdminState) {
        when {
            updateAdminState.Error != null -> {
                Toast.makeText(context, updateAdminState.Error, Toast.LENGTH_SHORT).show()
            }

            updateAdminState.isSuccess != null -> {
                Toast.makeText(context, updateAdminState.isSuccess, Toast.LENGTH_SHORT).show()

                viewModel.getCurrentAdmin()
                isEditing = false
            }
        }
    }

    // Loading UI
    if (adminData == null && getAdminState.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Error
    if (adminData == null && getAdminState.Error != null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error: ${getAdminState.Error}")
        }
        return
    }

    // MAIN UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Profile", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { showSignOutDialog = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Text("Sign Out")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEditing) {
                        adminData?.let { data ->
                            scope.launch(Dispatchers.IO) { viewModel.updateCurrentAdmin(data) }
                        }
                    }
                    isEditing = !isEditing
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(if (isEditing) "Save" else "Edit")
            }
        }
    ) { pad ->

        if (showSignOutDialog) {
            SignOutDialog(
                onConfirm = {
                    viewModel.AdminSignOut()
                    showSignOutDialog = false
                },
                onDismiss = { showSignOutDialog = false }
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // PROFILE PICTURE
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    adminData?.firstName?.firstOrNull()?.uppercase() ?: "",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            Spacer(Modifier.height(20.dp))

            //--------------------------------------------
            // PERSONAL INFORMATION
            //--------------------------------------------
            SectionTitle("Personal Information")

            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween) {
                fieldBox(
                    value = adminData?.firstName ?: "",
                    label = "First Name",
                    readOnly = !isEditing,
                    onValueChange = { adminData = adminData?.copy(firstName = it) }
                )

                fieldBox(
                    value = adminData?.lastName ?: "",
                    label = "Last Name",
                    readOnly = !isEditing,
                    onValueChange = { adminData = adminData?.copy(lastName = it) }
                )
            }

            //--------------------------------------------
            // CONTACT INFORMATION
            //--------------------------------------------
            SectionTitle("Contact Information")

            fieldBox(
                value = adminData?.email ?: "",
                label = "Email",
                leading = { Icon(Icons.Default.Email, null) },
                readOnly = !isEditing,
                onValueChange = { adminData = adminData?.copy(email = it) }
            )

            fieldBox(
                value = adminData?.phoneNumber ?: "",
                label = "Phone Number",
                leading = { Icon(Icons.Default.Phone, null) },
                readOnly = !isEditing,
                onValueChange = { adminData = adminData?.copy(phoneNumber = it) }
            )

            fieldBox(
                value = adminData?.address ?: "",
                label = "Address",
                leading = { Icon(Icons.Default.Home, null) },
                readOnly = !isEditing,
                onValueChange = { adminData = adminData?.copy(address = it) }
            )

            //--------------------------------------------
            // BUSINESS INFORMATION
            //--------------------------------------------
            SectionTitle("Business Information")

            fieldBox(
                value = adminData?.BrandName ?: "",
                label = "Brand Name",
                readOnly = !isEditing,
                onValueChange = { adminData = adminData?.copy(BrandName = it) }
            )

            fieldBox(
                value = adminData?.gstNumber ?: "",
                label = "GST Number",
                readOnly = !isEditing,
                onValueChange = { adminData = adminData?.copy(gstNumber = it) }
            )

            //--------------------------------------------
            // PASSWORD (ONLY IF EDITING)
            //--------------------------------------------
            if (isEditing) {
                SectionTitle("Password")

                OutlinedTextField(
                    value = adminData?.password ?: "",
                    onValueChange = { adminData = adminData?.copy(password = it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation =
                        if (isPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    }
                )
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun fieldBox(
    value: String,
    label: String,
    readOnly: Boolean,
    onValueChange: (String) -> Unit,
    leading: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (!readOnly) onValueChange(it) },
        label = { Text(label) },
        readOnly = readOnly,
        leadingIcon = leading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

@Composable
fun SignOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Sign Out") },
        text = { Text("Are you sure you want to sign out?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )) {
                Text("Sign Out")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
