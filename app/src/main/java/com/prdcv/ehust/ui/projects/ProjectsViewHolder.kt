package com.prdcv.ehust.ui.projects

import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemProjectGraduateBinding
import com.prdcv.ehust.model.ClassStudent

class ProjectsViewHolder(
    binding: ViewDataBinding,
    private val clickListener: (ClassStudent) -> Unit
) : BaseViewHolder<ClassStudent, ViewDataBinding>(binding){
    override fun bind(itemData: ClassStudent) {
        super.bind(itemData)
        if (binding is ItemProjectGraduateBinding){
            binding.item = itemData
        }
    }

    override fun onItemClickListener(itemData: ClassStudent) {
        clickListener(itemData)
    }
}