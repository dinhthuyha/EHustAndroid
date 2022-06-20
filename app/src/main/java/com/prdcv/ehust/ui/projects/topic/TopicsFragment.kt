package com.prdcv.ehust.ui.projects.topic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragment

import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.viewmodel.ProjectsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TopicsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopicsFragment : BaseFragment() {

    private val topicViewModel: ProjectsViewModel by activityViewModels()
    private var topics = listOf<Topic>(
        Topic(name = "lập trình web bán hàng online"),
        Topic(name = "Xây dụng app music")
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {

                setContent { DefaultPreview(topicViewModel, shareViewModel.user?.id ?: 0, shareViewModel.user?.roleId!!, findNavController()) }



        }
    }




}