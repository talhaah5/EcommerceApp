package com.example.ecommerceapplication.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast

import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs

import com.example.ecommerceapplication.databinding.ActivityProductDetailsBinding
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.utils.CartParser
import com.example.ecommerceapplication.utils.SharedPrefs
import com.example.ecommerceapplication.utils.viewBinding

import com.example.ecommerceapplication.global.Global
import java.lang.Exception


@Suppress("NAME_SHADOWING")
class ProductDetailsActivity : AppCompatActivity(){
    private val binding by viewBinding(ActivityProductDetailsBinding::inflate)
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var cartParser: CartParser
    private lateinit var product: Product
    var quantityCount = 1;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        actionViews()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun actionViews(){

        cartParser = CartParser(this)
        sharedPrefs = SharedPrefs(this)

        setViews()
        setClickListeners()

    }

    private fun parseIntent(){
        if(!intent.hasExtra("product")) {
            val args: ProductDetailsActivityArgs by navArgs()
            product = args.Product;
        }else{
            product = intent.getParcelableExtra<Product>("product")!!
        }
    }

    private fun setViews(){
        binding.actionBar.constraintLayout3.visibility = View.GONE
        binding.product = product

        try{
            if(Global.PRODUCT_HASHMAP[product.id]!! > 0) {
                binding.quantityTv.text = Global.PRODUCT_HASHMAP[product.id].toString()
                quantityCount = Global.PRODUCT_HASHMAP[product.id]!!
            }
        }catch (e:Exception){
            Log.d(TAG, "actionViews: $e")
        }

        binding.actionBar.isProductDetails = true
        binding.appCompatRatingBar.rating = product.rating.rate.toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners(){
        binding.actionBar.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding.removeImg.setOnClickListener {
            if (quantityCount > 1) {
                quantityCount--
                binding.quantityTv.text = quantityCount.toString()
            }
        }

        binding.addImg.setOnClickListener {

//            hard code qty in stock
            if (quantityCount < 100) {
                quantityCount++
                binding.quantityTv.text = quantityCount.toString()
            } else {
                binding.addImg.isEnabled = false
                Toast.makeText(this, "No more quantity in stock available.", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        binding.btnAddToCart.setOnClickListener {
            product.quantity = quantityCount
            cartParser.addUpdateProduct(product)
            finish()
        }

    }

    companion object {
        private const val TAG = "ProductDetailsActivity"
    }

}