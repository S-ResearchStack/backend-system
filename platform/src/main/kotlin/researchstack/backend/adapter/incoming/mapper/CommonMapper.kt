package researchstack.backend.adapter.incoming.mapper

import com.google.protobuf.Timestamp
import com.google.type.Date
import com.google.type.TimeOfDay
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toGrpc(zoneId: ZoneId = ZoneOffset.systemDefault()): Timestamp {
    val zonedDateTime = ZonedDateTime.of(this, zoneId)
    return Timestamp.newBuilder()
        .setSeconds(zonedDateTime.toEpochSecond())
        .setNanos(zonedDateTime.nano)
        .build()
}

fun LocalDate.toGrpc(): Date {
    return Date.newBuilder()
        .setYear(year)
        .setMonth(monthValue)
        .setDay(dayOfMonth)
        .build()
}

fun LocalTime.toGrpc(): TimeOfDay {
    return TimeOfDay.newBuilder()
        .setHours(hour)
        .setMinutes(minute)
        .setSeconds(second)
        .setNanos(nano)
        .build()
}

fun Timestamp.toLocalDateTime(zoneId: ZoneId = ZoneOffset.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(seconds, nanos.toLong()),
        zoneId
    )
}

fun Timestamp.toStringWithTimeZone(): String =
    DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)
        .format(Instant.ofEpochSecond(this.seconds, this.nanos.toLong()))

fun Timestamp.toEpochMilli(): Long =
    Instant.ofEpochSecond(this.seconds, this.nanos.toLong()).toEpochMilli()
