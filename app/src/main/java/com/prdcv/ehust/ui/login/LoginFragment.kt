package com.prdcv.ehust.ui.login

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentLoginBinding
import com.prdcv.ehust.extension.hideKeyboard
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.utils.SharedPreferencesKey
import com.royrodriguez.transitionbutton.TransitionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragmentWithBinding<FragmentLoginBinding>() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun getViewBinding(inflater: LayoutInflater) = FragmentLoginBinding.inflate(inflater)
    override fun init() {
        shareViewModel.token.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    binding.login.stopAnimation(
                        TransitionButton.StopAnimationStyle.EXPAND
                    ) {
                        shareViewModel.decodeResponseLogin(it.data)
                        if (shareViewModel.user?.roleId == Role.ROLE_ADMIN) {
                            findNavController().navigate(R.id.action_loginFragment_to_homeAdminFragment)
                        } else
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                }
                is State.Error -> {
                    binding.login.stopAnimation(
                        TransitionButton.StopAnimationStyle.SHAKE,
                        null
                    )
                    Toast.makeText(
                        context,
                        "Đăng nhập không thành công!\n${it.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                    setInputEnabled(true)
                }
                is State.Loading -> {
                    binding.login.startAnimation()
                    setInputEnabled(false)
                }
            }
        }

        binding.apply {
            login.setOnClickListener {
                if (contentId.text?.isNotEmpty() == true && contentPassword.text?.isNotEmpty() == true) {
                    hideKeyboard()
                    shareViewModel.login(
                        contentId.text.toString().toInt(),
                        contentPassword.text.toString()
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Vui lòng nhập mã số sinh viên và mật khẩu",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        if (checkTokenSaved()) {
            shareViewModel.checkToken()
        }
    }

    private fun setInputEnabled(enabled: Boolean) {
        binding.apply {
            contentId.isEnabled = enabled
            contentPassword.isEnabled = enabled
        }
    }

    private fun checkTokenSaved(): Boolean {
        val token = sharedPreferences.getString(SharedPreferencesKey.TOKEN, "")
        return !token.isNullOrBlank()
    }

}