package com.prdcv.ehust.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentProfileBinding
import com.prdcv.ehust.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragmentWithBinding<FragmentProfileBinding>() {
    private var user: User? = null
    var args: ProfileFragmentArgs? = null
    private val TAG = "ProfileFragment"

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewProfile.setContent {
            ProfileCard(user)
        }
    }

    private fun initArgs() {
        try {
            user = shareViewModel.user
            val args: ProfileFragmentArgs by navArgs()
            user = args.user
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

    override fun init() {}

}