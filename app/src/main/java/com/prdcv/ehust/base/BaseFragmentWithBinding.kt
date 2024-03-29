package com.prdcv.ehust.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.prdcv.ehust.extension.hideKeyboard

abstract  class BaseFragmentWithBinding<VB: ViewBinding> : BaseFragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        init()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    abstract fun init()
}