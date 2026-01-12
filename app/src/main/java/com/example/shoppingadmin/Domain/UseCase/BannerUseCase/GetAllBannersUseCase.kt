package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetAllBannersUseCase(private val repo: Repo) {
    suspend fun getAllBannersUseCase() = repo.getAllBanners()
}