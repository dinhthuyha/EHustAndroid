package com.prdcv.ehust.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.ui.ShareViewModel

abstract class BaseFragment: Fragment(){
    protected val shareViewModel by activityViewModels<ShareViewModel>()
    protected fun hideActionBar() {
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.hide()
    }

    open fun showActionBar() {
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        android.R.id.home ->{
            findNavController().popBackStack()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }
}