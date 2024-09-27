package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class WearHeartRateEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly`() = runTest {
        val wearHeartRateEntity = WearHeartRateEntity(
            id = "wearHeartRateId",
            subjectId = "subjectId",
            value = 70L,
            status = 1L,
            ibiList = listOf(100L, 200L, 300L),
            ibiStatusList = listOf(1L, 1L, 1L),
            timestamp = Timestamp.valueOf("2023-01-01 12:00:00"),
            timeOffset = "UTC+9"
        )

        assertEquals("wearHeartRateId", wearHeartRateEntity.id)
        assertEquals("subjectId", wearHeartRateEntity.subjectId)
        assertEquals(70L, wearHeartRateEntity.value)
        assertEquals(1L, wearHeartRateEntity.status)
        assertEquals(listOf(100L, 200L, 300L), wearHeartRateEntity.ibiList)
        assertEquals(listOf(1L, 1L, 1L), wearHeartRateEntity.ibiStatusList)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), wearHeartRateEntity.timestamp)
        assertEquals("UTC+9", wearHeartRateEntity.timeOffset)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly without optional variables`() = runTest {
        val wearHeartRateEntity = WearHeartRateEntity(
            id = "wearHeartRateId",
            subjectId = "subjectId",
            value = 70L,
            status = 1L,
            ibiList = listOf(100L, 200L, 300L),
            ibiStatusList = listOf(1L, 1L, 1L),
            timestamp = Timestamp.valueOf("2023-01-01 12:00:00")
        )

        assertEquals("wearHeartRateId", wearHeartRateEntity.id)
        assertEquals("subjectId", wearHeartRateEntity.subjectId)
        assertEquals(70L, wearHeartRateEntity.value)
        assertEquals(1L, wearHeartRateEntity.status)
        assertEquals(listOf(100L, 200L, 300L), wearHeartRateEntity.ibiList)
        assertEquals(listOf(1L, 1L, 1L), wearHeartRateEntity.ibiStatusList)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), wearHeartRateEntity.timestamp)
        assertNull(wearHeartRateEntity.timeOffset)
    }
}
