package com.example.ecommerceapplication.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.FragmentSignInBinding
import com.example.ecommerceapplication.models.User
import com.example.ecommerceapplication.network.APiInterface
import com.example.ecommerceapplication.network.ApiClient
import com.example.ecommerceapplication.utils.SharedPrefs
import com.example.ecommerceapplication.utils.viewBinding
import com.example.ecommerceapplication.global.Global
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val global = Global()
    val user = User()
    private lateinit var sharedPrefs: SharedPrefs

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionViews()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun actionViews() {
        sharedPrefs = SharedPrefs(requireContext())
        binding.btnSignIn.setOnClickListener {
            validations()
        }
        binding.signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragmentOne)
        }

        binding.btnGuest.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_welcomeBackFragment)
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun validations() {
        if (binding.emailEt.text?.isNotEmpty()!!) {
            if (binding.passwordEt.text?.isNotEmpty()!!) {

                    if (global.isOnline(requireContext())) {
                        loginApi(binding.emailEt.text.toString()!!,binding.passwordEt.text.toString()!!)
                    } else
                        Toast.makeText(requireContext(),"Please Check Your Internet",Toast.LENGTH_LONG).show()
            } else {
                AppCompatResources.getDrawable(requireContext(), R.drawable.shape_negative_et)
                    .also { it.also { binding.passwordEt.background = it } }
                binding.passwordEt.error = requireContext().getString(R.string.error_empty)
            }
        } else {
            binding.emailEt.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.shape_negative_et)
            binding.emailEt.error = getString(R.string.error_empty)
        }
    }

    companion object {
        private const val TAG = "SignInFragment"
    }

    private fun loginApi(email: String, password: String){
        user.username = email
        user.password = password
        requireActivity().runOnUiThread {
            binding.btnSignIn.showLoading()
        }

        val apiService: APiInterface = ApiClient.getClient().create(APiInterface::class.java)
        val call: Call<User> = apiService.login(user)
        call.enqueue( object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d(TAG, "onFailure: "+t!!.message)
                binding.btnSignIn.hideLoading()
                Toast.makeText(requireContext(),t!!.message,Toast.LENGTH_LONG).show()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.btnSignIn.hideLoading()
                Log.d(TAG, "onResponse: "+response.body()!!.token)
                Log.d(TAG, "onResponse: "+response.body()!!.firstName)
                if(response.body()!!.token.isNotEmpty()){

                    Log.d(TAG, "loginApiCall: $response")
                    try {

                        val sharedPrefs = SharedPrefs(requireContext())
                        val user = User();
                        user.token = response.body()!!.token
                        sharedPrefs.putUser(user)
                        sharedPrefs.putBoolean("login",true);
                        findNavController().navigate(R.id.action_signInFragment_to_welcomeBackFragment)

                    } catch (ex: Exception) {
                        Log.e(TAG, "loginApiCall: ", ex)
                    }
                }else{
                    Toast.makeText(requireContext(),"Email or password is wrong",Toast.LENGTH_LONG).show()
                }

            }
        })
    }

}
