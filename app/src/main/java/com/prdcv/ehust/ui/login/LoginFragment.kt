package com.prdcv.ehust.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.common.succeeded
import com.prdcv.ehust.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragmentWithBinding<FragmentLoginBinding>() {
   val viewModel by viewModels<LoginViewModel>()
    override fun getViewBinding(inflater: LayoutInflater) = FragmentLoginBinding.inflate(inflater)
    override fun init() {

        binding.login.setOnClickListener {
            viewModel.login(20173086,"123456")
            viewModel.token.observe(viewLifecycleOwner){
                if (it is State.Success){
                    Toast.makeText(context,"${it.data}",Toast.LENGTH_LONG ).show()
                    Log.d("hadinh", "init: ${it.data}")
                }
            }
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }



}