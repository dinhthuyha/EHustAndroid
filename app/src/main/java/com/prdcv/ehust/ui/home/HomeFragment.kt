package com.prdcv.ehust

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.HomeFragmentBinding
import com.prdcv.ehust.ui.home.ScheduleTodayAdapter
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.schedule.ScheduleEventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragmentWithBinding<HomeFragmentBinding>() {
    private val scheduleTodayAdapter = ScheduleTodayAdapter()
    companion object {
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater): HomeFragmentBinding =
        HomeFragmentBinding.inflate(inflater).apply { rvScheduleToday.adapter = scheduleTodayAdapter }

    override fun init() {
//        binding.name.text =
//            "Trong cuộc họp báo trước Madrid Open, tay vợt số một thế giới Novak Djokovic một lần nữa nhấn mạnh sự hà khắc mà Wimbledon đưa ra với các tay vợt Nga, Belarus. Anh nói: Tôi nghĩ quyết định đó là sai lầm. Chúng ta không thể chống lại các cá nhân đơn lẻ, vì những vấn đề tầm vóc quốc gia. Điều mà các tay vợt muốn chỉ đơn giản là thi đấu. Họ được điều chỉnh bởi luật lệ của ATP, WTA, ITF và không làm gì sai trong trường hợp này"
//        binding.name.isSelected = true
        binding.cdClassStudent.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_studentsFragment)
        }
        binding.cdProject.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
        }
        binding.tbHome.iconRightId.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
        }
        binding.cdSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_scheduleFragment)
        }
        shareViewModel.schedulesState.observe(viewLifecycleOwner){
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    if (shareViewModel.getScheduleToday(it.data).isEmpty())
                        Toast.makeText(context,"Hôm nay bạn không có lịch học trên trường", Toast.LENGTH_LONG).show()
                    scheduleTodayAdapter.setItems(shareViewModel.getScheduleToday(it.data))
                    Log.d(TAG, "success: ${it.data.size}")

                }
                is State.Error -> {
                    Log.d(TAG, "error: ${it.exception} ")
                }
            }
        }
    }
}