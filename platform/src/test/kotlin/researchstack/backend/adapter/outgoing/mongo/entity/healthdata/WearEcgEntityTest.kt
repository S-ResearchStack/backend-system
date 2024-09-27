package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import java.sql.Timestamp

@ExperimentalCoroutinesApi
internal class WearEcgEntityTest {
    private val wearEcgEntity = WearEcgEntity(
        subjectId = "subjectId",
        ecg1Mv = 1.0,
        ecg2Mv = 2.0,
        ecg3Mv = 3.0,
        ecg4Mv = 4.0,
        ecg5Mv = 5.0,
        ecg6Mv = 6.0,
        ecg7Mv = 7.0,
        ecg8Mv = 8.0,
        ecg9Mv = 9.0,
        ecg10Mv = 10.0,
        ppgGreen = 11,
        ppgGreenCurrent = 12,
        ppgGreenIOffset = 13,
        ppgGreenTiaGain = 14,
        leafOff = 15,
        posThresholdMv = 16.0,
        negThresholdMv = 17.0,
        pktSeqNum = 18,
        sessionId = 19,
        timestamp = Timestamp.valueOf("2023-01-01 12:00:00"),
        timeOffset = "20"
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `WearEcgEntity should have all properties set correctly`() = runTest {
        assertEquals("subjectId", wearEcgEntity.subjectId)
        assertEquals(1.0, wearEcgEntity.ecg1Mv)
        assertEquals(2.0, wearEcgEntity.ecg2Mv)
        assertEquals(3.0, wearEcgEntity.ecg3Mv)
        assertEquals(4.0, wearEcgEntity.ecg4Mv)
        assertEquals(5.0, wearEcgEntity.ecg5Mv)
        assertEquals(6.0, wearEcgEntity.ecg6Mv)
        assertEquals(7.0, wearEcgEntity.ecg7Mv)
        assertEquals(8.0, wearEcgEntity.ecg8Mv)
        assertEquals(9.0, wearEcgEntity.ecg9Mv)
        assertEquals(10.0, wearEcgEntity.ecg10Mv)
        assertEquals(11, wearEcgEntity.ppgGreen)
        assertEquals(12, wearEcgEntity.ppgGreenCurrent)
        assertEquals(13, wearEcgEntity.ppgGreenIOffset)
        assertEquals(14, wearEcgEntity.ppgGreenTiaGain)
        assertEquals(15, wearEcgEntity.leafOff)
        assertEquals(16.0, wearEcgEntity.posThresholdMv)
        assertEquals(17.0, wearEcgEntity.negThresholdMv)
        assertEquals(18, wearEcgEntity.pktSeqNum)
        assertEquals(19, wearEcgEntity.sessionId)
        assertEquals(Timestamp.valueOf("2023-01-01 12:00:00"), wearEcgEntity.timestamp)
        assertEquals("20", wearEcgEntity.timeOffset)
    }
}
