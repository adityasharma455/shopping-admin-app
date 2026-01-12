package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Domain.repo.Repo

class UpdateProductDataAdminUseCase (private val repo: Repo){
    suspend fun updateProductDataAdminUseCase(product: ProductModel) = repo.updateProductData(product = product)
}