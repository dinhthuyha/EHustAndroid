package com.prdcv.ehust.base

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.base.recyclerview.BindAbleAdapter
import com.prdcv.ehust.model.ScheduleEvent
import java.time.LocalTime
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
fun bindTime(textVew: TextView, flight: ScheduleEvent) {
    val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM")
    textVew.text = formatter.format(flight.date)
}
@BindingAdapter("visible")
fun setVisibility(target: View, visible: Boolean) {
    target.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("bindHour")
fun bindHour(textVew: TextView, time: LocalTime){
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    textVew.text = formatter.format(time)
}