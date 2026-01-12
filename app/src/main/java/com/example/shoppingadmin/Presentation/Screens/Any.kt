//package com.example.shoppingadmin.Presentation.Screens.Product.utils
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import coil3.compose.AsyncImage
//import com.example.shoppingadmin.Domain.Models.ProductModel
//import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
//import org.koin.compose.viewmodel.koinViewModel
//
//@Composable
//fun ProductCart(
//    product: ProductModel,
//    onItemClick : () -> Unit= {},
//    modifier: Modifier = Modifier,
//    viewModel: MyViewModel = koinViewModel<MyViewModel>()
//) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .width(160.dp)  // Fixed width for consistent sizing
//            .clickable { onItemClick() },
//        shape = MaterialTheme.shapes.medium  // Use theme shape
//
//    ) {
//        Column(
//            modifier = Modifier.padding(8.dp)
//
//        ) {
//            //Product Image
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)  // Fixed height for images
//                    .clip(MaterialTheme.shapes.medium)
//            ){
//                AsyncImage(
//                    model = product.image,
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .size(80.dp)
//                        .clip(
//                            RectangleShape
//                        )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            // Product Name
//            Text(
//                text = product.name,
//                style = MaterialTheme.typography.bodyMedium,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(horizontal = 4.dp)
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // Product Price
//            Text(
//                text = "${product.price} Rupees",
//                style = MaterialTheme.typography.bodyLarge.copy(
//                    color = MaterialTheme.colorScheme.primary
//                ),
//                modifier = Modifier.padding(horizontal = 4.dp)
//            )
//
//            // Add any other product details you want to display
//            // For example: ratings, discount badge, etc.
//        }
//
//    }
//
//}