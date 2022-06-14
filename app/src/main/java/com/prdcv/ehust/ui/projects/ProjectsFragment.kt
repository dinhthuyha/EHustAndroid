package com.prdcv.ehust.ui.projects


import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentProjectGraduateBinding
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.viewmodel.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProjectsFragment : BaseFragmentWithBinding<FragmentProjectGraduateBinding>() {
    private val projectsAdapter = ProjectsAdapter(
        clickListener = ::navigateToTopic
    )
    private val topicViewModel: ProjectsViewModel by activityViewModels()

    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentProjectGraduateBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            rvProjectGraduate.adapter = projectsAdapter
        }

    override fun init() {
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

    private fun navigateToTopic(newsItem: ClassStudent) {
        //TH dang dang nhap vs id gv
       when(shareViewModel.user?.roleId){
           Role.ROLE_TEACHER -> {
               findNavController().navigate(ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment())
               topicViewModel.findTopicByIdTeacherAndIdProject(shareViewModel.user?.id!!, newsItem.codeCourse )
           }
           Role.ROLE_STUDENT -> {
               if (!newsItem.nameTeacher.toString().isNullOrEmpty()){
                   findNavController().navigate(ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment())
               }
           }
           else -> {}
       }

    }



}