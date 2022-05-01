package com.prdcv.ehust.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


abstract  class BaseFragment<VB: ViewDataBinding, VM: ViewModel>  : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        android.R.id.home ->{
            popBackStack()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutView(), container , false)
        setUpToolbar()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel  = ViewModelProvider(this)[getClassViewModel()]
        init()
        super.onViewCreated(view, savedInstanceState)
    }
    open fun popBackStack(){
        findNavController().popBackStack()
    }
    open fun setUpToolbar(){}
    abstract  fun getLayoutView() : Int
    abstract fun getClassViewModel():Class<VM>
    abstract fun init()
}

