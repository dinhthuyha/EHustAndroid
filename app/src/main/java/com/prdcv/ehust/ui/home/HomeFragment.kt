package com.prdcv.ehust

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.HomeFragmentBinding
import com.prdcv.ehust.ui.home.ScheduleTodayAdapter
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.task.TaskRow
import com.prdcv.ehust.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragmentWithBinding<HomeFragmentBinding>() {
    private val scheduleTodayAdapter = ScheduleTodayAdapter()
    val taskViewModel:TaskViewModel by viewModels()
    companion object {
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater): HomeFragmentBinding =
        HomeFragmentBinding.inflate(inflater).apply {
            user = shareViewModel.user
           rvScheduleToday.adapter = scheduleTodayAdapter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.findAllTaskWillExpire()
    }
    @OptIn(ExperimentalFoundationApi::class)
    override fun init() {
        binding.viewStudent.composeTask.setContent {
            val taskViewModel: TaskViewModel = hiltViewModel()
            LaunchedEffect(key1 = Unit) {

            }
            val uiState = taskViewModel.uiState
            Column(modifier = Modifier.wrapContentHeight()) {
                uiState.filteredTaskList.forEach { item ->
                    TaskRow(
                        data = item,
                        modifier = Modifier
                            .clickable {
                                findNavController().navigate(
                                    MainFragmentDirections.actionMainFragmentToDetailTaskFragment(
                                        item.id
                                    )
                                )
                            }
                    )
                }
            }
        }
        binding.viewStudent.cdClassStudent.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_studentsFragment)
        }
        binding.viewStudent.cdProject.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
        binding.tbHome.iconRightId.setOnClickListener {
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

        binding.viewTeacher.cdSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_scheduleFragment)
        }
        binding.viewTeacher.cdNews.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
        }
        binding.viewTeacher.cdProject.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
    }
}