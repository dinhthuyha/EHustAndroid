package com.prdcv.ehust.ui.students

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.FragmentStudentsBinding
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.news.NewsAdapter
import com.prdcv.ehust.ui.news.NewsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentsFragment : BaseFragmentWithBinding<FragmentStudentsBinding>() {
    private val studentsAdapter = StudentsAdapter(
        clickListener = ::navigateToProfile
    )
    override fun getViewBinding(inflater: LayoutInflater)= FragmentStudentsBinding.inflate(inflater).apply {
        lifecycleOwner = viewLifecycleOwner
        rvInformationStudent.adapter = studentsAdapter
    }
    override fun init() {

        shareViewModel.listUser.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    studentsAdapter.setItems(it.data)

                }
                is State.Error -> {

                }
            }
        }
    }

    private fun navigateToProfile(newsItem: User) {
        findNavController().navigate(
            StudentsFragmentDirections.actionStudentsFragmentToProfileFragment(
                newsItem
            )
        )

    }
}