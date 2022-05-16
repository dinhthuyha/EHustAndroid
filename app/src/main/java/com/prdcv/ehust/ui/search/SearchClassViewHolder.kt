package com.prdcv.ehust.ui.search

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemSearchClassBinding
import com.prdcv.ehust.model.ClassStudent


class SearchClassViewHolder(
    binding: ViewDataBinding,
    private val clickListener: (ItemSearch) -> Unit
) : BaseViewHolder<ItemSearch, ViewDataBinding>(binding){
    override fun bind(itemData: ItemSearch) {
        super.bind(itemData)
        if (binding is ItemSearchClassBinding){
            binding.item = itemData as ClassStudent
        }
    }

    override fun onItemClickListener(itemData: ItemSearch) {
        clickListener(itemData)
    }
}