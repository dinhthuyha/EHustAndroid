package com.prdcv.ehust.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.data.model.ClassStudent
import com.prdcv.ehust.data.model.Role
import com.prdcv.ehust.data.model.User
import com.prdcv.ehust.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _classState = SingleLiveEvent<State<ClassStudent>>()
    val classState get() = _classState

    private var _userSearchState = SingleLiveEvent<State<User>>()
    val userSearchState get() = _userSearchState

    fun searchClassById(codeClass: Int){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                userRepository.searchClassById(codeClass).collect {
                    _classState.postValue(it)
                }
            }

        }
    }

    fun searchUserById(id: Int, role: Role){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                userRepository.searchUserById(id, role).collect {
                    _userSearchState.postValue(it)
                }
            }

        }
    }

    fun searchUserByFullName(fullName: String, role: Role){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                userRepository.searchUserByFullName(fullName, role).collect {
                    _userSearchState.postValue(it)
                }
            }

        }
    }
}