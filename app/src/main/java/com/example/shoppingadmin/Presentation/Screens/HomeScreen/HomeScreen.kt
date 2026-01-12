package com.example.shoppingadmin.Presentation.Screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingadmin.Presentation.Navigation.Routes
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MyViewModel = koinViewModel(),
    navController: NavController
) {

    // Real-time order state
    val ordersState by viewModel.getAllOrdersState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getAllOrders() }

    val pendingCount = ordersState.isSuccess?.count { it.orderStatus == "Pending" } ?: 0
    val confirmedCount = ordersState.isSuccess?.count { it.orderStatus == "Confirmed" } ?: 0
    val deliveredCount = ordersState.isSuccess?.count { it.orderStatus == "Delivered" } ?: 0

    val newOrders = pendingCount > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Admin Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    if (newOrders) {
                        Box {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "New Orders",
                                tint = Color.Red,
                                modifier = Modifier.size(28.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        // ---------- SCROLLABLE PAGE (Option 1) ----------
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- TOP BANNER (Option 2) ----------
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "Welcome Admin 👋",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "Manage your orders, track status and update your profile.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // ---------- SUMMARY ROW ----------
            item {
                DashboardSummaryRow(
                    pending = pendingCount,
                    confirmed = confirmedCount,
                    delivered = deliveredCount
                )
            }

            // ---------- FEATURE: Manage Orders ----------
            item {
                FeatureCard(
                    title = "Manage Orders",
                    icon = Icons.Default.ShoppingBag,
                    showBadge = newOrders,
                    onClick = { navController.navigate(Routes.AdminOrdersScreen) }
                )
            }

            // ---------- FEATURE: Profile ----------
            item {
                FeatureCard(
                    title = "Profile",
                    icon = Icons.Default.Person,
                    onClick = { navController.navigate(Routes.AdminProfileScreenRoute) }
                )
            }

            // ---------- OPTIONAL: Add padding at bottom ----------
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}


@Composable
fun DashboardSummaryRow(
    pending: Int,
    confirmed: Int,
    delivered: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCard("Pending", pending, Color(0xFFFFA000))
        SummaryCard("Confirmed", confirmed, Color(0xFF2196F3))
        SummaryCard("Delivered", delivered, Color(0xFF4CAF50))
    }
}

@Composable
fun SummaryCard(title: String, count: Int, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontWeight = FontWeight.SemiBold)
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = color
        )
    }
}

@Composable
fun FeatureCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    showBadge: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )

                if (showBadge) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
