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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.HomeFragmentBinding
import com.prdcv.ehust.extension.toLocalDate
import com.prdcv.ehust.extension.toLocalTime
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.ui.home.HomeScreen
import com.prdcv.ehust.ui.home.MeetingToDayAdapter
import com.prdcv.ehust.ui.home.ScheduleTodayAdapter
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.task.TaskRow
import com.prdcv.ehust.ui.task.TaskScreenPreview
import com.prdcv.ehust.ui.task.detail.TaskDetailArgs
import com.prdcv.ehust.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.bouncycastle.asn1.x500.style.RFC4519Style.title
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val scheduleTodayAdapter = ScheduleTodayAdapter()
    private val meetingTodayAdapter = MeetingToDayAdapter()
    val taskViewModel: TaskViewModel by viewModels()
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
        val title = dialog.findViewById(R.id.event) as EditText
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
                val taskViewModel: TaskViewModel = hiltViewModel()
                HomeScreen(shareViewModel.user?.roleId!!, findNavController(),taskViewModel,shareViewModel, callback = {showDialog()})
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.findAllTaskWillExpire()
    }


//    override fun init() {
//        shareViewModel.meetings.observe(viewLifecycleOwner) {
//            when (it) {
//                is State.Success -> {
//                  //  meetingTodayAdapter.setItems(it.data)
//                }
//                else -> {}
//            }
//        }
//        binding.viewStudent.composeTask.setContent {
//            val taskViewModel: TaskViewModel = hiltViewModel()
//            LaunchedEffect(key1 = Unit) {
//
//            }
//            val uiState = taskViewModel.uiState
//            Column(modifier = Modifier.wrapContentHeight()) {
//                uiState.filteredTaskList.forEach { item ->
//                    TaskRow(
//                        data = item,
//                        modifier = Modifier
//                            .clickable {
//                                findNavController().navigate(
//                                    MainFragmentDirections.actionMainFragmentToDetailTaskFragment(
//                                        TaskDetailArgs(idTask = item.id)
//                                    )
//                                )
//                            }
//                    )
//                }
//            }
//        }
//        binding.rvScheduleToday.setOnClickListener {
//
//        }


//        binding.tbHome.iconRightId.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
//        }

//        shareViewModel.schedulesState.observe(viewLifecycleOwner) {
//            when (it) {
//                is State.Loading -> {
//                }
//                is State.Success -> {
//                    shareViewModel.schedules = it.data
//                    if (shareViewModel.getScheduleToday(it.data).isEmpty()) {
//                        binding.txtNoScheduler.apply {
//                            visibility = View.VISIBLE
//                            text = "Hôm nay bạn không có lịch học trên trường"
//                        }
//                    }
//                    scheduleTodayAdapter.setItems(shareViewModel.getScheduleToday(it.data))
//                    Log.d(TAG, "success: ${it.data.size}")
//
//                }
//                is State.Error -> {
//                    Log.d(TAG, "error: ${it.exception} ")
//                }
//            }
//        }
//
//        /**
//         * teacher event
//         */
//
//
//        binding.viewTeacher.cdNews.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_newsFragment)
//        }
//        binding.viewTeacher.cdProject.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_projectGraduateFragment)
//        }
//        binding.imgAdd.setOnClickListener { showDialog() }
//    }


}