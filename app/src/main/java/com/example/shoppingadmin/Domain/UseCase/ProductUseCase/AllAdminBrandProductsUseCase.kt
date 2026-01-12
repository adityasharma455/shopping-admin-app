package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class AllAdminProductsUseCase(private val repo: Repo) {
    suspend fun allAdminProductsUseCase() = repo.adminAllProducts()

}