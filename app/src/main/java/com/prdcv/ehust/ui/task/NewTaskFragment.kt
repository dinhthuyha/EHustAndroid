package com.prdcv.ehust.ui.task

import android.view.LayoutInflater
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : BaseFragmentWithBinding<FragmentNewTaskBinding>() {
    override fun getViewBinding(inflater: LayoutInflater)=  FragmentNewTaskBinding.inflate(inflater).apply {

    }

    override fun init() {

    }

}