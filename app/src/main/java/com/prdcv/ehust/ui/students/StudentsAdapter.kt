package com.prdcv.ehust.ui.students

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.User

class StudentsAdapter(
    private val clickListener: (User) -> Unit
) : BaseRecyclerAdapter<User, BaseViewHolder<User, ViewDataBinding>>(ItemDiffUtilCallback()){

    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<User>(){
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

    }
    override fun getItemLayoutResource(viewType: Int) = R.layout.item_teacher_student

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<User, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return StudentsViewHolder(viewDataBinding, clickListener)
    }
}