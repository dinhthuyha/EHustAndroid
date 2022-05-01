package com.prdcv.ehust.ui.loginp
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.databinding.FragmentLoginBinding
import com.prdcv.ehust.ui.login.LoginViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun getLayoutView(): Int = R.layout.fragment_login

    override fun getClassViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun init() {
        TODO("Not yet implemented")
    }
}