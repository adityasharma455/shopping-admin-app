package com.example.shoppingadmin.Presentation.DI

import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// di/PresentationModule.kt
val presentationModule = module {
    // ViewModels
    viewModel<MyViewModel> { MyViewModel(
        addCategoryUseCase = get(),
        getCategoryUseCase = get(),
        addProductUseCase = get(),
        addProductPhotoUseCase = get(),
        addBannerUserUserCase = get(),
        addBannerModelPhotoUserUseCase = get(),
        createUserUseCase = get(),
        getAllAdminProductsUseCase = get(),
        getSpecificProductAdminUseCase = get(),
        removeSpecificProductAdminUseCaes = get(),
        updateProductDataAdminUseCase = get(),
        getProductsByCategory = get(),
        getSearchProductUseCase = get(),
        getAllBannersUseCase = get(),
        getBannerByIdUseCase = get(),
        deleteBannerUseCase = get(),
        deleteBannerImgaeUseCase = get(),
        updateBannerUseCase = get(),
        getAllOrdersUseCase = get(),
        updateOrderUseCase = get()
    )
    }
}