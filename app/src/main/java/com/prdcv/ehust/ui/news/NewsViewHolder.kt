package com.prdcv.ehust.ui.news

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemNewsBinding
import com.prdcv.ehust.model.News


class NewsViewHolder(
    binding: ViewDataBinding,
    private val clickListener: (News) -> Unit
) : BaseViewHolder<News, ViewDataBinding>(binding) {

    override fun bind(itemData: News) {
        super.bind(itemData)
        if (binding is ItemNewsBinding){
            binding.item = itemData
           //set click listener o day

        }
    }

    override fun onItemClickListener(itemData: News) {
        clickListener(itemData)
    }
}