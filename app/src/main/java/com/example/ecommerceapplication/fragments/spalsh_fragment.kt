package com.example.ecommerceapplication.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.utils.SharedPrefs
import java.lang.Exception

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
        private const val TAG = "SplashFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_spalsh, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {

            val sharedPrefs = SharedPrefs(requireContext())
            val user = sharedPrefs.getUser();
//            findNavController().navigate(R.id.action_splashFragment_to_welcomeBackFragment);
            if(user==null){
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_signInEmail);
                },1000);
            }else{
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_welcomeBackFragment);
                },1000);

            }


        }catch (ex : Exception){
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragmentOne);
            },1000);

        }


    }
}