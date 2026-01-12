package com.example.shoppingadmin.Domain.UseCase.BannerUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetBannerByIdUseCase(private val repo: Repo) {
    suspend fun getBannerByIdUseCase(bannerId: String) = repo.getBannerById(bannerId)
}