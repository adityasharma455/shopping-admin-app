package com.example.shoppingadmin.Domain.UseCase.OrderUseCase

import com.example.shoppingadmin.Domain.repo.Repo

class GetAllOrdersUseCase (private val repo: Repo) {
    suspend fun getAllOrdersUseCase()= repo.getAllOrders()
}