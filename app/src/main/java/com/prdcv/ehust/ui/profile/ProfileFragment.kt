package com.prdcv.ehust.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.ProfileFragmentBinding

class ProfileFragment : BaseFragmentWithBinding<ProfileFragmentBinding>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun getViewBinding(inflater: LayoutInflater): ProfileFragmentBinding = ProfileFragmentBinding.inflate(inflater)

    override fun init() {

    }

}