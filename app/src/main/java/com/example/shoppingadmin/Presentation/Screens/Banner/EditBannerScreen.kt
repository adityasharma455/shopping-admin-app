package com.example.shoppingadmin.Presentation.Screens.Banner


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBannerScreen(
    bannerId: String,
    navController: NavController,
    viewModel: MyViewModel = koinViewModel(),
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    val bannerDetailsState by viewModel.bannerDetailsState.collectAsStateWithLifecycle()
    val updateBannerState by viewModel.updateBannerState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Local state for banner name
    var bannerName by remember { mutableStateOf("") }

    LaunchedEffect(bannerId) {
        viewModel.getBannerById(bannerId)
    }

    // Update form field when banner data loads
    LaunchedEffect(bannerDetailsState.isSuccess) {
        bannerDetailsState.isSuccess?.let { banner ->
            bannerName = banner.name ?: ""
        }
    }

    // Handle update success/error
    LaunchedEffect(updateBannerState) {
        when {
            updateBannerState.isSuccess == true -> {
                Toast.makeText(context, "Banner updated successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateBannerState()
                onBackClick()
            }
            updateBannerState.Error != null -> {
                Toast.makeText(context, "Update failed: ${updateBannerState.Error}", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateBannerState()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Banner",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Save button
                    TextButton(
                        onClick = {
                            if (bannerName.isBlank()) {
                                Toast.makeText(context, "Please enter a banner name", Toast.LENGTH_SHORT).show()
                                return@TextButton
                            }

                            val currentBanner = bannerDetailsState.isSuccess
                            if (currentBanner != null) {
                                val updatedBanner = BannerDataModel(
                                    name = bannerName,
                                    imageUrl = currentBanner.imageUrl ?: "",
                                    date = currentBanner.date,
                                    bannerId = currentBanner.bannerId
                                )
                                viewModel.updateBanner(updatedBanner)
                            } else {
                                Toast.makeText(context, "Banner data not loaded", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = bannerName.isNotBlank() && bannerDetailsState.isSuccess != null
                    ) {
                        Text(
                            "Save",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when {
                bannerDetailsState.isLoading -> {
                    Text("Loading banner details...")
                }
                bannerDetailsState.Error != null -> {
                    Text(
                        text = "Error: ${bannerDetailsState.Error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                bannerDetailsState.isSuccess == null -> {
                    Text("Banner not found")
                }
                else -> {
                    // Banner name field
                    OutlinedTextField(
                        value = bannerName,
                        onValueChange = { bannerName = it },
                        label = { Text("Banner Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Show loading when updating
                    if (updateBannerState.isLoading) {
                        Text("Updating banner...")
                    }
                }
            }
        }
    }
}