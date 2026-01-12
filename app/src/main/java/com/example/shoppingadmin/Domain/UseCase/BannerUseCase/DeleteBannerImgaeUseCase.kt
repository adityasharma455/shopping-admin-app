package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class DeleteBannerImgaeUseCase(private val repo: Repo) {
    suspend fun deleteBannerImage(imageUrl : String ) = repo.deleteBannerImage(imageUrl)
}