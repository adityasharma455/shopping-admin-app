package com.example.shoppingadmin.Domain.UseCase.CategoryUseCase

import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Domain.repo.Repo

class AddCategoryUseCase  constructor(private val repo: Repo) {

    suspend fun addCategoryUseCase(category: CategoryModel) =
        repo.addCategpry(category)
}