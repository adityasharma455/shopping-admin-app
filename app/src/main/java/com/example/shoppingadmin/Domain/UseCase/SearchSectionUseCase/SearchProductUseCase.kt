package com.example.shoppingadmin.Domain.UseCase.SearchSectionUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class SearchProductUseCase(private val repo: Repo) {
    suspend fun searchProductUseCase(SearchQuery: String) = repo.searchProduct(SearchQuery)
}