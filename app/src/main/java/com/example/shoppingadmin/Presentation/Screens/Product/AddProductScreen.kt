    package com.example.shoppingadmin.Presentation.Screens.Product

    import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import com.example.shoppingadmin.uriToByteArray
import org.koin.compose.viewmodel.koinViewModel


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddProductScreen(
        navController: NavController,
        viewModel: MyViewModel = koinViewModel(),
        onBackClick: () -> Unit = {navController.popBackStack()}
    ) {
        val getAllCategoryState = viewModel.getCategory.collectAsState()
        val addProductState by viewModel.addProduct.collectAsStateWithLifecycle()
        val addProductPhotoState by viewModel.addProductPhoto.collectAsStateWithLifecycle()
        val context = LocalContext.current

        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var finalPrice by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var availableUnits by remember { mutableStateOf("") }
        var BrandName by remember { mutableStateOf("") }
        var photoUri by remember { mutableStateOf<Uri?>(null) }
        var photoByteArray by remember { mutableStateOf<ByteArray?>(null) }

        // Image picker
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            photoUri = uri
            uri?.let {
                try {
                    photoByteArray = it.uriToByteArray(context)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.getAllCategories()
            val products = getAllCategoryState.value.isSuccess ?: emptyList<CategoryModel>()
            Log.d("UiCategory", "${products}")


        }
        LaunchedEffect(addProductState.isSuccess) {
            if (addProductState.isSuccess!= null) {
                // Show success message
                Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show()
                // Navigate back after success
                onBackClick()
                // Reset the state
                viewModel.resetAddProductState()
            }
        }

        // Handle errors
        LaunchedEffect(addProductState.Error) {
            addProductState.Error?.let { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                viewModel.resetAddProductState()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Add Product",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                )
            }
        ) {innerPadding->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Image Selector
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    imagePicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (photoUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(photoUri),
                                    contentDescription = "Product Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        CustomInputField("Product Name", name) { name = it }
                        CustomInputField("Price", price) { price = it }
                        CustomInputField("Final Price", finalPrice) { finalPrice = it }
                        CustomInputField("Description", description) { description = it }
                        val categoryList = getAllCategoryState.value.isSuccess ?:emptyList<CategoryModel>()

                        CategoryDropdown(
                            categoryList = categoryList,
                            category = category,
                            onCategoryChange = { category = it }
                        )
                        CustomInputField("Available Units", availableUnits) { availableUnits = it }
                        CustomInputField("BrandName", BrandName) { BrandName = it}

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (photoByteArray == null) {
                                    Toast.makeText(
                                        context,
                                        "Please select an image",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                viewModel.addProductPhoto(
                                    byteArray = photoByteArray!!,
                                    onSuccess = { downloadUrl ->
                                        val product = ProductModel(
                                            name = name,
                                            price = price,
                                            finalprice = finalPrice,
                                            description = description,
                                            category = category,
                                            availabelUnits = availableUnits,
                                            image = downloadUrl
                                        )
                                        viewModel.addProduct(product)

                                    },
                                    onError = { error ->
                                        Toast.makeText(
                                            context,
                                            "Image upload failed: $error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )

                                val categoryData = CategoryModel(
                                    name = category
                                )
                                viewModel.addCategory(categoryData)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = name.isNotBlank() && category.isNotBlank() &&
                                    finalPrice.isNotBlank() && price.isNotBlank() &&
                                    description.isNotBlank() && availableUnits.isNotBlank()
                        ) {
                            if (addProductState.isLoading || addProductPhotoState.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text("Add Product", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CustomInputField(label: String, value: String, onValueChange: (String) -> Unit) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CategoryDropdown(
        categoryList: List<CategoryModel>,
        category: String,
        onCategoryChange: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = { onCategoryChange(it) }, // Allows manual typing
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 150.dp) // scroll limit
            ) {
                categoryList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            onCategoryChange(item.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

