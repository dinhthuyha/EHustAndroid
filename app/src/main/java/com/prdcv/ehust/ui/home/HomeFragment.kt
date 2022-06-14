package com.prdcv.ehust

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.HomeFragmentBinding
import com.prdcv.ehust.ui.home.ScheduleTodayAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragmentWithBinding<HomeFragmentBinding>() {
    private val scheduleTodayAdapter = ScheduleTodayAdapter()
    companion object {
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater): HomeFragmentBinding =
        HomeFragmentBinding.inflate(inflater).apply {
            user = shareViewModel.user
           rvScheduleToday.adapter = scheduleTodayAdapter }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun init() {
        binding.viewStudent.cdClassStudent.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_studentsFragment)
        }
        binding.viewStudent.cdProject.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
        binding.tbHome.iconRightId.setOnClickListener {
            shareViewModel.getNews()
            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
        }
        binding.viewStudent.cdSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_scheduleFragment)
        }
        shareViewModel.schedulesState.observe(viewLifecycleOwner){
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    shareViewModel.schedules =it.data
                    if (shareViewModel.getScheduleToday(it.data).isEmpty()){
                        binding.txtNoScheduler.apply {
                            visibility = View.VISIBLE
                            text = "Hôm nay bạn không có lịch học trên trường"
                        }
                    }
                    scheduleTodayAdapter.setItems(shareViewModel.getScheduleToday(it.data))
                    Log.d(TAG, "success: ${it.data.size}")

                }
                is State.Error -> {
                    Log.d(TAG, "error: ${it.exception} ")
                }
            }
        }

        /**
         * teacher event
         */
        binding.viewTeacher.cdProject.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
        binding.viewTeacher.cdSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_scheduleFragment)
        }
        binding.viewTeacher.cdNews.setOnClickListener {
            shareViewModel.getNews()
            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
        }
        binding.viewTeacher.cdProject.setOnClickListener {
            shareViewModel.findAllProjectsById()
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
    }
}