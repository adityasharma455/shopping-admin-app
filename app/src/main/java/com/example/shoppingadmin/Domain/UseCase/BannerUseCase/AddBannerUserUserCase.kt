package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Domain.repo.Repo

class AddBannerUserUserCase(private val repo: Repo) {
    suspend fun addBannerUserUserCase(bannerModel: BannerDataModel) = repo.addBannerModel(bannerModel)
}