package com.prdcv.ehust.viewmodel

import androidx.lifecycle.ViewModel
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.News
import com.prdcv.ehust.repo.UserRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor( private val userRepository: UserRepository) : ViewModel() {
    private var _news= SingleLiveEvent<State<List<News>>>()
    val news get() = _news
    fun findProfileById(id: Int){
        userRepository.getProfileById(id)
    }

}