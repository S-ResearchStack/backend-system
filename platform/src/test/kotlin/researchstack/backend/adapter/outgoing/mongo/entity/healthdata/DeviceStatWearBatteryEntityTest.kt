package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class DeviceStatWearBatteryEntityTest {
    private val deviceStatWearBatteryEntity = DeviceStatWearBatteryEntity(
        subjectId = "subjectId",
        isCharging = 1,
        percentage = 50,
        timeOffset = 28800,
        timestamp = Timestamp.valueOf("2023-01-01 12:00:00")
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `DeviceStatWearBatteryEntity should have all properties set correctly`() = runTest {
        assertEquals("subjectId", deviceStatWearBatteryEntity.subjectId)
        assertEquals(1, deviceStatWearBatteryEntity.isCharging)
        assertEquals(50, deviceStatWearBatteryEntity.percentage)
        assertEquals(28800, deviceStatWearBatteryEntity.timeOffset)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), deviceStatWearBatteryEntity.timestamp)
    }
}
