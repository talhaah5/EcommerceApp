package com.example.ecommerceapplication.viewModel
import androidx.lifecycle.ViewModel
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.navigator.CartNavigator


class CartViewModel : ViewModel() {
    var cartNavigator: CartNavigator? = null

    fun onProductClick(product: Product) {
        cartNavigator!!.onChildItemClick(product)
    }

    fun onRemoveProduct(product: Product,position: Int) {
        cartNavigator!!.onRemoveProduct(product,position)
    }
}