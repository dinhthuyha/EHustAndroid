package com.prdcv.ehust.ui.news

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.News

class NewsAdapter(
    private val clickListener: (News) -> Unit
) : BaseRecyclerAdapter<News, BaseViewHolder<News, ViewDataBinding>>(ItemDiffUtilCallback()) {

    override fun getItemLayoutResource(viewType: Int) = R.layout.item_news

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<News, ViewDataBinding> {
        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return NewsViewHolder(viewDataBinding, clickListener)
    }


    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<News>(){
        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
            oldItem == newItem

    }
}