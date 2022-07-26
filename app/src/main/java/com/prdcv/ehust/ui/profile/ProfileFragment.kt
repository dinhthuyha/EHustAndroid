package com.prdcv.ehust.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.data.model.User
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.utils.SharedPreferencesKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var user: User? = null
    private var navigateFromAdmin : Boolean = false
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
        val navController = findNavController()
        val enableLogout = shareViewModel.user?.id == user?.id
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ProfileCard(user, navigateFromAdmin, enableLogout, doLogout = {
                    try {
                        navController.navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
                    } catch (e: Exception) {
                        navController.navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
                    }
                    sharedPreferences.edit().remove(SharedPreferencesKey.TOKEN).commit()
                })
            }
        }
    }

    private fun initArgs() {
        try {
            user = shareViewModel.user
            val args: ProfileFragmentArgs by navArgs()
            user = args.user
            navigateFromAdmin = args.navigateFromAdmin
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}