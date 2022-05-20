package com.prdcv.ehust.ui.schedule

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.EventSchedule


class ScheduleEventAdapter(
    private val clickListener: (EventSchedule) -> Unit
) : BaseRecyclerAdapter<EventSchedule, BaseViewHolder<EventSchedule, ViewDataBinding>>(ItemDiffUtilCallback()){

    override fun getItemLayoutResource(viewType: Int) = R.layout.calendar_event_item_view

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<EventSchedule, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return ScheduleEventsViewHolder(viewDataBinding, clickListener)
    }
    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<EventSchedule>(){
        override fun areContentsTheSame(oldItem: EventSchedule, newItem: EventSchedule): Boolean =
            oldItem == newItem

    }
}