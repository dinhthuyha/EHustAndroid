package com.prdcv.ehust.ui.profile

import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.databinding.ProfileFragmentBinding

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun getLayoutView(): Int = R.layout.profile_fragment

    override fun getClassViewModel(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun init() {

    }

}