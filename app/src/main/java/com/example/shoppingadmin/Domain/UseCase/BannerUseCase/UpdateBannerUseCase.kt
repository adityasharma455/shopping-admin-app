package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Domain.repo.Repo

class UpdateBannerUseCase(private val repo: Repo) {
    suspend fun updateBannerUseCase(banner: BannerDataModel)= repo.updateBanner(banner)
}