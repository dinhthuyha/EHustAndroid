package com.prdcv.ehust.ui.projects.topic

import android.content.ClipData
import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.daimajia.swipe.SwipeLayout
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.databinding.ItemSwipeBinding
import com.prdcv.ehust.model.Topic

class TopicViewHolder(
    binding: ViewDataBinding,
) : BaseViewHolder<Topic,ViewDataBinding>(binding){

    private  val TAG = "TopicViewHolder"
    override fun bind(itemData: Topic) {
        super.bind(itemData)
        if (binding is ItemSwipeBinding){
            binding.swipe.showMode = SwipeLayout.ShowMode.PullOut
            binding.swipe.addDrag(SwipeLayout.DragEdge.Right, binding.bottomWrapper)
            binding.topic = itemData
        }
    }

    init {

        if (binding is ItemSwipeBinding){

            binding.tvAccept.setOnClickListener {
                Log.d(TAG, " accept")
            }
            binding.tvRequest.setOnClickListener {
                Log.d(TAG, "requét ")
            }
        }

    }
}