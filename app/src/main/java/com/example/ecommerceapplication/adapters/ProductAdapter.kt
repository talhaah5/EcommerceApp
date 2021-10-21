package com.example.ecommerceapplication.adapters
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.ItemPromoProductsBinding
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.utils.CartParser
import com.example.ecommerceapplication.global.Global

import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), Filterable {

    private var productList: List<Product>? = null
    private var filteredProductList: List<Product> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val binding = DataBindingUtil.inflate<ItemPromoProductsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_promo_products, parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredProductList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(context,filteredProductList[holder.adapterPosition],position,itemClickListener)
        holder.itemView.setOnClickListener { itemClickListener.onChildItemClick(filteredProductList[holder.adapterPosition]) }
    }

    fun setList(list: List<Product>) {

        this.productList = list
        filteredProductList = list
        notifyDataSetChanged()
    }

    class ProductViewHolder(var binding: ItemPromoProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(context:Context, product: Product,position: Int, itemClickListener:OnItemClickListener) {
            val cartParser = CartParser(context)
            val handler = Handler(Looper.getMainLooper())
            binding.product = product



            if(Global.PRODUCT_HASHMAP.containsKey(product.id)) {
                binding.countTv.text = Global.PRODUCT_HASHMAP[product.id].toString()
                binding.totalCountTv.text = Global.PRODUCT_HASHMAP[product.id].toString()
                binding.totalCountTv.visibility = View.VISIBLE
                binding.addButtonToCart.visibility = View.GONE
                binding.isVisibleCount = true

            }
            else {
                binding.totalCountTv.visibility = View.GONE
                binding.addButtonToCart.visibility = View.VISIBLE
                binding.totalCountTv.text = "1"
                binding.countTv.text = "1"
                binding.isVisibleCount = false
            }
            if(binding.countTv.text.toString().toInt() == 1)
                binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_bx_trash)
            else
                binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24)

            binding.totalCountTv.setOnClickListener {
                binding.countTv.visibility = View.VISIBLE
                binding.subtractButtonToCart.visibility = View.VISIBLE
                binding.addButtonToCart.visibility = View.VISIBLE
                binding.totalCountTv.visibility = View.GONE
                binding.isVisibleCount = false
            }

            binding.addButtonToCart.setOnClickListener{

                binding.isVisibleCount = false
                handler.removeCallbacksAndMessages(null);
                if(binding.countTv.visibility == View.GONE){

                        binding.totalCountTv.text = "1"
                        binding.countTv.text = "1"
                        binding.countTv.visibility = View.VISIBLE
                        binding.subtractButtonToCart.visibility = View.VISIBLE
                        product.quantity = binding.countTv.text.toString().toInt()
                        cartParser.addUpdateProduct(product)


                }else{
                        binding.countTv.text = (binding.countTv.text.toString().toInt()+1).toString()
                        binding.totalCountTv.text = binding.countTv.text
                        product.quantity = binding.countTv.text.toString().toInt()
                        if (!cartParser.addUpdateProduct(product)) {
                            Toast.makeText(
                                context,
                                "Product not added something bad happened.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }


                handler.postDelayed({
                    binding.countTv.visibility = View.GONE
                    binding.subtractButtonToCart.visibility = View.GONE
                    binding.addButtonToCart.visibility = View.GONE
                    binding.totalCountTv.visibility = View.VISIBLE
                    binding.isVisibleCount = true
                    itemClickListener.onCartChange(product,position)
                }, 3000)
                if(binding.countTv.text.toString().toInt() == 1)
                    binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_bx_trash)
                else
                    binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24)


            }

            binding.subtractButtonToCart.setOnClickListener {
                if(binding.countTv.text.toString().toInt() > 1){
                    handler.removeCallbacksAndMessages(null);
                    if(binding.countTv.text.toString().toInt()-1 != 0) {
                        binding.countTv.text = (binding.countTv.text.toString().toInt() - 1).toString()
                        binding.totalCountTv.text = binding.countTv.text
                    }

                    product.quantity = binding.countTv.text.toString().toInt()
                    if (cartParser.addUpdateProduct(product)) {

                    } else {
                        Toast.makeText(
                            context,
                            "Product not added something bad happened.",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                    if(binding.countTv.text.toString().toInt() == 1) {
                        binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_bx_trash)
                        Log.d(TAG, "bind: 1")
                    }
                    else {
                        binding.subtractButtonToCart.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24)
                        Log.d(TAG, "bind: not 1")
                    }
                    handler.postDelayed(Runnable {
                        binding.countTv.visibility = View.GONE
                        binding.subtractButtonToCart.visibility = View.GONE
                        binding.addButtonToCart.visibility = View.GONE
                        binding.totalCountTv.visibility = View.VISIBLE
                        binding.isVisibleCount = true
                        itemClickListener.onCartChange(product, position)
                    }, 3000)
                }else{
                    binding.countTv.text = "0"
                    binding.totalCountTv.text = binding.countTv.text
                    try{
                        cartParser.removeProduct(product)
                        itemClickListener.onCartChange(product,position)
                    }catch (e:Exception){
                        Log.d(TAG, "bind: "+e)
                    }
                    binding.countTv.visibility = View.GONE
                    binding.subtractButtonToCart.visibility = View.GONE
                    binding.addButtonToCart.visibility = View.VISIBLE
                    binding.totalCountTv.visibility = View.GONE
                    binding.isVisibleCount = false
                }


            }
        }
    }

    companion object {
        private const val TAG = "AislesAdapter";
    }

    interface OnItemClickListener {
        fun onChildItemClick(product: Product)
        fun onCartChange(product: Product,position: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredProductList = if (charSearch.isEmpty()) {
                    productList!!
                } else {
                    val resultList = ArrayList<Product>()
                    for (row in productList!!) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredProductList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredProductList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
    }

}