package com.prdcv.ehust.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentProfileBinding
import com.prdcv.ehust.model.User
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class ProfileFragment : BaseFragmentWithBinding<FragmentProfileBinding>() {
    private var user: User? =null
    var args: ProfileFragmentArgs?=null
    private  val TAG = "ProfileFragment"
    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    private fun initArgs() {
        try {
            user = shareViewModel.user
            val args: ProfileFragmentArgs by navArgs()
            user = args.user
        } catch (e: Exception) {

        }

    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            user?.let {
                userItem = it
            }
        }

    override fun init() {
    }

}