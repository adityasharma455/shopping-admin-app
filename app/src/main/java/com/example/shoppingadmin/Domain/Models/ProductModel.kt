package com.example.shoppingadmin.Domain.Models

data class ProductModel(
    val name: String = "",
    val price: String = "",
    val finalprice: String = "",
    val description: String ="",
    val image: String? = "",
    val category: String = "",
    val date: Long = System.currentTimeMillis(),
    val availabelUnits: String = "",
    val BrandName: String = "",
    var productId: String = ""
)
