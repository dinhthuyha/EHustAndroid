package com.prdcv.ehust.ui.news

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.baoyz.widget.PullRefreshLayout
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentNewsBinding
import com.prdcv.ehust.model.News
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragmentWithBinding<FragmentNewsBinding>() {
    private val testAdapter = NewsAdapter(
        clickListener = ::navigateToDetailNews
    )


    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentNewsBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = shareViewModel
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
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.postDelayed(object : Runnable{
                override fun run() {
                    shareViewModel.getNews()
                }
            },500)
        }

        shareViewModel.newsState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    shareViewModel.loadingVisibility.set(View.VISIBLE)
                }
                is State.Success -> {
                    binding.swipeRefreshLayout.setRefreshing(false)
                    testAdapter.setItems(it.data)
                    shareViewModel.loadingVisibility.set(View.GONE)

                }
                is State.Error -> {

                }
            }
        }
    }

}