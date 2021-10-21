package com.example.ecommerceapplication.navigator
import com.example.ecommerceapplication.models.Product

interface CartNavigator {
    fun onChildItemClick(product: Product)
    fun onRemoveProduct(product: Product,position:Int)
}