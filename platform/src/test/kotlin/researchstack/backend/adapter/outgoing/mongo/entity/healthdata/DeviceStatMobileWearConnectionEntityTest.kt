package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class DeviceStatMobileWearConnectionEntityTest {
    private val deviceStatMobileWearConnectionEntity = DeviceStatMobileWearConnectionEntity(
        subjectId = "subjectId",
        wearableDeviceName = "Galaxy Watch",
        timeOffset = 3600000,
        timestamp = Timestamp.valueOf("2023-01-01 12:00:00")
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `DeviceStatMobileWearConnectionEntity should have all properties set correctly`() = runTest {
        assertEquals("subjectId", deviceStatMobileWearConnectionEntity.subjectId)
        assertEquals("Galaxy Watch", deviceStatMobileWearConnectionEntity.wearableDeviceName)
        assertEquals(3600000, deviceStatMobileWearConnectionEntity.timeOffset)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), deviceStatMobileWearConnectionEntity.timestamp)
    }
}
