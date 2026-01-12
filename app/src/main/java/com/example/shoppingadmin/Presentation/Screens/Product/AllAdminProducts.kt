package com.example.shoppingadmin.Presentation.Screens.Product

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Presentation.Navigation.Routes
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import com.example.shoppingapp.Presentation.Screens.Screens.Products.Utils.ProductCart
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAdminProducts(
    navController: NavController,
    viewModel: MyViewModel = koinViewModel<MyViewModel>()
) {
    val productState by viewModel.allAdminProductsState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchProductState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllAdminBrandProducts()
        viewModel.searchQuery() // Start the search query flow
    }

    // Determine which products to display based on search state
    val displayProducts = if (searchQuery.isBlank()) {
        productState.isSuccess ?: emptyList()
    } else {
        searchState.isSuccess ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "All Admin Products",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.AddProductScreenRoutes)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            // Search Section with Cross Icon
            SearchSectionWithCross(
                searchQuery = searchQuery,
                onSearchChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChanged(it)
                },
                onClearSearch = {
                    searchQuery = ""
                    viewModel.clearSearchResults()
                },
                onSearchClick = {
                    if (searchQuery.isNotEmpty()) {
                        viewModel.SearchProduct(searchQuery)
                    }
                },
                isLoading = productState.isLoading || (searchQuery.isNotEmpty() && searchState.isLoading)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Products Count Card (Always visible when products exist)
            if (displayProducts.isNotEmpty()) {
                ProductCountCard(
                    title = if (searchQuery.isNotEmpty()) "Search Results" else "All Products",
                    count = displayProducts.size,
                    searchQuery = searchQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            when {
                productState.isLoading || (searchQuery.isNotEmpty() && searchState.isLoading) -> {
                    AllProductsLoadingState("Loading products...")
                }

                productState.Error != null -> {
                    AllProductsErrorState(
                        error = "Error loading products: ${productState.Error}",
                        onRetry = { viewModel.getAllAdminBrandProducts() }
                    )
                }

                searchState.Error != null -> {
                    AllProductsErrorState(
                        error = "Search error: ${searchState.Error}",
                        onRetry = { if (searchQuery.isNotEmpty()) viewModel.SearchProduct(searchQuery) }
                    )
                }

                else -> {
                    if (displayProducts.isEmpty()) {
                        if (searchQuery.isNotEmpty()) {
                            EmptyStateSection("No products found for '$searchQuery'")
                        } else {
                            EmptyStateSection("No products available")
                        }
                    } else {
                        // Grid of products
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(displayProducts) { product ->
                                ProductCart(
                                    product = product,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp),
                                    onItemClick = {
                                        navController.navigate(Routes.EachProductItemScreenRoutes(product.productId))
                                        Log.d("AllProduct", "${product.productId}")
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSectionWithCross(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSearchClick: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            // Search row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    label = { Text("Search products") },
                    placeholder = { Text("Search by name..") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.clickable { onSearchClick() }
                        )
                    },
                    trailingIcon = {
                        Row {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = onClearSearch) {
                                    Icon(Icons.Default.Close, "Clear search")
                                }
                            }
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            
        }
    }
}

@Composable
fun ProductCountCard(
    title: String,
    count: Int,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$count products found" +
                        if (searchQuery.isNotEmpty()) " for '$searchQuery'" else "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// --- Shared UI States --- (keep your existing functions)
@Composable
fun AllProductsLoadingState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Text(
                text = message,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun AllProductsErrorState(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun EmptyStateSection(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}