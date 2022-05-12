package com.prdcv.ehust.base.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseRecyclerAdapter<T : Any, VH : BaseViewHolder<T, ViewDataBinding>>(
    diffUtilCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffUtilCallback), BindAbleAdapter<T> {

    @LayoutRes
    abstract fun getItemLayoutResource(@RecyclerType viewType: Int): Int

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<T>?) {
        super.submitList(list ?: emptyList())
    }

    override fun setItems(items: List<T>) {
        submitList(items.toMutableList())
    }


    protected fun getViewHolderDataBinding(parent: ViewGroup, viewType: Int): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getItemLayoutResource(viewType),
            parent,
            false
        )

    protected fun inflateView(parent: ViewGroup, @LayoutRes layoutResId: Int): View =
        LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
}
