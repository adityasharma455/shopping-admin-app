package com.example.shoppingadmin.Domain.repo


import com.example.shoppingadmin.Common.ResultState
import com.example.shoppingadmin.Domain.Models.AdminDataModel
import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Domain.Models.OrderDataModel
import com.example.shoppingadmin.Domain.Models.ProductModel
import kotlinx.coroutines.flow.Flow

interface Repo {
     fun addCategpry(category : CategoryModel) : Flow<ResultState<String>>

     fun getCategories(): Flow<ResultState<List<CategoryModel>>>

     fun addProduct(product : ProductModel): Flow<ResultState<String>>

     fun addProductPhoto(byteArray: ByteArray) : Flow<ResultState<String>>

     fun addBannerModel(bannerModel: BannerDataModel): Flow<ResultState<String>>

     fun addBannerModelPhoto(byteArray: ByteArray): Flow<ResultState<String>>

     fun registerAdminWithEmailAndPassword(AdminData: AdminDataModel): Flow<ResultState<Boolean>>

     fun signInAdminWithEmailAndPassword(UserData: AdminDataModel): Flow<ResultState<Boolean>>

     fun getCurrentAdmin(): Flow<ResultState<AdminDataModel>>

     fun UpdateAdimnData(AdminData: AdminDataModel): Flow<ResultState<String>>
     fun adminSignOut(): Flow<ResultState<Boolean>>

     fun adminAllProducts(): Flow<ResultState<List<ProductModel>>>

     fun removeProducts( productID: String): Flow<ResultState<Boolean>>

     fun getspecificProduct( productID: String): Flow<ResultState<ProductModel>>

     fun updateProductData(product: ProductModel): Flow<ResultState<Boolean>>

     fun getProductsByCategory(CategoryName: String): Flow<ResultState<List<ProductModel>>>

     fun searchProduct(SearchQuery: String) : Flow<ResultState<List<ProductModel>>>

     fun deleteBannerImage(imageUrl: String): Flow<ResultState<Boolean>>

     fun deleteBanner(bannerId: String): Flow<ResultState<Boolean>>

     fun getAllBanners(): Flow<ResultState<List<BannerDataModel>>>

     fun getBannerById(bannerId: String): Flow<ResultState<BannerDataModel>>

     fun updateBanner(banner: BannerDataModel): Flow<ResultState<Boolean>>

     fun getAllOrders(): Flow<ResultState<List<OrderDataModel>>>


     fun updateOrderStatus(order: OrderDataModel, status: String): Flow<ResultState<Boolean>>


}