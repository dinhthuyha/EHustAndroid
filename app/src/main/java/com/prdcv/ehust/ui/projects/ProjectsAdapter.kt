package com.prdcv.ehust.ui.projects

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.User

class ProjectsAdapter(
    private val clickListener: (ClassStudent) -> Unit
) : BaseRecyclerAdapter<ClassStudent, BaseViewHolder<ClassStudent, ViewDataBinding>>(ItemDiffUtilCallback()){

    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<ClassStudent>(){
        override fun areContentsTheSame(oldItem: ClassStudent, newItem: ClassStudent): Boolean =
            oldItem == newItem

    }
    override fun getItemLayoutResource(viewType: Int) = R.layout.item_project_graduate

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ClassStudent, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return ProjectsViewHolder(viewDataBinding, clickListener)
    }
}