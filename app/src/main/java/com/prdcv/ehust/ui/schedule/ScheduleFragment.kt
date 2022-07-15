package com.prdcv.ehust.ui.schedule

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.ui.graphics.Color
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.CalendarDayBinding
import com.prdcv.ehust.databinding.CalendarHeaderBinding
import com.prdcv.ehust.databinding.FragmentScheduleBinding
import com.prdcv.ehust.extension.convertDateEnglishToVN
import com.prdcv.ehust.extension.convertMonthToVN
import com.prdcv.ehust.extension.daysOfWeekFromLocale
import com.prdcv.ehust.extension.setTextColorRes
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.SChedule
import com.prdcv.ehust.model.ScheduleEvent
import dagger.hilt.android.AndroidEntryPoint
import generateColorMeetings
import generateColorSChedules
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class ScheduleFragment : BaseFragmentWithBinding<FragmentScheduleBinding>() {


    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private val scheduleAdapter = ScheduleEventAdapter(clickListener = ::navigateToDetailNews)
    private var schedule = mapOf<String, List<ScheduleEvent>>()
    private var meetings = mapOf<String, List<Meeting>>()
    override fun getViewBinding(inflater: LayoutInflater) =
        FragmentScheduleBinding.inflate(inflater).apply {

        }

    private fun navigateToDetailNews(newsItem: SChedule) {


    }

    override fun init() {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedule = generateColorSChedules(shareViewModel.uiState.schedulesState).groupBy {
            it.startDateStudy?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                .toString()
        }
        meetings = generateColorMeetings(shareViewModel.uiState.meetings).groupBy {
            it.date?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                .toString()
        }
        binding.exFiveRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = scheduleAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        scheduleAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        binding.exFiveCalendar.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
        binding.exFiveCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            Log.d("TAG", "onViewCreated:${selectedDate},  ${day.date}")
                            selectedDate = day.date
                            val binding = this@ScheduleFragment.binding
                            binding.exFiveCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.exFiveCalendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }
        binding.exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exFiveDayText
                val layout = container.binding.exFiveDayLayout
                textView.text = day.date.dayOfMonth.toString()

                val flightTopView = container.binding.exFiveDayFlightTop
                val flightBottomView = container.binding.exFiveDayFlightBottom
                flightTopView.background = null
                flightBottomView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.black)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.example_5_selected_bg else 0)
                    val nameDayOfWeek =
                        day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    //check neu ngay co lich hoc

                    val flights = schedule[nameDayOfWeek]
                    val meetingToday = meetings[nameDayOfWeek]

                    if (flights != null && day.date < flights.first().dueDateStudy) {

                        if (flights.isNotEmpty()) {
                            flightBottomView.setBackgroundColor(flights[0].color)
                        }
                    }

                    if (meetingToday != null) {

                        if (meetingToday.isNotEmpty()) {
                            flightBottomView.setBackgroundColor(
                                meetingToday[0].color ?: android.graphics.Color.BLUE
                            )
                        }
                    }

                } else {
                    textView.setTextColorRes(R.color.example_5_text_grey)
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.exFiveCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                val date = daysOfWeek[index].getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH
                                )
                                    .toUpperCase(Locale.ENGLISH).toString()
                                tv.text = date.convertDateEnglishToVN()
                                tv.setTextColorRes(R.color.black)
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                            }
                        month.yearMonth
                    }
                }
            }

        binding.exFiveCalendar.monthScrollListener = { month ->
            val title = "${
                monthTitleFormatter.format(month.yearMonth).convertMonthToVN()
            } ${month.yearMonth.year}"
            binding.exFiveMonthYearText.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.exFiveCalendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

        binding.exFiveNextMonthImage.setOnClickListener {
            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.exFivePreviousMonthImage.setOnClickListener {
            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }

//        binding.exThreeAddButton.setOnClickListener {
//            inputDialog.show()
//        }
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        val nameDayOfWeek = date?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        schedule[nameDayOfWeek]?.forEach { it.date = date!! }
        meetings[nameDayOfWeek]?.forEach { }
        val list = mutableListOf<SChedule>()
        schedule[nameDayOfWeek]?.let { list.addAll(it) }
        meetings[nameDayOfWeek]?.let { list.addAll(it) }
        scheduleAdapter.setItems(list.orEmpty())
        scheduleAdapter.notifyDataSetChanged()
    }
}