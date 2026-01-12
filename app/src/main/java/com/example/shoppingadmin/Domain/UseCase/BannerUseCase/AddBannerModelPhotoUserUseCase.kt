package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class AddBannerModelPhotoUserUseCase(private val repo: Repo) {
    suspend fun addBannerModelPhotoUserUseCase(byteArray: ByteArray) = repo.addBannerModelPhoto(byteArray = byteArray)
}