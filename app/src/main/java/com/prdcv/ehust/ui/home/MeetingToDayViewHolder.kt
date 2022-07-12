package com.prdcv.ehust.ui.home

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.MeetingTodayItemBinding
import com.prdcv.ehust.databinding.ScheduleTodayItemBinding
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.ScheduleEvent

class MeetingToDayViewHolder(
    binding: ViewDataBinding,
) : BaseViewHolder<Meeting, ViewDataBinding>(binding) {


    override fun bind(scheduleToday: Meeting) {
        super.bind(scheduleToday)
        if (binding is MeetingTodayItemBinding){
            binding.flight = scheduleToday


        }
    }

    override fun onItemClickListener(itemData: Meeting) {
    }
}