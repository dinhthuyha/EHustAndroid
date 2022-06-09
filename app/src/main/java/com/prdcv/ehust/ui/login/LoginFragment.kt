package com.prdcv.ehust.ui.login

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentLoginBinding
import com.prdcv.ehust.extension.hideKeyboard
import com.royrodriguez.transitionbutton.TransitionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragmentWithBinding<FragmentLoginBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = FragmentLoginBinding.inflate(inflater)
    override fun init() {
        binding.login.setOnClickListener {
            if (binding.contentId.text?.isNotEmpty() == true  && binding.contentPassword.text?.isNotEmpty() == true ){
                hideKeyboard()
                binding.login.startAnimation()
                shareViewModel.login(binding.contentId.text.toString().toInt(),binding.contentPassword.text.toString())
                Log.d("TAG", "init: ${binding.contentId.text.toString().toInt()}, ${binding.contentPassword.text.toString()}")
                shareViewModel.token.observe(viewLifecycleOwner){
                    when(it){
                        is State.Success->{
                            val handler = Handler()
                            handler.postDelayed({
                                binding.login.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND
                                ) {
                                    shareViewModel.decodeResponseLogin(it.data)
                                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                }
                            }, 800)
                        }
                        is State.Error->{
                            binding.login.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                            Toast.makeText(context,"Đăng nhập không thành công!",Toast.LENGTH_LONG ).show()
                        }
                    }
                }
            }else{
                Toast.makeText(context, " Vui lòng nhập mã số sinh viên và mật khẩu", Toast.LENGTH_LONG).show()
            }
        }
    }


}