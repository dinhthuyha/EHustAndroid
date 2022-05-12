package com.prdcv.ehust.ui.news

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.News
import com.prdcv.ehust.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val newsRepository: NewsRepository): ViewModel() {
    private var _news= SingleLiveEvent<State<List<News>>>()
    val news get() = _news
    var loadingVisibility = ObservableInt()

    fun setup(){
        loadingVisibility.set(View.VISIBLE)
    }

    fun getNews(){
        viewModelScope.launch {
            newsRepository.getNews().collect {
                _news.postValue(it)
                if (it is State.Success){
                    loadingVisibility.set(View.GONE)
                }
            }
        }

    }
}