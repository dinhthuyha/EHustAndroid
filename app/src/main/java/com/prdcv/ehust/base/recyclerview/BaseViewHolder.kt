package com.prdcv.ehust.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : RecyclerViewItem, VB : ViewDataBinding>(
    protected val binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    protected var itemData: T? = null
        private set

    init {
        itemView.setOnClickListener {
            itemData?.let(::onItemClickListener)
        }
    }

    open fun bind(itemData: T) {
        this.itemData = itemData
    }

    open fun onItemClickListener(itemData: T) {
    }
}
