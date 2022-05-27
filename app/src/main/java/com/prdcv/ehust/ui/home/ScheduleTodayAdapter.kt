package com.prdcv.ehust.ui.home

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.ui.schedule.ScheduleEventsViewHolder
import java.time.format.DateTimeFormatter

class ScheduleTodayAdapter() :
    BaseRecyclerAdapter<ScheduleEvent, BaseViewHolder<ScheduleEvent, ViewDataBinding>>(ItemDiffUtilCallback()){

    override fun getItemLayoutResource(viewType: Int) = R.layout.schedule_today_item

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ScheduleEvent, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return ScheduleTodayViewHolder(viewDataBinding)
    }
    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<ScheduleEvent>(){
        override fun areContentsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent): Boolean =
            oldItem == newItem

    }
}