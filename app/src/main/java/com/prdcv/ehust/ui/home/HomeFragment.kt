package com.prdcv.ehust

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.utils.extension.toLocalDate
import com.prdcv.ehust.utils.extension.toLocalTime
import com.prdcv.ehust.data.model.Meeting
import com.prdcv.ehust.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var meeting: Meeting? = null

    companion object {
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_schedule)
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as TextView
        val timeFrom = dialog.findViewById(R.id.timeFrom) as TextView
        val timeTo = dialog.findViewById(R.id.timeTo) as TextView
        val dateFrom = dialog.findViewById(R.id.txtDate) as TextView
        val txtSV = dialog.findViewById(R.id.txtSv) as EditText
        val title = dialog.findViewById(R.id.edtId) as EditText
        dateFrom.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        mDateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month = month + 1
                Log.d(TAG, "onDateSet: mm/dd/yyy: $month/$day/$year")
                val date = "$year-$month-$day"
                val selectedDate = LocalDate.of(year, month + 1, day)
                dateFrom.text = selectedDate.format(DateTimeFormatter.ISO_DATE)
            }
        timeFrom.setOnClickListener {
            // Custom text and color
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setPrefix(R.string.time_prefix)
                setSuffix(R.string.time_suffix)
                setThemeColor(R.color.colorAccent)
                setTitleColor(R.color.colorWhite)
                setNegativeButtonColor(android.R.color.holo_red_dark)
                setPositiveButtonColor(android.R.color.holo_blue_bright)
                setButtonTextAllCaps(false)
            }.build().apply {
                setListener { hour, minute -> onTimePicked(hour, minute, timeFrom) }
            }.show(requireActivity().supportFragmentManager, SnapTimePickerDialog.TAG)
        }

        timeTo.setOnClickListener { // Custom text and color
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setPrefix(R.string.time_prefix)
                setSuffix(R.string.time_suffix)
                setThemeColor(R.color.colorAccent)
                setTitleColor(R.color.colorWhite)
                setNegativeButtonColor(android.R.color.holo_red_dark)
                setPositiveButtonColor(android.R.color.holo_blue_bright)
                setButtonTextAllCaps(false)
            }.build().apply {
                setListener { hour, minute -> onTimePicked(hour, minute, timeTo) }
            }.show(requireActivity().supportFragmentManager, SnapTimePickerDialog.TAG)
        }
        btnSave.setOnClickListener {
            if (shareViewModel.user?.id != null
                && txtSV.text.isNotEmpty()
                && title.text.isNotEmpty()
                && dateFrom.text.isNotEmpty()
                && timeFrom.text.isNotEmpty()
                && timeTo.text.isNotEmpty()){
                meeting = Meeting(
                    idUserTeacher = shareViewModel.user?.id!!,
                    nameStudent = txtSV.text.toString(),
                    title = title.text.toString(),
                    date = dateFrom.text.toString().toLocalDate(),
                    startTime = timeFrom.text.toString().toLocalTime(),
                    endTime = timeTo.text.toString().toLocalTime()
                )
                shareViewModel.postMeeting(meeting!!)
                dialog.dismiss()
            }else{
                Toast.makeText(requireContext(),"Xin hãy điền đầy đủ thống tin vào các trường", Toast.LENGTH_SHORT).show()
            }

        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun onTimePicked(selectedHour: Int, selectedMinute: Int, textView: TextView) {
        val hour = selectedHour.toString().padStart(2, '0')
        val minute = selectedMinute.toString().padStart(2, '0')
        textView.text = String.format(getString(R.string.selected_time_format, hour, minute))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HomeScreen(shareViewModel.user?.roleId!!, findNavController(),shareViewModel, callback = {showDialog()})
            }
        }
    }



}