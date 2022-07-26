package com.prdcv.ehust.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
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