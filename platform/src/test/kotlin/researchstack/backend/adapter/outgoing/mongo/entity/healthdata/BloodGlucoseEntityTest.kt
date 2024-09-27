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
internal class BloodGlucoseEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly`() = runTest {
        val bloodGlucoseEntity = BloodGlucoseEntity(
            id = "bloodGlucoseId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            timeOffset = "UTC+9",
            glucose = 100.0,
            measurementType = 1L,
            mealTime = 2L,
            mealType = 3L,
            sampleSourceType = 4L
        )

        assertEquals("bloodGlucoseId", bloodGlucoseEntity.id)
        assertEquals("subjectId", bloodGlucoseEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), bloodGlucoseEntity.startTime)
        assertEquals("UTC+9", bloodGlucoseEntity.timeOffset)
        assertEquals(100.0, bloodGlucoseEntity.glucose)
        assertEquals(1L, bloodGlucoseEntity.measurementType)
        assertEquals(2L, bloodGlucoseEntity.mealTime)
        assertEquals(3L, bloodGlucoseEntity.mealType)
        assertEquals(4L, bloodGlucoseEntity.sampleSourceType)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly without optional variables`() = runTest {
        val bloodGlucoseEntity = BloodGlucoseEntity(
            id = "bloodGlucoseId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            glucose = 100.0,
            measurementType = 1L
        )

        assertEquals("bloodGlucoseId", bloodGlucoseEntity.id)
        assertEquals("subjectId", bloodGlucoseEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), bloodGlucoseEntity.startTime)
        assertNull(bloodGlucoseEntity.timeOffset)
        assertEquals(100.0, bloodGlucoseEntity.glucose)
        assertEquals(1L, bloodGlucoseEntity.measurementType)
        assertNull(bloodGlucoseEntity.mealTime)
        assertNull(bloodGlucoseEntity.mealType)
        assertNull(bloodGlucoseEntity.sampleSourceType)
    }
}
