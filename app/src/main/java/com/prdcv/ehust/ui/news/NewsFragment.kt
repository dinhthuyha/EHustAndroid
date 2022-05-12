package com.prdcv.ehust.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentNewsBinding
import com.prdcv.ehust.model.News
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragmentWithBinding<FragmentNewsBinding>() {
    private val newsViewModel: NewsViewModel by activityViewModels()
    private val testAdapter = NewsAdapter(
        clickListener = ::navigateToDetailNews
    )


    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentNewsBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = newsViewModel
            rvNews.adapter = testAdapter
        }

    private fun navigateToDetailNews(newsItem: News) {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToDetailNewsFragment(
                newsItem
            )
        )

    }

    override fun init() {
        newsViewModel.getNews()
        newsViewModel.setup()
        newsViewModel.news.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    testAdapter.setItems(it.data)

                }
                is State.Error -> {

                }
            }
        }
    }

}