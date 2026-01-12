package com.example.shoppingadmin.Presentation.Screens.OrdersScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.shoppingadmin.Domain.Models.OrderDataModel
import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(
    navController: NavController,
    viewModel: MyViewModel= koinViewModel()
) {
    val ordersState by viewModel.getAllOrdersState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateOrderState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle order updates
    LaunchedEffect(updateState) {
        when {
            updateState.isSuccess -> {
                Toast.makeText(context, "Order updated successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateOrderState()
            }
            updateState.Error != null -> {
                Toast.makeText(context, updateState.Error, Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateOrderState()
            }
        }
    }

    // Load all orders
    LaunchedEffect(Unit) {
        viewModel.getAllOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                ordersState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                ordersState.Error != null -> {
                    Text(
                        text = "Error: ${ordersState.Error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                ordersState.isSuccess.isNullOrEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Text("No Orders Yet", fontWeight = FontWeight.Bold)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = ordersState.isSuccess!!.sortedByDescending { it.orderDate },
                            key = { it.orderId }
                        ) { order ->
                            AdminOrderCard(
                                order = order,
                                onAccept = { viewModel.updateOrderStatus(order, "Confirmed") },
                                onCancel = { viewModel.updateOrderStatus(order, "Cancelled") },
                                onDeliver = { viewModel.updateOrderStatus(order, "Delivered") }
                            )
                        }
                    }
                }
            }

            // Updating overlay
            if (updateState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Updating Order...", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrderCard(
    order: OrderDataModel,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDeliver: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Order #${order.orderId.takeLast(6)}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Customer: ${order.customerName}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Phone: ${order.phoneNumber}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                StatusChip(order.orderStatus)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Products preview
            order.products.take(2).forEach { product ->
                ProductPreview(product)
            }

            if (order.products.size > 2) {
                Text(
                    text = "+${order.products.size - 2} more items",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Order info
            Column {
                Text("Address:", fontWeight = FontWeight.Bold)
                Text(
                    order.shippingAddress,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("Payment: ${order.paymentMethod}", color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Subtotal: ₹${order.subtotal}")
                Text("Discount: ₹${order.discount}")
                Text("Delivery Fee: ₹${order.deliveryFee}")
                Text(
                    "Total: ₹${order.totalAmount}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OrderActionButtons(
                status = order.orderStatus,
                onAccept = onAccept,
                onCancel = onCancel,
                onDeliver = onDeliver
            )
        }
    }
}

@Composable
fun ProductPreview(product: ProductModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.image ?: ""),
            contentDescription = product.name,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "₹${product.finalprice}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor) = when (status) {
        "Pending" -> Color(0xFFFFA000) to Color.Black
        "Confirmed" -> Color(0xFF2196F3) to Color.White
        "Delivered" -> Color(0xFF4CAF50) to Color.White
        "Cancelled" -> Color(0xFFF44336) to Color.White
        else -> Color.Gray to Color.White
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(status, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun OrderActionButtons(
    status: String,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDeliver: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (status == "Pending") {
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Text("Confirm") }

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) { Text("Cancel") }
        }

        if (status == "Confirmed") {
            Button(
                onClick = onDeliver,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) { Text("Deliver") }
        }
    }
}
