package app.tabletracker.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

fun generateInstantTime(): Long {
    return Instant.now().toEpochMilli()
}

fun LocalDateTime.toEpochMillis(): Long {
    return this.toInstant(ZoneOffset.UTC).toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}
fun generateLocalDate(): LocalDate {
    return LocalDate.now()
}

fun generateLocalDate(year: Int, month: Int, dayOfMonth: Int): LocalDate {
    return LocalDate.of(year, month, dayOfMonth)
}

fun generateLocalTime(): LocalTime {
    return LocalTime.now()
}

fun generateLocalTime(hour: Int, minute: Int): LocalTime {
    return LocalTime.of(hour, minute)
}

fun generateLocalDateTime(localDate: LocalDate, localTime: LocalTime): LocalDateTime {
    return localDate.atTime(localTime)
}
fun generateLocalDateTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun getStartOfDay(): Long {
    return LocalDate.now().atStartOfDay().toEpochMillis()
}

fun getEndOfDay(): Long {
    return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toEpochMillis()
}