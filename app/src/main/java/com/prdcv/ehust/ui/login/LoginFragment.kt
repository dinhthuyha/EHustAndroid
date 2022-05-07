package com.prdcv.ehust.ui.login

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentLoginBinding


class LoginFragment : BaseFragmentWithBinding<FragmentLoginBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = FragmentLoginBinding.inflate(inflater)
    override fun init() {
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

}