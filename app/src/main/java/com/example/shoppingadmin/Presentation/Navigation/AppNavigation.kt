package com.example.shoppingadmin.Presentation.Navigation

import AllCategoryScreen
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.shoppingadmin.Presentation.Screens.AuthScreens.AdminLogInScreen
import com.example.shoppingadmin.Presentation.Screens.AuthScreens.AdminSignUpScreen
import com.example.shoppingadmin.Presentation.Screens.Banner.AddBannerModel
import com.example.shoppingadmin.Presentation.Screens.Banner.EditBannerScreen
import com.example.shoppingadmin.Presentation.Screens.Banner.ManageBannersScreen
import com.example.shoppingadmin.Presentation.Screens.Category.AddCategoryScreen
import com.example.shoppingadmin.Presentation.Screens.Category.AllProductsByCategoryScreen
import com.example.shoppingadmin.Presentation.Screens.HomeScreen.HomeScreen
import com.example.shoppingadmin.Presentation.Screens.OrdersScreen.AdminOrdersScreen
import com.example.shoppingadmin.Presentation.Screens.Product.AddProductScreen
import com.example.shoppingadmin.Presentation.Screens.Product.AllAdminProducts
import com.example.shoppingadmin.Presentation.Screens.Product.EachProductItem
import com.example.shoppingadmin.Presentation.Screens.Product.EditProductScreen
import com.example.shoppingadmin.Presentation.Screens.ProfileScreen.AdminProfileScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    // Define all auth screen routes
    val authRoutes = setOf(
        Routes.AdminLogInScreenRoutes,
        Routes.AdminSignUpScreenRoutes
    )

    // Check if we should show bottom bar
    val showBottomBar = currentRoute != null &&
            !authRoutes.contains(currentRoute) &&
            firebaseAuth.currentUser != null

    var startScreen = if (firebaseAuth.currentUser == null){
        SubNavigation.LogInSignUpScreenRoutes
    }else{
        SubNavigation.HomeScreenRoutes
    }

    val BottomNavItem = listOf<BottomItem>(
        BottomItem(
            title = "Home",
            icon = Icons.Default.Home
        ),
        BottomItem(
            title = "Add Product",
            icon = Icons.Default.ProductionQuantityLimits
        ),
        BottomItem(
            title = "Add Category",
            icon = Icons.Default.Category
        ),
        BottomItem(
            title = "Add Banner",
            icon = Icons.Default.Add
        )

    )

    Scaffold(
modifier = Modifier.fillMaxSize(),
        bottomBar = {
           if (showBottomBar){
               AnimatedBottomBar(
                   modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
                   selectedItem = selectedItem,
                   itemSize = BottomNavItem.size,
                   containerColor = Color.Transparent,
                   indicatorStyle = IndicatorStyle.FILLED,
                   indicatorColor = Color.Red,
                   indicatorDirection = IndicatorDirection.BOTTOM
               ){
                   BottomNavItem.fastForEachIndexed { index, item ->
                       BottomBarItem(
                           selected = selectedItem == index,
                           onClick = {
                               selectedItem = index
                               when (index) {
                                   0-> {
                                       navController.navigate(Routes.HomeScreenRoutes) {
                                           popUpTo(SubNavigation.ProductScreenRoutes) {
                                               inclusive = true
                                           }
                                       }
                                   }
                                   1 ->{
                                       navController.navigate(Routes.AllAdminProductsRoute)
                                   }
                                   2->{
                                       navController.navigate((Routes.AllCategoryScreenRoutes))
                                   }
                                   3 ->{
                                       navController.navigate((Routes.ManageBannersScreenRoutes))
                                   }
                               }
                           },
                           modifier = Modifier,
                           imageVector = item.icon,
                           label = item.title,
                           containerColor = Color.Transparent
                       )
                   }

               }
           }

        }
    ) {innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController, startDestination = startScreen
            ){

            navigation<SubNavigation.LogInSignUpScreenRoutes>(
                startDestination = Routes.AdminLogInScreenRoutes
            ) {
                composable<Routes.AdminLogInScreenRoutes> {
                    AdminLogInScreen(navController = navController)
                }
                composable<Routes.AdminSignUpScreenRoutes> {
                    AdminSignUpScreen(navController = navController)
                }
            }

                navigation<SubNavigation.HomeScreenRoutes>(
                    startDestination = Routes.HomeScreenRoutes
                ) {
                    composable<Routes.HomeScreenRoutes> {
                        HomeScreen(
                            navController = navController
                        )
                    }
                }

                navigation<SubNavigation.ProfileScreenRoutes>(
                    startDestination = Routes.AdminProfileScreenRoute
                ){
                    composable<Routes.AdminProfileScreenRoute> {
                        AdminProfileScreen(navController = navController)
                    }
                }
                navigation<SubNavigation.OrdersScreenRoutes>(
                    startDestination = Routes.AdminOrdersScreen
                ){
                    composable<Routes.AdminOrdersScreen> {
                        AdminOrdersScreen(navController)
                    }
                }

                navigation<SubNavigation.BannerScreenRoutes>(
                    startDestination = Routes.ManageBannersScreenRoutes
                ) {
                    composable<Routes.ManageBannersScreenRoutes> {
                        ManageBannersScreen(navController)
                    }
                    composable<Routes.EditBannerScreenRoutes> {
                        val data = it.toRoute<Routes.EditBannerScreenRoutes>()
                        EditBannerScreen(
                            navController = navController,
                            bannerId = data.bannerId)

                    }
                }

                navigation<SubNavigation.CategoryScreenRoutes>(startDestination = Routes.AllCategoryScreenRoutes) {
                    composable<Routes.AllCategoryScreenRoutes> {
                        AllCategoryScreen(navController = navController)
                    }
                    composable<Routes.AllProductsByCategoryRoutes> {
                        val data = it.toRoute<Routes.AllProductsByCategoryRoutes>()
                        AllProductsByCategoryScreen(
                            navController = navController,
                            CategoryName = data.CategoryName
                        )
                    }
                }

                navigation<SubNavigation.ProductScreenRoutes>(
                    startDestination = Routes.AllAdminProductsRoute
                ) {

                    composable<Routes.AddProductScreenRoutes> {
                        AddProductScreen(navController)
                    }
                    composable<Routes.AllAdminProductsRoute> {
                        AllAdminProducts(navController)
                    }
                    composable<Routes.AddCategoryScreenRoutes> {
                        AddCategoryScreen(navController = navController)
                    }
                    composable<Routes.AddBannerModelScreenRoutes> {
                        AddBannerModel(navController = navController)
                    }
                    composable<Routes.EachProductItemScreenRoutes> {
                        val data =it.toRoute<Routes.EachProductItemScreenRoutes>()
                        Log.d("AppNavi", "${data}")

                        EachProductItem(
                            productID = data.ProductID,
                            navController = navController
                        )
                    }
                    composable<Routes.EditProductScreenRoutes> {
                        val data = it.toRoute<Routes.EditProductScreenRoutes>()
                        EditProductScreen(
                            productID = data.productId,
                            navController = navController
                        )
                    }
                }
            }

        }
    }
}
data class BottomItem(
    val title: String,
    val icon: ImageVector
)