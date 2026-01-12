package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class DeleteBannerUseCase(private val repo: Repo) {
    suspend fun deleteBannerUseCase(bannerId: String)= repo.deleteBanner(bannerId)
}