package com.prdcv.ehust.ui.news.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentDetailNewsBinding
import com.prdcv.ehust.model.News
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailNewsFragment : BaseFragmentWithBinding<FragmentDetailNewsBinding>() {
    private var newsItem: News?=null
    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentDetailNewsBinding.inflate(inflater).apply {
            lifecycleOwner= viewLifecycleOwner
            news= newsItem
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }
    private fun initArgs() {
        val args: DetailNewsFragmentArgs by navArgs()
        newsItem = args.itemNews

    }

    override fun init() {

    }

}