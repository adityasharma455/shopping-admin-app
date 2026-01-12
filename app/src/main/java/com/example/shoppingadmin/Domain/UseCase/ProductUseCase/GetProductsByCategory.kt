package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetProductsByCategory(private val repo: Repo) {
    suspend fun getProductsByCategory(CategoryName: String) = repo.getProductsByCategory(CategoryName)
}