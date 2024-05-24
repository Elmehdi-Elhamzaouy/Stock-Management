package com.example.stock_managment_app.dataClass

data class Stock(
    val id: Long,
    val name: String,
    val company: String,
    val category : String,
    val supplier : String,
    val quantity :Int,
    val date : String,
    val totalPrice : Double

)
