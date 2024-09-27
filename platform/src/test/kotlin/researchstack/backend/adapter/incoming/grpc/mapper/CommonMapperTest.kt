package researchstack.backend.adapter.incoming.grpc.mapper

import com.google.protobuf.Timestamp
import com.google.type.Date
import com.google.type.TimeOfDay
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.adapter.incoming.mapper.toLocalDateTime
import researchstack.backend.adapter.incoming.mapper.toStringWithTimeZone
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class CommonMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `LocalDateTime's toGrpc should work properly`() {
        val localDateTime = LocalDateTime.now()
        Assertions.assertEquals(localDateTime, localDateTime.toGrpc().toLocalDateTime())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Timestamp's toStringWithTimeZone should work properly`() {
        Assertions.assertEquals(
            "2023-07-11T20:00:00.000Z",
            Timestamp.newBuilder().setSeconds(1689105600).setNanos(1234).build().toStringWithTimeZone()
        )
        Assertions.assertEquals(
            "2023-07-11T20:00:00.000Z",
            Timestamp.newBuilder().setSeconds(1689105600).build().toStringWithTimeZone()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `LocalDate's toGrpc should work properly`() {
        val year = 2023
        val month = 12
        val dayOfMonth = 26
        val localDate = LocalDate.of(year, month, dayOfMonth)
        val expected = Date.newBuilder()
            .setYear(year)
            .setMonth(month)
            .setDay(dayOfMonth)
            .build()
        Assertions.assertEquals(expected, localDate.toGrpc())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `LocalTime's toGrpc should work properly`() {
        val hour = 4
        val minute = 5
        val second = 10
        val nano = 100
        val localTime = LocalTime.of(hour, minute, second, nano)
        val expected = TimeOfDay.newBuilder()
            .setHours(hour)
            .setMinutes(minute)
            .setSeconds(second)
            .setNanos(nano)
            .build()
        Assertions.assertEquals(expected, localTime.toGrpc())
    }
}
