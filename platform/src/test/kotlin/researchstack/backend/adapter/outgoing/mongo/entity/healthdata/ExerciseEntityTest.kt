package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class ExerciseEntityTest {
    private val startTime = Timestamp.valueOf("2023-01-01 12:00:00")
    private val endTime = Timestamp.valueOf("2023-01-01 13:00:00")
    private val exerciseEntity = ExerciseEntity(
        subjectId = "subjectId",
        startTime = startTime,
        endTime = endTime,
        exerciseType = 1,
        calorie = 100.0,
        duration = 3600000L,
        distance = 10.0,
        altitudeGain = 50.0,
        altitudeLoss = 30.0,
        count = 20L,
        countType = 1L,
        maxSpeed = 15.0,
        meanSpeed = 10.0,
        maxCaloriburnRate = 20.0,
        meanCaloriburnRate = 10.0,
        maxCadence = 90.0,
        meanCadence = 80.0,
        maxHeartRate = 180.0,
        meanHeartRate = 150.0,
        minHeartRate = 120.0,
        maxAltitude = 200.0,
        minAltitude = 100.0,
        inclineDistance = 5.0,
        declineDistance = 3.0,
        maxPower = 300.0,
        meanPower = 250.0,
        meanRpm = 60.0,
        maxRpm = 70.0,
        vo2Max = 65.0
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `ExerciseEntity should have all properties set correctly`() = runTest {
        assertEquals("subjectId", exerciseEntity.subjectId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), exerciseEntity.startTime)
        assertEquals(Timestamp.valueOf("2023-01-01 13:00:00"), exerciseEntity.endTime)
        assertEquals(1, exerciseEntity.exerciseType)
        assertEquals(100.0, exerciseEntity.calorie)
        assertEquals(3600000L, exerciseEntity.duration)
        assertEquals(10.0, exerciseEntity.distance)
        assertEquals(50.0, exerciseEntity.altitudeGain)
        assertEquals(30.0, exerciseEntity.altitudeLoss)
        assertEquals(20L, exerciseEntity.count)
        assertEquals(1L, exerciseEntity.countType)
        assertEquals(15.0, exerciseEntity.maxSpeed)
        assertEquals(10.0, exerciseEntity.meanSpeed)
        assertEquals(20.0, exerciseEntity.maxCaloriburnRate)
        assertEquals(10.0, exerciseEntity.meanCaloriburnRate)
        assertEquals(90.0, exerciseEntity.maxCadence)
        assertEquals(80.0, exerciseEntity.meanCadence)
        assertEquals(180.0, exerciseEntity.maxHeartRate)
        assertEquals(150.0, exerciseEntity.meanHeartRate)
        assertEquals(120.0, exerciseEntity.minHeartRate)
        assertEquals(200.0, exerciseEntity.maxAltitude)
        assertEquals(100.0, exerciseEntity.minAltitude)
        assertEquals(5.0, exerciseEntity.inclineDistance)
        assertEquals(3.0, exerciseEntity.declineDistance)
        assertEquals(300.0, exerciseEntity.maxPower)
        assertEquals(250.0, exerciseEntity.meanPower)
        assertEquals(60.0, exerciseEntity.meanRpm)
        assertEquals(70.0, exerciseEntity.maxRpm)
        assertEquals(65.0, exerciseEntity.vo2Max)
    }
}
