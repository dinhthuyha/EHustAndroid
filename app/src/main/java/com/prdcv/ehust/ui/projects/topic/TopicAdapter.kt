package com.prdcv.ehust.ui.projects.topic

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.news.NewsViewHolder

class TopicAdapter(
) : BaseRecyclerAdapter<Topic, BaseViewHolder<Topic, ViewDataBinding>>(ItemDiffUtilCallback()) {

    override fun getItemLayoutResource(viewType: Int) = R.layout.item_swipe

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Topic, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return TopicViewHolder(viewDataBinding)
    }


    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<Topic>(){
        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            oldItem == newItem

    }


}