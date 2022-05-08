package com.prdcv.ehust.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.repo.EhustRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(val ehustRepository: EhustRepository):ViewModel() {
    private var _token= SingleLiveEvent<State<String>>()
    val token get() = _token

    fun login(id:Int, password:String){
         viewModelScope.launch {
             ehustRepository.login(id, password).collect {
                 _token.postValue(it)
             }
         }

    }
}