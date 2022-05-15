package com.prdcv.ehust.ui

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.NewsRepository
import com.prdcv.ehust.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val newsRepository: NewsRepository
): ViewModel() {
    private var _profileState= SingleLiveEvent<State<User>>()
    val profileState get() = _profileState

    private var _listUser= SingleLiveEvent<State<List<User>>>()
    val listUser get() = _listUser
    var loadingVisibility = ObservableInt()

    private var _newsState= SingleLiveEvent<State<List<News>>>()
    val newsState get() = _newsState

    var user: User?= null

    private var _token= SingleLiveEvent<State<String>>()
    val token get() = _token

    private var _projectsState = SingleLiveEvent<State<List<ClassStudent>>>()
    val projectsState get() = _projectsState

      fun findProfileById(){
        viewModelScope.launch {
            userRepository.getProfileById(user?.id!!).collect {
                _profileState.postValue(it)
            }
        }
    }



    fun login(id:Int, password:String){
        viewModelScope.launch {
            userRepository.login(id, password).collect {
                _token.postValue(it)
            }
        }
    }

    fun decodeToken(token: String){
        val jwt = JWT(token)
        val grade = jwt.claims["grade"]?.asString()
        val roleId = jwt.claims["role_id"]?.asInt()
        val id = jwt.claims["id"]?.asInt()
        user = User( id = id!!,
        grade = grade,
        roleId = roleId!!,
        )
    }

   fun getListStudentInClass(){
        viewModelScope.launch {
            userRepository.getListStudentInClass(user?.grade!!).collect {
                _listUser.postValue(it)
                if (it is State.Success){
                    loadingVisibility.set(View.GONE)
                }
            }
        }
    }

    fun setup(){
        loadingVisibility.set(View.VISIBLE)
    }

    fun getNews(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                newsRepository.getNews().collect {
                    _newsState.postValue(it)

                }
            }

        }
    }

    fun findAllProjectsByStudentId(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                userRepository.findAllProjectsByStudentId(user?.id!!).collect {
                    _projectsState.postValue(it)
                }
            }

        }
    }
}