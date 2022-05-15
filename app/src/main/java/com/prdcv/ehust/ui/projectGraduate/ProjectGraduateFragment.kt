package com.prdcv.ehust.ui.projectGraduate


import android.view.LayoutInflater

import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentProjectGraduateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectGraduateFragment : BaseFragmentWithBinding<FragmentProjectGraduateBinding>() {
    override fun getViewBinding(inflater: LayoutInflater)= FragmentProjectGraduateBinding.inflate(inflater).apply {

    }

    override fun init() {

    }

}