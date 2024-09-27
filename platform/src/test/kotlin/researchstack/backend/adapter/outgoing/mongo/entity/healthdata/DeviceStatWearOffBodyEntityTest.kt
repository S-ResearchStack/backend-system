package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class DeviceStatWearOffBodyEntityTest {
    private val deviceStatWearOffBodyEntity = DeviceStatWearOffBodyEntity(
        subjectId = "subjectId",
        isWearableOffBody = 1,
        timeOffset = 3600000,
        timestamp = Timestamp.valueOf("2023-01-01 12:00:00")
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `DeviceStatWearOffBodyEntity should have all properties set correctly`() = runTest {
        assertEquals("subjectId", deviceStatWearOffBodyEntity.subjectId)
        assertEquals(1, deviceStatWearOffBodyEntity.isWearableOffBody)
        assertEquals(3600000, deviceStatWearOffBodyEntity.timeOffset)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), deviceStatWearOffBodyEntity.timestamp)
    }
}
