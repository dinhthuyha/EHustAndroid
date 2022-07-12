package com.prdcv.ehust.ui.home

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.ScheduleEvent

class MeetingToDayAdapter:
    BaseRecyclerAdapter<Meeting, BaseViewHolder<Meeting, ViewDataBinding>>(ItemDiffUtilCallback()){

    override fun getItemLayoutResource(viewType: Int) = R.layout.schedule_today_item

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Meeting, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return MeetingToDayViewHolder(viewDataBinding)
    }
    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<Meeting>(){
        override fun areContentsTheSame(oldItem: Meeting, newItem: Meeting): Boolean =
            oldItem == newItem

    }
}