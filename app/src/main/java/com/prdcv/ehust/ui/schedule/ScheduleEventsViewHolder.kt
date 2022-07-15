package com.prdcv.ehust.ui.schedule

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.CalendarEventItemViewBinding
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.SChedule
import com.prdcv.ehust.model.ScheduleEvent
import java.time.format.DateTimeFormatter

class ScheduleEventsViewHolder(
     binding: ViewDataBinding,
    private val clickListener: (SChedule) -> Unit
) : BaseViewHolder<SChedule, ViewDataBinding>(binding) {


    override fun bind(flight: SChedule) {
        super.bind(flight)
        if (binding is CalendarEventItemViewBinding){
            binding.itemFlightDateText.apply {
                binding.flight = flight
            }
            if (flight is ScheduleEvent)
                binding.itemLineVertical.setBackgroundColor(flight.color)
            if (flight is Meeting)
                binding.itemLineVertical.setBackgroundColor(flight.color?: Color.BLUE)
        }
    }

    override fun onItemClickListener(itemData: SChedule) {
        clickListener(itemData)
    }
}