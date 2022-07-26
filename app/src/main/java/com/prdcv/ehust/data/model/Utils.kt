import com.prdcv.ehust.utils.extension.randomColor
import com.prdcv.ehust.data.model.Meeting
import com.prdcv.ehust.data.model.ScheduleEvent

fun generateColorSChedules(schedules: List<ScheduleEvent>): List<ScheduleEvent> {
    schedules.forEach { it.color  = randomColor() }
    return schedules
}

fun generateColorMeetings(schedules: List<Meeting>): List<Meeting> {
    schedules.forEach { it.color  = randomColor() }
    return schedules
}
