package com.example.ecommerceapplication.activity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.ActivityHomeBinding
import com.example.ecommerceapplication.databinding.LayoutAlertGenericBinding
import com.example.ecommerceapplication.utils.SharedPrefs
import com.example.ecommerceapplication.global.Global
import java.util.*





class HomeActivity : AppCompatActivity(){
    lateinit var binding: ActivityHomeBinding
    private val EXTRA_LOGOUT = "clearBackStack"
    private lateinit var sharedPrefs: SharedPrefs
    private val global = Global()
    private lateinit var navController: NavController
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        if (intent.getBooleanExtra(EXTRA_LOGOUT, false)) {
            clearBackStack()
        } else {
            setContentView(binding.root)
            actionViews()
        }
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun actionViews() {

        sharedPrefs = SharedPrefs(this)
        navController = findNavController(R.id.nav_host_fragment)


            binding.btnLogout.setOnClickListener {
                binding.drawerLayout.closeDrawer(Gravity.END)
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    showLogoutAlert()
                }, 200)

            }
            binding.btnLogout.text = "Logout"
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun updateCartAndNavHeader(){
//        val user = sharedPrefs.getUser()
//        binding.navHeader.user  = user;
//        binding.navHeader.itemsCount = cartParser.totalProductsCountInCart
//        val df = DecimalFormat("#.00")
//        try{
//            if(!df.format(cartParser.totalPrice).isNullOrBlank()){
//                val total: String = df.format(cartParser.totalPrice)
//                binding.navHeader.total = total
//            }else{
//                binding.navHeader.total = "$ 0.0"
//            }
//        }catch(e: Exception){
//            binding.navHeader.total = "$ 0.0"
//            Log.d(TAG, "updateCartAndNavHeader: $e")
//        }
//
//
//    }

    private fun clearBackStack() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onResume() {
        super.onResume()
        try{
//            updateCartAndNavHeader()
        }catch(e: IllegalStateException){
            Log.d(TAG, "onResume: $e")
        }

    }

    fun openDrawer() {

        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.END)) binding.drawerLayout.openDrawer(
            GravityCompat.END
        )
        else binding.drawerLayout.closeDrawer(GravityCompat.END);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showLogoutAlert() {
        val layoutParkHereBinding: LayoutAlertGenericBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.layout_alert_generic, null, false)
        val builder =
            AlertDialog.Builder(this)
        builder.setView(layoutParkHereBinding.root)
        val alertDialog = builder.create()
        Objects.requireNonNull(alertDialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParkHereBinding.title = "Logout"
        layoutParkHereBinding.subTitle = "You will be returned to login screen"
        layoutParkHereBinding.positiveBtnText = "Logout"
        layoutParkHereBinding.negativeBtnText = "Cancel"
        layoutParkHereBinding.btnYes.setOnClickListener {
            alertDialog.dismiss()
            sharedPrefs.clear()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()
            sharedPrefs.putUser(null)
        }
        layoutParkHereBinding.btnNo.setOnClickListener { v -> alertDialog.dismiss() }
        alertDialog.show()
    }
    override fun onBackPressed() {
        showAlertDialog(this, "Do you want to exit")
    }
    fun showAlertDialog(context: Context, message: String?) {
        val genericBinding: LayoutAlertGenericBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_alert_generic,
            null,
            false
        )
        val builder = AlertDialog.Builder(context)
        builder.setView(genericBinding.root)
        val alertDialog = builder.create()
        Objects.requireNonNull(alertDialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        genericBinding.title = "Confirm Exit"
        genericBinding.subTitle = message
        genericBinding.positiveBtnText = "Yes"
        genericBinding.negativeBtnText = "No"
        genericBinding.titleTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )

//        genericBinding.btnNo.visibility = View.GONE

        genericBinding.btnYes.setOnClickListener { v ->
            alertDialog.dismiss()
            finish()
        }
        genericBinding.btnNo.setOnClickListener { v ->
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}