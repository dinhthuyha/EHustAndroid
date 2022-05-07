package com.prdcv.ehust.ui.news

import android.view.LayoutInflater
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentNewsBinding

class NewsFragment : BaseFragmentWithBinding<FragmentNewsBinding>() {
    override fun getViewBinding(inflater: LayoutInflater)= FragmentNewsBinding.inflate(inflater)


    override fun init() {

    }

}