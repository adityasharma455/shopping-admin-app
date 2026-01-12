package com.example.shoppingadmin.Domain.UseCase.ProductUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class AddProductPhotoUseCase (private val repo: Repo) {
    suspend fun addProductPhotoUseCase(byteArray: ByteArray) = repo.addProductPhoto(byteArray)
}