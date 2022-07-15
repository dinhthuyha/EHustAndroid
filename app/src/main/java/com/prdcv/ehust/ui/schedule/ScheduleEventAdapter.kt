package com.prdcv.ehust.ui.schedule

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.SChedule
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.ui.search.SearchClassViewHolder
import com.prdcv.ehust.ui.search.SearchUserViewHolder
import com.prdcv.ehust.ui.search.ViewType
import java.time.format.DateTimeFormatter


class ScheduleEventAdapter(
    private val clickListener: (SChedule) -> Unit
) : BaseRecyclerAdapter<SChedule, BaseViewHolder<SChedule, ViewDataBinding>>(ItemDiffUtilCallback()){
    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")
    override fun getItemLayoutResource(viewType: Int) = R.layout.calendar_event_item_view

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SChedule, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return when(viewType){
            ViewType.MEETING.type -> {
                ScheduleEventsViewHolder(viewDataBinding, clickListener)
            }
            else -> {
                ScheduleEventsViewHolder(viewDataBinding, clickListener)
            }
        }
    }
    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<SChedule>(){
        override fun areContentsTheSame(oldItem: SChedule, newItem: SChedule): Boolean =
            oldItem == newItem

    }

    override fun getItemViewType(position: Int): Int {
        return when(currentList[position] as? ScheduleEvent){
            null -> ViewType.MEETING.type
            else ->ViewType.STUDY.type
        }
    }

    enum class ViewType(val type: Int){
        MEETING(0),
        STUDY(1)
    }
}
