package com.prdcv.ehust.ui.schedule

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.CalendarEventItemViewBinding
import com.prdcv.ehust.model.ScheduleEvent
import java.time.format.DateTimeFormatter

class ScheduleEventsViewHolder(
     binding: ViewDataBinding,
    private val clickListener: (ScheduleEvent) -> Unit
) : BaseViewHolder<ScheduleEvent, ViewDataBinding>(binding) {


    override fun bind(flight: ScheduleEvent) {
        super.bind(flight)
        if (binding is CalendarEventItemViewBinding){
            binding.itemFlightDateText.apply {
                binding.flight = flight
            }
            binding.itemLineVertical.setBackgroundColor(flight.color)
        }
    }

    override fun onItemClickListener(itemData: ScheduleEvent) {
        clickListener(itemData)
    }
}