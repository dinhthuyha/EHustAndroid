package com.prdcv.ehust.ui.home

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.CalendarEventItemViewBinding
import com.prdcv.ehust.databinding.ScheduleTodayItemBinding
import com.prdcv.ehust.model.ScheduleEvent

class ScheduleTodayViewHolder(
    binding: ViewDataBinding,
) : BaseViewHolder<ScheduleEvent, ViewDataBinding>(binding) {


    override fun bind(scheduleToday: ScheduleEvent) {
        super.bind(scheduleToday)
        if (binding is ScheduleTodayItemBinding){
           binding.flight = scheduleToday
            binding.itemLineVertical.setBackgroundColor(scheduleToday.color)
        }
    }

    override fun onItemClickListener(itemData: ScheduleEvent) {
    }
}