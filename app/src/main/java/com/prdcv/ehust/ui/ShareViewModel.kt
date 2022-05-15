package com.prdcv.ehust.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private var _user= SingleLiveEvent<State<User>>()
    val user get() = _user
    private var userId =1
    fun findProfileById(){
        viewModelScope.launch {
            userRepository.getProfileById(userId).collect {
                _user.postValue(it)
            }
        }
    }

    private var _token= SingleLiveEvent<State<String>>()
    val token get() = _token

    fun login(id:Int, password:String){
        viewModelScope.launch {
            userId = id
            userRepository.login(id, password).collect {
                _token.postValue(it)
            }
        }

    }
}