package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Domain.repo.Repo

class AddProductUseCase  constructor(private val repo: Repo) {
    suspend fun addProductUseCase(product : ProductModel) = repo.addProduct(product)
}