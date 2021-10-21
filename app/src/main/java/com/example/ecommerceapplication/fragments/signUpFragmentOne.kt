package com.example.ecommerceapplication.fragments
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.FragmentSignUpOneBinding
import com.example.ecommerceapplication.models.User
import com.example.ecommerceapplication.network.APiInterface
import com.example.ecommerceapplication.network.ApiClient
import com.example.ecommerceapplication.utils.SharedPrefs
import com.example.ecommerceapplication.utils.viewBinding
import com.example.ecommerceapplication.global.Global
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragmentOne : Fragment(R.layout.fragment_sign_up_one) {

    private val binding by viewBinding(FragmentSignUpOneBinding::bind)
    private  var user = User()
    private val global = Global()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()

    }

    private fun clickListeners() {

        binding.btnNextStep.setOnClickListener {
            validations()
        }
        binding.signUpTv.setOnClickListener { findNavController().navigate(R.id.action_signUpFragmentOne_to_signInFragment) }

        binding.firstNameEt.doAfterTextChanged {

            if(binding.lastNameEt.text?.isNotEmpty()!!){
                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.spruce_valencia)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

            }else{

                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.viewDisabledColor)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.spruce_light_gray_25
                    )
                )

            }


            if(binding.firstNameEt.text?.isEmpty()!!){

                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.viewDisabledColor)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.spruce_light_gray_25
                    )
                )
            }
        }
        binding.lastNameEt.doAfterTextChanged {
            if(binding.firstNameEt.text?.isNotEmpty()!!){
                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.spruce_valencia)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }else{

                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.viewDisabledColor)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.spruce_light_gray_25
                    )
                )
            }

            if(binding.lastNameEt.text?.isEmpty()!!){

                ViewCompat.setBackgroundTintList(
                    binding.btnNextStep,
                    ContextCompat.getColorStateList(requireContext(), R.color.viewDisabledColor)
                )
                binding.btnNextStep.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.spruce_light_gray_25
                    )
                )
            }
        }

    }


    private fun validations() {
        if (binding.firstNameEt.text?.trim()?.isNotEmpty()!!) {
            if (binding.lastNameEt.text?.trim()?.isNotEmpty()!!) {
                if(binding.emailEt.text?.trim()?.isNotEmpty()!! && global.isEmailValid(binding.emailEt.text?.trim().toString())){
                    if(binding.passwordEt.text?.trim()?.isNotEmpty()!! && binding.passwordEt.text?.trim()?.length!! >= 8){
                        if(binding.usernameEt.text?.trim()?.isNotEmpty()!!){
                            user.firstName = binding.firstNameEt.text.toString()
                            user.lastName = binding.lastNameEt.text.toString()
                            user.email = binding.emailEt.text.toString()
                            user.password = binding.passwordEt.text.toString()
                            user.username = binding.usernameEt.text.toString()
//                            signUpApiCall()
                            signUpAPI()

                        }else{
                            binding.usernameEt.background = getDrawable(
                                requireContext(),
                                R.drawable.shape_negative_et
                            )
                            binding.usernameEt.error = getString(R.string.error_empty)
                        }

                    }else{
                        binding.passwordEt.background = getDrawable(
                            requireContext(),
                            R.drawable.shape_negative_et
                        )
                        binding.passwordEt.error = "Password should be atleast 8 characters long"
                    }

                }else{
                    binding.emailEt.background = getDrawable(
                        requireContext(),
                        R.drawable.shape_negative_et
                    )
                    binding.emailEt.error ="Email must be valid"
                }

            } else {
                binding.lastNameEt.background = getDrawable(
                    requireContext(),
                    R.drawable.shape_negative_et
                )
                binding.lastNameEt.error = getString(R.string.error_empty)
            }
        } else {
            binding.firstNameEt.background =
                getDrawable(requireContext(), R.drawable.shape_negative_et)
            binding.firstNameEt.error = getString(R.string.error_empty)
        }
    }

    companion object {
        private const val TAG = "SignUpFragmentOne"
    }

    fun signUpAPI(){
        binding.btnNextStep.showLoading()
        val apiService: APiInterface = ApiClient.getClient().create(APiInterface::class.java)
        val call: Call<User> = apiService.signUp(user)
        call.enqueue( object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d(TAG, "onFailure: "+t!!.message)
                binding.btnNextStep.hideLoading()
                Toast.makeText(requireContext(),t!!.message,Toast.LENGTH_LONG).show()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.btnNextStep.hideLoading()
                try {
                    if(response.body()!!.token.isNotEmpty()){
                        val sharedPrefs = SharedPrefs(requireContext())
                        val user = User();
                        user.token = response.body()!!.token
                        sharedPrefs.putUser(user)
                        sharedPrefs.putBoolean("login",true);
                        Toast.makeText(requireContext(),"Sign Up successfully",Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_signUpFragmentOne_to_signInFragment)
                    }else{
                        Toast.makeText(requireContext(),"Email or password is wrong",Toast.LENGTH_LONG).show()
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "loginApiCall: ", ex)
                    Toast.makeText(requireContext(),"Something is technically wrong. Please try again later",Toast.LENGTH_LONG).show()
                }

            }
        })
    }
}