package com.example.shoppingadmin.Domain.UseCase.OrderUseCase

import com.example.shoppingadmin.Domain.Models.OrderDataModel
import com.example.shoppingadmin.Domain.repo.Repo

class UpdateOrderStatusUseCase(private val repo: Repo) {
    suspend fun updateOrderStatusUseCase(order: OrderDataModel, status: String) = repo
        .updateOrderStatus(order, status)
}