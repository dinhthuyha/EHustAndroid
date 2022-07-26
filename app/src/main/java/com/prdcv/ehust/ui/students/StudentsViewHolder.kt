package com.prdcv.ehust.ui.students

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemTeacherStudentBinding
import com.prdcv.ehust.data.model.User

class StudentsViewHolder(
    binding: ViewDataBinding,
    private val clickListener: (User) -> Unit
) : BaseViewHolder<User, ViewDataBinding>(binding){
    override fun bind(itemData: User) {
        super.bind(itemData)
        if (binding is ItemTeacherStudentBinding){
            binding.item = itemData
        }
    }

    override fun onItemClickListener(itemData: User) {
        clickListener(itemData)
    }
}