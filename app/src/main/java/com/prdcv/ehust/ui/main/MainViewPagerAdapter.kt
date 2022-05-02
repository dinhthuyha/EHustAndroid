package com.prdcv.ehust.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.prdcv.ehust.HomeFragment
import com.prdcv.ehust.ui.profile.ProfileFragment
import com.prdcv.ehust.ui.sreach.SeachFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager, behavior: Int):FragmentPagerAdapter(fragmentManager,behavior) {
    override fun getCount(): Int {
        return  3
    }

    override fun getItem(position: Int): Fragment {
        when (position ){
            0->return HomeFragment.newInstance()
            1->return SeachFragment.newInstance()
            2->return ProfileFragment.newInstance()
        }
        return HomeFragment.newInstance()
    }
}