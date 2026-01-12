package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetSpecificProductAdminUseCase (private val repo: Repo) {
    suspend fun getSpecificProduct( productID: String)= repo.getspecificProduct(productID)
}