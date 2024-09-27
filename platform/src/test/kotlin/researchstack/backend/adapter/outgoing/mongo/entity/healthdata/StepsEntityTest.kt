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
internal class StepsEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly`() = runTest {
        val stepsEntity = StepsEntity(
            id = "stepEntityId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            endTime = Timestamp.valueOf("2023-01-01 13:00:00"),
            count = 10000.0,
            calorie = 300.0,
            distance = 5.0,
            speed = 2.0,
            timeOffset = "UTC+9"
        )

        assertEquals("stepEntityId", stepsEntity.id)
        assertEquals("subjectId", stepsEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), stepsEntity.startTime)
        assertEquals(Timestamp.valueOf("2023-01-01 13:00:00"), stepsEntity.endTime)
        assertEquals(10000.0, stepsEntity.count)
        assertEquals(300.0, stepsEntity.calorie)
        assertEquals(5.0, stepsEntity.distance)
        assertEquals(2.0, stepsEntity.speed)
        assertEquals("UTC+9", stepsEntity.timeOffset)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly without timeOffset`() = runTest {
        val stepsEntity = StepsEntity(
            id = "stepEntityId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            endTime = Timestamp.valueOf("2023-01-01 13:00:00"),
            count = 10000.0,
            calorie = 300.0,
            distance = 5.0,
            speed = 2.0
        )

        assertEquals("stepEntityId", stepsEntity.id)
        assertEquals("subjectId", stepsEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), stepsEntity.startTime)
        assertEquals(Timestamp.valueOf("2023-01-01 13:00:00"), stepsEntity.endTime)
        assertEquals(10000.0, stepsEntity.count)
        assertEquals(300.0, stepsEntity.calorie)
        assertEquals(5.0, stepsEntity.distance)
        assertEquals(2.0, stepsEntity.speed)
        assertNull(stepsEntity.timeOffset)
    }
}
