package com.prdcv.ehust.ui.schedule

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.ScheduleEvent
import java.time.format.DateTimeFormatter


class ScheduleEventAdapter(
    private val clickListener: (ScheduleEvent) -> Unit
) : BaseRecyclerAdapter<ScheduleEvent, BaseViewHolder<ScheduleEvent, ViewDataBinding>>(ItemDiffUtilCallback()){
    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")
    override fun getItemLayoutResource(viewType: Int) = R.layout.calendar_event_item_view

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ScheduleEvent, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return ScheduleEventsViewHolder(viewDataBinding, clickListener)
    }
    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<ScheduleEvent>(){
        override fun areContentsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent): Boolean =
            oldItem == newItem

    }
}