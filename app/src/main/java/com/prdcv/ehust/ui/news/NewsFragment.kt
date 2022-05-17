package com.prdcv.ehust.ui.news

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
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
        shareViewModel.getNews()
        shareViewModel.setup()
        shareViewModel.newsState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    testAdapter.setItems(it.data)
                    shareViewModel.loadingVisibility.set(View.GONE)

                }
                is State.Error -> {

                }
            }
        }
    }

}