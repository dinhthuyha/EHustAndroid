package com.prdcv.ehust.base

import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.base.recyclerview.BindAbleAdapter

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