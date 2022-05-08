package com.prdcv.ehust.ui.search


import android.view.LayoutInflater

import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.SeachFragmentBinding

class SearchFragment : BaseFragmentWithBinding<SeachFragmentBinding>(){

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun getViewBinding(inflater: LayoutInflater)= SeachFragmentBinding.inflate(inflater).apply {

    }

    override fun init() {

    }

}