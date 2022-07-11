package com.prdcv.ehust.ui.projects


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.viewmodel.TopicsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProjectsFragment : BaseFragment() {
    private val topicsViewModel: TopicsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { ProjectScreen(shareViewModel,topicsViewModel, findNavController()) }
        }
    }
}