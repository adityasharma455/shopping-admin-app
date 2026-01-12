package com.example.shoppingadmin.Presentation.Navigation

import kotlinx.serialization.Serializable


sealed class SubNavigation{

    @Serializable
    object LogInSignUpScreenRoutes: SubNavigation()

    @Serializable
    object ProductScreenRoutes : SubNavigation()

    @Serializable
    object CategoryScreenRoutes: SubNavigation()

    @Serializable
    object ProfileScreenRoutes : SubNavigation()

    @Serializable
    object HomeScreenRoutes: SubNavigation()

    @Serializable
    object BannerScreenRoutes: SubNavigation()

    @Serializable
    object OrdersScreenRoutes: SubNavigation()
}

sealed class Routes{

    @Serializable
    object HomeScreenRoutes

    @Serializable
    object AddProductScreenRoutes

    @Serializable
    object AddCategoryScreenRoutes

    @Serializable
    object AddBannerModelScreenRoutes

    @Serializable
    object AdminLogInScreenRoutes

    @Serializable
    object AdminOrdersScreen

    @Serializable
    object AdminSignUpScreenRoutes

    @Serializable
    object AllAdminProductsRoute

    @Serializable
    object AdminProfileScreenRoute
    @Serializable
    data class EachProductItemScreenRoutes(
        val ProductID: String
    )

    @Serializable
    data class EditProductScreenRoutes(
        val productId: String
    )

    @Serializable
    object AllCategoryScreenRoutes

    @Serializable
    data class AllProductsByCategoryRoutes(
        val CategoryName: String
    )

    @Serializable
    data class EditBannerScreenRoutes(
        val bannerId: String
    )

    @Serializable
    object ManageBannersScreenRoutes
}