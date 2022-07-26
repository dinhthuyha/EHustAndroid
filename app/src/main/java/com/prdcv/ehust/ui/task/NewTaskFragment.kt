package com.prdcv.ehust.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.data.model.Topic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : BaseFragment() {
    private var topic: Topic? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArg()
    }
    private fun initArg(){
        val args: NewTaskFragmentArgs by navArgs()
        topic = args.topic
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                topic?.let { TaskScreenPreview(findNavController(), it) }
            }
        }
    }

}