package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class RemoveSpecificProductAdminUseCaes(private val repo: Repo) {
    suspend fun removeSpecificProduct( productID: String)= repo.removeProducts(productID)

}