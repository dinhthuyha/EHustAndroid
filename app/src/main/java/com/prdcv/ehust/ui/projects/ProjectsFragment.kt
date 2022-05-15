package com.prdcv.ehust.ui.projects


import android.view.LayoutInflater
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentProjectGraduateBinding
import com.prdcv.ehust.model.ClassStudent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProjectsFragment : BaseFragmentWithBinding<FragmentProjectGraduateBinding>() {
    private val projectsAdapter = ProjectsAdapter(
        clickListener = ::navigateToProfile
    )

    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentProjectGraduateBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            rvProjectGraduate.adapter = projectsAdapter
        }

    override fun init() {
        shareViewModel.findAllProjectsByStudentId()
        shareViewModel.projectsState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    projectsAdapter.setItems(it.data)
                }
                is State.Error -> {

                }
            }
        }
    }

    private fun navigateToProfile(newsItem: ClassStudent) {


    }

}