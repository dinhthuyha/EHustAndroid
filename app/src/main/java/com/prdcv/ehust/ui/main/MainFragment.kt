@file:Suppress("DEPRECATION")

package com.prdcv.ehust.ui.main

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.FragmentStatePagerAdapter.*
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragmentWithBinding<MainFragmentBinding>() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPagerAdapterAdapter: MainViewPagerAdapter

    override fun getViewBinding(inflater: LayoutInflater): MainFragmentBinding =
        MainFragmentBinding.inflate(inflater)

    override fun init() {
        viewPager = binding.viewPager
        viewPager.offscreenPageLimit = 4
        bottomNavigationView = binding.bottomNavigationView
        viewPagerAdapterAdapter = MainViewPagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = viewPagerAdapterAdapter
        setPageSelect()
        setItemSelect()

        shareViewModel.getListStudentInClass()
        shareViewModel.findAllSchedules()
    }

    private fun setItemSelect() {
        bottomNavigationView.setOnItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home -> viewPager.currentItem = 0
                    R.id.search -> viewPager.currentItem = 1
                    R.id.profile -> viewPager.currentItem = 2
                }
                return false
            }
        })
    }

  private  fun setPageSelect() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.menu.findItem(R.id.home).isChecked = true
                    1 -> bottomNavigationView.menu.findItem(R.id.search).isChecked = true
                    2 -> bottomNavigationView.menu.findItem(R.id.profile).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


}