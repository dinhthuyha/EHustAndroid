package com.prdcv.ehust.ui.schedule

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.CalendarEventItemViewBinding
import com.prdcv.ehust.model.EventSchedule

class ScheduleEventsViewHolder(
     binding: ViewDataBinding,
    private val clickListener: (EventSchedule) -> Unit
) : BaseViewHolder<EventSchedule, ViewDataBinding>(binding) {
    override fun bind(itemData: EventSchedule) {
        super.bind(itemData)
        if (binding is CalendarEventItemViewBinding){
            binding.itemEventText.text = itemData.text
            //set click listener o day

        }
    }

    override fun onItemClickListener(itemData: EventSchedule) {
        clickListener(itemData)
    }
}