package com.example.shoppingadmin.Domain.Di

import com.example.shoppingadmin.Domain.UseCase.AuthSectionUseCase.CreateUserUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.AddBannerModelPhotoUserUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.AddBannerUserUserCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.DeleteBannerImgaeUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.DeleteBannerUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.GetAllBannersUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.GetBannerByIdUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.UpdateBannerUseCase
import com.example.shoppingadmin.Domain.UseCase.CategoryUseCase.AddCategoryUseCase
import com.example.shoppingadmin.Domain.UseCase.CategoryUseCase.GetAllCategoryUseCase
import com.example.shoppingadmin.Domain.UseCase.OrderUseCase.GetAllOrdersUseCase
import com.example.shoppingadmin.Domain.UseCase.OrderUseCase.UpdateOrderStatusUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AddProductPhotoUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AddProductUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AllAdminProductsUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.GetProductsByCategory
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.GetSpecificProductAdminUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.RemoveSpecificProductAdminUseCaes
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.UpdateProductDataAdminUseCase
import com.example.shoppingadmin.Domain.UseCase.SearchSectionUseCase.SearchProductUseCase
import org.koin.dsl.module


// di/domain/DomainModule.kt
val domainModule = module {
    // UseCases
    factory{AddCategoryUseCase(get()) }
    factory{ GetAllCategoryUseCase(get()) }
    factory{ AddProductUseCase(get()) }
    factory{ AddProductPhotoUseCase(get()) }
    factory{ AddBannerUserUserCase(get()) }
    factory{ AddBannerModelPhotoUserUseCase(get()) }
    factory{ CreateUserUseCase(get()) }
   factory { AllAdminProductsUseCase(get()) }
    factory { GetSpecificProductAdminUseCase(get()) }
    factory { UpdateProductDataAdminUseCase(get()) }
    factory { RemoveSpecificProductAdminUseCaes(get()) }
    factory { GetProductsByCategory(get()) }
    factory { SearchProductUseCase(get()) }
    factory { GetAllBannersUseCase(get()) }
    factory { GetBannerByIdUseCase(get()) }
    factory { DeleteBannerUseCase(get()) }
    factory { DeleteBannerImgaeUseCase(get()) }
    factory { DeleteBannerImgaeUseCase(get()) }
    factory { UpdateBannerUseCase(get()) }
    factory{ GetAllOrdersUseCase(get()) }
    factory { UpdateOrderStatusUseCase( get()) }

}