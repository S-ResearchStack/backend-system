package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
internal class WearBiaEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly`() = runTest {
        val wearBiaEntity = WearBiaEntity(
            id = "wearBiaId",
            subjectId = "subjectId",
            basalMetabolicRate = 1500.0,
            bodyFatMass = 10.5,
            bodyFatRatio = 15.0,
            fatFreeMass = 59.5,
            fatFreeRatio = 85.0,
            skeletalMuscleMass = 35.0,
            skeletalMuscleRatio = 30.0,
            totalBodyWater = 50.0,
            measurementProgress = 100.0,
            status = 1L,
            timestamp = Timestamp.valueOf("2023-01-01 12:00:00"),
            timeOffset = "UTC+9"
        )

        assertEquals("wearBiaId", wearBiaEntity.id)
        assertEquals("subjectId", wearBiaEntity.subjectId)
        assertEquals(1500.0, wearBiaEntity.basalMetabolicRate)
        assertEquals(10.5, wearBiaEntity.bodyFatMass)
        assertEquals(15.0, wearBiaEntity.bodyFatRatio)
        assertEquals(59.5, wearBiaEntity.fatFreeMass)
        assertEquals(85.0, wearBiaEntity.fatFreeRatio)
        assertEquals(35.0, wearBiaEntity.skeletalMuscleMass)
        assertEquals(30.0, wearBiaEntity.skeletalMuscleRatio)
        assertEquals(50.0, wearBiaEntity.totalBodyWater)
        assertEquals(100.0, wearBiaEntity.measurementProgress)
        assertEquals(1L, wearBiaEntity.status)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), wearBiaEntity.timestamp)
        assertEquals("UTC+9", wearBiaEntity.timeOffset)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `constructor should set all properties correctly without timeOffset`() {
        val wearBiaEntity = WearBiaEntity(
            id = "wearBiaId",
            subjectId = "subjectId",
            basalMetabolicRate = 1500.0,
            bodyFatMass = 10.5,
            bodyFatRatio = 15.0,
            fatFreeMass = 59.5,
            fatFreeRatio = 85.0,
            skeletalMuscleMass = 35.0,
            skeletalMuscleRatio = 30.0,
            totalBodyWater = 50.0,
            measurementProgress = 100.0,
            status = 1L,
            timestamp = Timestamp.valueOf("2023-01-01 12:00:00")
        )

        assertEquals("wearBiaId", wearBiaEntity.id)
        assertEquals("subjectId", wearBiaEntity.subjectId)
        assertEquals(1500.0, wearBiaEntity.basalMetabolicRate)
        assertEquals(10.5, wearBiaEntity.bodyFatMass)
        assertEquals(15.0, wearBiaEntity.bodyFatRatio)
        assertEquals(59.5, wearBiaEntity.fatFreeMass)
        assertEquals(85.0, wearBiaEntity.fatFreeRatio)
        assertEquals(35.0, wearBiaEntity.skeletalMuscleMass)
        assertEquals(30.0, wearBiaEntity.skeletalMuscleRatio)
        assertEquals(50.0, wearBiaEntity.totalBodyWater)
        assertEquals(100.0, wearBiaEntity.measurementProgress)
        assertEquals(1L, wearBiaEntity.status)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), wearBiaEntity.timestamp)
        assertNull(wearBiaEntity.timeOffset)
    }
}
