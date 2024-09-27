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
internal class BloodPressureEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly`() = runTest {
        val bloodPressureEntity = BloodPressureEntity(
            id = "bloodPressureId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            timeOffset = "UTC+9",
            systolic = 120.0,
            diastolic = 80.0,
            mean = 95.0,
            pulse = 70L
        )

        assertEquals("bloodPressureId", bloodPressureEntity.id)
        assertEquals("subjectId", bloodPressureEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), bloodPressureEntity.startTime)
        assertEquals("UTC+9", bloodPressureEntity.timeOffset)
        assertEquals(120.0, bloodPressureEntity.systolic)
        assertEquals(80.0, bloodPressureEntity.diastolic)
        assertEquals(95.0, bloodPressureEntity.mean)
        assertEquals(70L, bloodPressureEntity.pulse)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly without optional variables`() = runTest {
        val bloodPressureEntity = BloodPressureEntity(
            id = "bloodPressureId",
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            systolic = 120.0,
            diastolic = 80.0
        )

        assertEquals("bloodPressureId", bloodPressureEntity.id)
        assertEquals("subjectId", bloodPressureEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), bloodPressureEntity.startTime)
        assertNull(bloodPressureEntity.timeOffset)
        assertEquals(120.0, bloodPressureEntity.systolic)
        assertEquals(80.0, bloodPressureEntity.diastolic)
        assertNull(bloodPressureEntity.mean)
        assertNull(bloodPressureEntity.pulse)
    }
}
