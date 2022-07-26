package com.prdcv.ehust.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.data.model.User
import com.prdcv.ehust.data.repo.SubjectRepository
import com.prdcv.ehust.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    var semester: Int? = null
    val projectsScreenState = ProjectsScreenState()
     var user: User? = null
    fun findAllProjectsById() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.findAllProjectsByStudentId(user?.id!!).collect {
                projectsScreenState.addProjectListFromState(it)
            }
        }
    }

    fun fetchDataProjectScreen() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                async { getAllSemester() }
                async { findAllProjectsById() }
                async {
                    getAllProjectByIdTeacherAndSemester(
                        user?.id?:0,
                        semester?:0
                    )
                }

            }
        }
    }

    fun getAllSemester() {
        viewModelScope.launch {
            subjectRepository.getAllSemester().collect {
                projectsScreenState.getAllSemester(it)


            }
        }
    }


    fun getAllProjectByIdTeacherAndSemester(idTeacher: Int, semester: Int) {
        viewModelScope.launch {
            subjectRepository.getAllProjectByIdTeacherAndSemester(idTeacher, semester).collect {
                projectsScreenState.getAllProjectByIdTeacherAndSemester(it)
            }
        }
    }

    fun onSemesterSelected(semester: Int) {
        projectsScreenState.semesterStatus.value = semester
        getAllProjectByIdTeacherAndSemester(
            user?.id?:0, semester
        )
    }
}