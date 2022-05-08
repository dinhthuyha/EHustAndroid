package com.prdcv.ehust.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.ProfileFragmentBinding
import javax.inject.Inject

class ProfileFragment : BaseFragmentWithBinding<ProfileFragmentBinding>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun getViewBinding(inflater: LayoutInflater): ProfileFragmentBinding = ProfileFragmentBinding.inflate(inflater)

    override fun init() {
        binding.imgAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_informationStudentFragment)
        }
    }

}