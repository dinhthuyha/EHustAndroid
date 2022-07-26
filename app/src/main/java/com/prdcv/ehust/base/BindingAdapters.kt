package com.prdcv.ehust.base

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.base.recyclerview.BindAbleAdapter
import com.prdcv.ehust.data.model.Meeting
import com.prdcv.ehust.data.model.SChedule
import com.prdcv.ehust.data.model.ScheduleEvent
import java.time.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["app:adapter", "app:items"], requireAll = false)
fun <T : Any> RecyclerView.setAdapterData(
    itemAdapter: BaseRecyclerAdapter<T, BaseViewHolder<T, ViewDataBinding>>?,
    items: List<T>?
) {
    itemAdapter?.let {
        adapter = it
    }
    (adapter as? BindAbleAdapter<T>)?.setItems(items ?: emptyList())
}

@BindingAdapter("bindTime")
fun bindTime(textVew: TextView, flight: SChedule) {
    val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM")
    if (flight is ScheduleEvent){
        textVew.text = formatter.format(flight.date)
    }
    if(flight is Meeting){
        textVew.text = formatter.format(flight.date)
    }

}

@BindingAdapter("titleEvent")
fun setEventTitle(textVew: TextView, flight: SChedule) {
    if (flight is ScheduleEvent){
        textVew.text = flight.subjectClass.name
    }
    if(flight is Meeting){
        textVew.text = flight.title
    }

}

@BindingAdapter("visible")
fun setVisibility(target: View, visible: Boolean) {
    target.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("bindStartHour")
fun bindStartHour(textVew: TextView, flight: SChedule){
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        if (flight is ScheduleEvent){
            textVew.text = formatter.format(flight.startTime)
        }
        if(flight is Meeting){
            textVew.text = formatter.format(flight.startTime)
        }
}

@BindingAdapter("bindFinishHour")
fun bindFinishedHour(textVew: TextView, flight: SChedule){
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    if (flight is ScheduleEvent){
        textVew.text = formatter.format(flight.finishTime)
    }
    if(flight is Meeting){
        textVew.text = formatter.format(flight.endTime)
    }
}