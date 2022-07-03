package com.prdcv.ehust.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var user: User? = null
    var args: ProfileFragmentArgs? = null
    private val TAG = "ProfileFragment"
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ProfileCard(user = user,findNavController(), sharedPreferences)
            }
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

}