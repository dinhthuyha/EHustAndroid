package com.prdcv.ehust.ui.search

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemTeacherStudentBinding
import com.prdcv.ehust.model.User

class SearchUserViewHolder(
    binding: ViewDataBinding,
    private val clickListener: (ItemSearch) -> Unit
) : BaseViewHolder<ItemSearch, ViewDataBinding>(binding){
    override fun bind(itemData: ItemSearch) {
        super.bind(itemData)
        if (binding is ItemTeacherStudentBinding){
            binding.item = itemData as User
        }
    }

    override fun onItemClickListener(itemData: ItemSearch) {
        clickListener(itemData)
    }
}