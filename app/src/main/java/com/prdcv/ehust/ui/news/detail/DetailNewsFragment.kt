package com.prdcv.ehust.ui.news.detail

import android.view.LayoutInflater
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentDetailNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailNewsFragment : BaseFragmentWithBinding<FragmentDetailNewsBinding>() {
    override fun getViewBinding(inflater: LayoutInflater)= FragmentDetailNewsBinding.inflate(inflater).apply  {

    }

    override fun init() {

    }

}