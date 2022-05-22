package com.prdcv.ehust.model

import com.prdcv.ehust.extension.randomColor
import java.time.LocalTime
import java.time.YearMonth


fun generateFlights(): List<ScheduleEvent> {
    val list = mutableListOf<ScheduleEvent>()
    val currentMonth = YearMonth.now()

    val currentMonth17 = currentMonth.atDay(17)
    list.add(
        ScheduleEvent(
            currentMonth17,
            "Thị giác máy tính",
            LocalTime.of(6, 45),
            LocalTime.of(8, 30),
            randomColor()
        )
    )
    list.add(
        ScheduleEvent(
            currentMonth17,
            "Tin sinh học",
            LocalTime.of(14, 0),
            LocalTime.of(15, 30),
            randomColor()
        )
    )

    val currentMonth22 = currentMonth.atDay(22)
    list.add(
        ScheduleEvent(
            currentMonth22,
            "Đánh giá hiệu năng mạng",
            LocalTime.of(13, 20),
            LocalTime.of(15, 20),
            randomColor()
        )
    )
    list.add(
        ScheduleEvent(
            currentMonth22,
            "Quản trị dự án CNTT",
            LocalTime.of(17, 40),
            LocalTime.of(19, 40),
            randomColor()
        )
    )
    list.add(
        ScheduleEvent(
            currentMonth22,
            "Các kỹ thuật định vị và ứng dụng",
            LocalTime.of(12, 30),
            LocalTime.of(15, 0),
            randomColor()
        )
    )

    list.add(
        ScheduleEvent(
            currentMonth.atDay(3),
            "Phát triển phần mềm nhúng thông minh",
            LocalTime.of(15, 0),
            LocalTime.of(16, 0),
            randomColor()
        )
    )

    list.add(
        ScheduleEvent(
            currentMonth.atDay(12),
            "Công nghệ nhận dạng và tổng hợp tiếng nói",
            LocalTime.of(6, 45),
            LocalTime.of(8, 15),
            randomColor()
        )
    )

    val nextMonth13 = currentMonth.plusMonths(1).atDay(13)
    list.add(
        ScheduleEvent(
            nextMonth13,
            "Nhập môn học máy và khai phá dữ liệu",
            LocalTime.of(7, 30),
            LocalTime.of(9, 0),
            randomColor()
        )
    )
    list.add(
        ScheduleEvent(
            nextMonth13,
            "Kỹ năng mềm ",
            LocalTime.of(16, 30),
            LocalTime.of(17, 45),
            randomColor()
        )
    )

    list.add(
        ScheduleEvent(
            currentMonth.minusMonths(1).atDay(9),
            "Các công nghệ truyền thông cho IoT",
            LocalTime.of(6, 55),
            LocalTime.of(9, 15),
            randomColor()
        )
    )

    return list
}
