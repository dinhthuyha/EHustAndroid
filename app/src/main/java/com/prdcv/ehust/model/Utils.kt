import com.prdcv.ehust.extension.randomColor
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.ScheduleEvent

fun generateColorSChedules(schedules: List<ScheduleEvent>): List<ScheduleEvent> {
    schedules.forEach { it.color  = randomColor() }
    return schedules
}

fun generateColorMeetings(schedules: List<Meeting>): List<Meeting> {
    schedules.forEach { it.color  = randomColor() }
    return schedules
}
