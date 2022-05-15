package com.prdcv.ehust.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
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
    private fun initArgs(){
        try {
            val args: ProfileFragmentArgs by navArgs()
            user= args.user
        }catch (e: Exception){

        }

    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater).apply {
            lifecycleOwner= viewLifecycleOwner
    }

    override fun init() {
        shareViewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    user = state.data

                }
                is State.Error -> {
                }
            }
        }
        binding.userItem = user
    }

}