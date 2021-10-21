package com.example.ecommerceapplication.fragments
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.activity.HomeActivity
import com.example.ecommerceapplication.adapters.ProductAdapter
import com.example.ecommerceapplication.databinding.FragmentHomeBinding
import com.example.ecommerceapplication.models.Product
import com.example.ecommerceapplication.network.APiInterface
import com.example.ecommerceapplication.network.ApiClient
import com.example.ecommerceapplication.utils.CartParser
import com.example.ecommerceapplication.utils.SharedPrefs
import com.example.ecommerceapplication.utils.viewBinding
import com.example.ecommerceapplication.global.Global
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception


open class HomeFragment : Fragment(R.layout.fragment_home),ProductAdapter.OnItemClickListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var cartParser: CartParser;
    private lateinit var productAdapter: ProductAdapter
    private lateinit var  productList:List<Product>
    private lateinit var sharedPrefs: SharedPrefs
    private val global = Global()


    companion object {
        private const val TAG = "HomeFragment"
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        actionViews()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun actionViews() {

        cartParser = CartParser(requireContext())
        cartParser.getHashMapOfProduct()
        productAdapter = ProductAdapter(requireContext(),this)
        sharedPrefs = SharedPrefs(requireContext())

        setClickListeners()

        if (global.isOnline(requireContext())) {
            getProductsApiCall()
        } else
            Toast.makeText(requireContext(),"Please Check Your Internet", Toast.LENGTH_LONG).show()


    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners(){

        binding.cartItemsCountTv.text = Global.PRODUCT_HASHMAP.size.toString()
        binding.imgDrawer.setOnClickListener {
            try{
                (activity as HomeActivity?)?.openDrawer()
            }catch (e:Exception){
                Log.d(TAG, "actionViews: $e")
            }
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter.filter(newText)
                return false
            }

        })

        binding.retryBtn.setOnClickListener{
            if (global.isOnline(requireContext())) {
                getProductsApiCall()
            } else
                Toast.makeText(requireContext(),"Please Check Your Internet", Toast.LENGTH_LONG).show()
        }

        binding.imgCart.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_cartActivity)
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getProductsApiCall() {

        binding.progressBar.visibility = View.VISIBLE
        Log.d(TAG, "getProductsApiCall: ")
        val apiService: APiInterface = ApiClient.getClient().create(APiInterface::class.java)
        val call: Call<List<Product>> = apiService.products
        call.enqueue( object : Callback<List<Product>>{
            override fun onFailure(call: Call<List<Product>>?, t: Throwable?) {
                Log.d(TAG, "onFailure: "+t!!.message)
                binding.retryConstraintLayout.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<List<Product>>,
                response: retrofit2.Response<List<Product>>
            ) {
                binding.progressBar.visibility = View.GONE
                binding.retryConstraintLayout.visibility = View.GONE
                productList = response.body()!!
                productAdapter.setList(productList)
                binding.recyclerView.adapter = productAdapter

            }
        })
    }

    override fun onChildItemClick(product: Product) {

        Log.d(TAG, "onChildItemClick: "+product.title)
        val action = HomeFragmentDirections.actionNavHomeToProductDetailsActivity(
           product
        )
        findNavController().navigate(action)
    }


    override fun onCartChange(product: Product,position: Int) {
        productAdapter.notifyItemChanged(position)
        binding.cartItemsCountTv.text = Global.PRODUCT_HASHMAP.size.toString()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        try {
            productAdapter.notifyDataSetChanged()
            if (!::cartParser.isInitialized)
                cartParser = CartParser(requireContext());
            binding.cartItemsCountTv.text = Global.PRODUCT_HASHMAP.size.toString()
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "onResume: $e")
        }
    }

}