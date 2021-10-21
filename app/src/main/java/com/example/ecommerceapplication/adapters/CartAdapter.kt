package com.example.ecommerceapplication.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.ItemCartChildBinding
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.viewModel.CartViewModel

import java.util.*
import kotlin.collections.ArrayList

class CartAdapter(
    private val mViewModel: CartViewModel,
) :
    RecyclerView.Adapter<CartAdapter.CartProductViewHolder>(), Filterable {
    private var productList: ArrayList<Product>? = null
    private var filteredProductList: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartChildBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_child, parent, false
        )
        return CartProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredProductList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(position,filteredProductList[holder.adapterPosition],mViewModel)
    }

    fun setList(list: ArrayList<Product>) {

        this.productList = list
        filteredProductList = list
        notifyDataSetChanged()
    }

    class CartProductViewHolder(var binding: ItemCartChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, product: Product,mViewModel: CartViewModel) {
            binding.product = product
            binding.position = position
            binding.cartViewModel = mViewModel
        }
    }

    companion object {
        private const val TAG = "CartAdapter";
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredProductList = productList!!
                } else {
                    val resultList = ArrayList<Product>()
                    for (row in productList!!) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filteredProductList = resultList
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