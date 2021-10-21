package com.example.ecommerceapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.databinding.FragmentWelcomeBackBinding
import com.example.ecommerceapplication.utils.viewBinding


class WelcomeBackFragment : Fragment(R.layout.fragment_welcome_back) {

    private val binding by viewBinding(FragmentWelcomeBackBinding::bind)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        actionViews()
    }

    private fun actionViews() {
       try {

            binding.isNewUser = false;
            binding.btnContinue.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBackFragment_to_homeActivity)
            }
        } catch (e: Exception) {
            Log.e(TAG, "actionViews: ", e)
        }

    }

    companion object {
        private const val TAG = "WelcomeBackFragment"
    }


}