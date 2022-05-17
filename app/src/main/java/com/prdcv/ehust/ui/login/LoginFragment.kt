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
    override fun getViewBinding(inflater: LayoutInflater) = FragmentLoginBinding.inflate(inflater)
    override fun init() {
        binding.login.setOnClickListener {
            if (binding.contentId.text?.isNotEmpty() == true  && binding.contentPassword.text?.isNotEmpty() == true ){
                Log.d("hadinh", "id: ${binding.contentId.text.toString().toInt()}")
                shareViewModel.login(binding.contentId.text.toString().toInt(),"123456")
                shareViewModel.token.observe(viewLifecycleOwner){
                    when(it){
                        is State.Success->{
                            shareViewModel.decodeToken(it.data)
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                        is State.Error->{
                            Toast.makeText(context,"$it",Toast.LENGTH_LONG ).show()
                        }
                    }
                }
            }


        }
    }

}