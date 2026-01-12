package com.example.shoppingadmin.Presentation.Screens.Category

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddCategoryScreen(
    navController: NavController
) {
    var CategoryName by remember { mutableStateOf("") }

    val viewModel: MyViewModel = koinViewModel()
    val addCategoryState = viewModel.addCategory.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle success state with LaunchedEffect
    LaunchedEffect(addCategoryState) {
        when {
            addCategoryState.value.isSuccess != null -> {
                Toast.makeText(context, addCategoryState.value.isSuccess, Toast.LENGTH_SHORT).show()
                //to reset the viewModel state to its intial value
                viewModel.resetAddCategoryState()
                //now its for reset ui state
                CategoryName = ""
            }

            addCategoryState.value.Error != null -> {
                Toast.makeText(context, addCategoryState.value.Error, Toast.LENGTH_SHORT).show()
                //now reset the viewModel state
                viewModel.resetAddCategoryState()

            }


        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (addCategoryState.value.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            OutlinedTextField(
                value = CategoryName,
                onValueChange = {
                    CategoryName = it
                },
                label = { Text("CategoryName") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val data = CategoryModel(
                        name = CategoryName
                    )
                    viewModel.addCategory(data)

                }, enabled = CategoryName.isNotBlank()
            ) {
                Text("Add to Category")
            }
        }
    }
}
