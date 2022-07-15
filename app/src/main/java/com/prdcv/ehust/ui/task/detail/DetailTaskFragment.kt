package com.prdcv.ehust.ui.task.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.calendar.CalendarScreen
import com.prdcv.ehust.viewmodel.DetailTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTaskFragment : BaseFragment() {

    private val args: DetailTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val viewModel = hiltViewModel<DetailTaskViewModel>().apply { user = shareViewModel.user }
                viewModel.idTopic = args.arg.idTopic
                viewModel.idTask = args.arg.idTask
                viewModel.isNewTask = args.arg.isNewTask
                DetailTask(
                    viewModel = viewModel,
                    navController = findNavController()
                )
            }
        }
    }
}