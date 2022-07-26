package com.prdcv.ehust.ui.admin

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.utils.extension.hideKeyboard
import com.prdcv.ehust.viewmodel.AssignViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [HomeAdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeAdminFragment : BaseFragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: AssignViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { AdminMainScreen(viewModel,findNavController(), sharedPreferences, hideKeyboard = { hideKeyboard() }) }
        }
    }



}
