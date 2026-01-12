package com.example.shoppingadmin.Domain.UseCase.CategoryUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetAllCategoryUseCase  constructor(private val repo: Repo) {
    suspend fun getAllCategoryUseCase() = repo.getCategories()
}