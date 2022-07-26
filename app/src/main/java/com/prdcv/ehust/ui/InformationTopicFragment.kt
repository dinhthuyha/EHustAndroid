package com.prdcv.ehust.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationTopicFragment: BaseFragment() {
    private var idTopic: Int? = null
    private val topicsViewModel: TopicsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArg()
    }
    private fun initArg(){
        val args: InformationTopicFragmentArgs by navArgs()
        idTopic = args.idTopic
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
               InformationTopic(idTopic?:0, topicsViewModel)
            }
        }
    }
}