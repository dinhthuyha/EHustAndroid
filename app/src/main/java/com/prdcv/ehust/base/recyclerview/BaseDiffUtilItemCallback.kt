package com.prdcv.ehust.base.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.tr4n.toeic.base.recyclerview.RecyclerViewItem

abstract class BaseDiffUtilItemCallback<T : RecyclerViewItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // Override this if your item have an id
        return oldItem === newItem
    }

    /**
     * 2 ways to make this happened
     *  - Simple way: make your [T] item is data class
     *  - Performance way: make your own [T] item's equals() & hashcode() methods
     */
    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem
}
