package com.prdcv.ehust.ui.schedule

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentScheduleBinding


class ScheduleFragment : BaseFragmentWithBinding<FragmentScheduleBinding>()  {
    private val eventsAdapter = ScheduleEventAdapter {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialog_delete_confirmation)
            .setPositiveButton("Delete") { _, _ ->
               // deleteEvent(it)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun getViewBinding(inflater: LayoutInflater)= FragmentScheduleBinding.inflate(inflater).apply {

    }

    override fun init() {

    }


}