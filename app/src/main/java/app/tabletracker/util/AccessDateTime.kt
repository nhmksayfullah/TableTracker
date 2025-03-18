package app.tabletracker.util

import kotlinx.datetime.*

fun generateInstantTime(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

fun LocalDateTime.toEpochMillis(timeZone: TimeZone = TimeZone.UTC): Long {
    return this.toInstant(timeZone).toEpochMilliseconds()
}

fun Long.toLocalDateTime(timeZone: TimeZone = TimeZone.UTC): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
}




fun generateLocalDate(): LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}

fun generateLocalDate(year: Int, month: Int, dayOfMonth: Int): LocalDate {
    return LocalDate(year, month, dayOfMonth)
}

fun generateLocalTime(): LocalTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
}

fun generateLocalTime(hour: Int, minute: Int): LocalTime {
    return LocalTime(hour, minute)
}

fun generateLocalDateTime(localDate: LocalDate, localTime: LocalTime): LocalDateTime {
    return LocalDateTime(localDate.year, localDate.monthNumber, localDate.dayOfMonth, localTime.hour, localTime.minute)
}

fun generateLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun getStartOfDay(): Long {
    val today = generateLocalDate()
    return today.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun getEndOfDay(): Long {
    val today = generateLocalDate()
    val endOfDayTime = LocalTime(23, 59, 59, 999_999_999) // 23:59:59.999999999
    val endOfDay = LocalDateTime(today.year, today.monthNumber, today.dayOfMonth, endOfDayTime.hour, endOfDayTime.minute, endOfDayTime.second, endOfDayTime.nanosecond)
    return endOfDay.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}
