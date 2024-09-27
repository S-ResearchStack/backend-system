package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class WeightEntityTest {
    @Test
    @Tag("POSITIVE_TEST")
    fun `constructor should set all properties correctly`() = runTest {
        val weightEntity = WeightEntity(
            subjectId = "subjectId",
            startTime = Timestamp.valueOf("2023-01-01 12:00:00"),
            weight = 70.0,
            height = 180.0,
            bodyFat = 15.0,
            skeletalMuscle = 30.0,
            muscleMass = 40.0,
            basalMetabolicRate = 1500L,
            bodyFatMass = 10.5,
            fatFreeMass = 59.5,
            fatFree = 85.0,
            skeletalMuscleMass = 35.0,
            totalBodyWater = 50.0
        )

        assertEquals("subjectId", weightEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), weightEntity.startTime)
        assertEquals(70.0, weightEntity.weight)
        assertEquals(180.0, weightEntity.height)
        assertEquals(15.0, weightEntity.bodyFat)
        assertEquals(30.0, weightEntity.skeletalMuscle)
        assertEquals(40.0, weightEntity.muscleMass)
        assertEquals(1500L, weightEntity.basalMetabolicRate)
        assertEquals(10.5, weightEntity.bodyFatMass)
        assertEquals(59.5, weightEntity.fatFreeMass)
        assertEquals(85.0, weightEntity.fatFree)
        assertEquals(35.0, weightEntity.skeletalMuscleMass)
        assertEquals(50.0, weightEntity.totalBodyWater)
    }
}
