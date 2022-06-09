package com.prdcv.ehust.ui.projects.topic

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.swipe.SwipeLayout
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentTopicsBinding
import com.prdcv.ehust.model.Topic

/**
 * A simple [Fragment] subclass.
 * Use the [TopicsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopicsFragment : BaseFragmentWithBinding<FragmentTopicsBinding>() {
    private var topicAdapter = TopicAdapter()
    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentTopicsBinding.inflate(inflater).apply {
            rv.adapter = topicAdapter

        }

    override fun init() {

        var list = mutableListOf<Topic>()
        list.add(Topic("item 11"))
        list.add(Topic("item 2"))
        topicAdapter.setItems(list)


    }


}