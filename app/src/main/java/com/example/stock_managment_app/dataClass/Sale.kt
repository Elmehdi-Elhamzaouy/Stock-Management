package com.example.stock_managment_app.dataClass

data class Sale(
    val id: Long,
    val name: String,
    val company: String,
    val buyer: String,
    val quantity: Int,
    val date: String,
    val totalPrice: Double
)
