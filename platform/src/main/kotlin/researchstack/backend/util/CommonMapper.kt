package researchstack.backend.util

import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.atZone(ZoneOffset.systemDefault()).toInstant()
    return Timestamp.newBuilder().setSeconds(instant.epochSecond).setNanos(instant.nano).build()
}

fun LocalDateTime.toLong(): Long {
    val instant = this.atZone(ZoneOffset.systemDefault()).toInstant()
    return instant.epochSecond
}

fun Instant.toTimestamp(): Timestamp =
    Timestamp.newBuilder().setSeconds(this.epochSecond).setNanos(this.nano).build()
