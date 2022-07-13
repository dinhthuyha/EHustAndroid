package com.prdcv.ehust.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.UserRepository
import com.prdcv.ehust.viewmodel.state.ProjectsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val projectsScreenState = ProjectsScreenState()
     var user: User? = null
    fun findAllProjectsById() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.findAllProjectsByStudentId(user?.id!!).collect {
                projectsScreenState.addProjectListFromState(it)
            }
        }
    }

    fun onSemesterSelected(semester: Int) {
        projectsScreenState.semesterStatus.value = semester
    }
}