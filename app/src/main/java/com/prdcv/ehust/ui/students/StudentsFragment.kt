package com.prdcv.ehust.ui.students

import android.view.LayoutInflater
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentStudentsBinding

class StudentsFragment : BaseFragmentWithBinding<FragmentStudentsBinding>() {
    override fun getViewBinding(inflater: LayoutInflater)= FragmentStudentsBinding.inflate(inflater)
    override fun init() {

    }

}