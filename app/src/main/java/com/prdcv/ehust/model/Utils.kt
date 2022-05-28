import com.prdcv.ehust.extension.randomColor
import com.prdcv.ehust.model.ScheduleEvent

fun generateColorSChedules(schedules: List<ScheduleEvent>): List<ScheduleEvent> {
    schedules.forEach { it.color  = randomColor() }
    return schedules
}
