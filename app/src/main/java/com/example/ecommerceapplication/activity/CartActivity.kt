package com.example.ecommerceapplication.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.adapters.CartAdapter
import com.example.ecommerceapplication.adapters.ProductAdapter
import com.example.ecommerceapplication.databinding.ActivityCartBinding
import com.example.ecommerceapplication.databinding.ActivityProductDetailsBinding
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.navigator.CartNavigator
import com.example.ecommerceapplication.utils.CartParser
import com.example.ecommerceapplication.utils.viewBinding
import com.example.ecommerceapplication.viewModel.CartViewModel

class CartActivity : AppCompatActivity() ,CartNavigator{

    private val binding by viewBinding(ActivityCartBinding::inflate)
    private lateinit var productAdapter: CartAdapter
    private var mViewModel: CartViewModel? = null
    private lateinit var cartParser: CartParser
    private lateinit var productList:ArrayList<Product>

    val TAG = "CartActivity"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mViewModel = ViewModelProviders.of(this)[CartViewModel::class.java]
        actionViews()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun actionViews() {

        cartParser = CartParser(this)

        productList = cartParser.cartProducts
        binding.total = getCartTotal(productList)
        productAdapter = CartAdapter(mViewModel!!)
        productAdapter.setList(productList)
        binding.recyclerView.adapter = productAdapter

        mViewModel!!.cartNavigator = this

        setViews()
        setCLickListener()

    }

    private fun setViews(){
        binding.actionBar.constraintLayout3.visibility = View.GONE
        binding.actionBar.title = "Cart Activity"
    }

    private fun setCLickListener(){
        binding.actionBar.imgBack.setOnClickListener {
            finish()
        }
    }


    override fun onChildItemClick(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRemoveProduct(product: Product,position:Int) {
        cartParser.removeProduct(product)
        productList.remove(product)
        productAdapter.notifyItemRemoved(position)
    }

    fun getCartTotal(products:ArrayList<Product>):Double{
        var total = 0.0
        for(product in products){
            total += product.quantity*product.price.toDouble()
        }
        return total
    }
}