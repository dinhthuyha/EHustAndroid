package com.prdcv.ehust.ui.profile

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentProfileBinding
import com.prdcv.ehust.model.User
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ProfileFragment : BaseFragmentWithBinding<FragmentProfileBinding>() {
    private var user: User? =null
    companion object {
        fun newInstance() = ProfileFragment()
    }
    private fun initArgs(){

    }

    private lateinit var viewModel: ProfileViewModel

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater).apply {
            userItem = user
    }

    override fun init() {
        binding.imgAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_studentsFragment)
        }
        shareViewModel.user.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {

                }
                is State.Success -> {
                    user = state.data
                }
                is State.Success -> {

                }
            }
        }
    }

}