package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.study.GetHealthDataOutPort
import researchstack.backend.application.service.healthdata.GetHealthDataService
import researchstack.backend.domain.study.HealthDataGroup

@ExperimentalCoroutinesApi
internal class GetHealthDataServiceTest {
    private val getHealthDataOutPort = mockk<GetHealthDataOutPort>()
    private val getHealthDataService = GetHealthDataService(getHealthDataOutPort)
    private val healthDataGroup1 = HealthDataGroup(
        "samsunghealth",
        listOf("HEART_RATE", "OXYGEN_SATURATION", "SLEEP_SESSION")
    )
    private val healthDataList = listOf(healthDataGroup1)

    @Test
    @Tag(POSITIVE_TEST)
    fun `getHealthDataList should work properly`() = runTest {
        coEvery {
            getHealthDataOutPort.getHealthDataList()
        } returns healthDataList

        val response = getHealthDataService.getHealthDataList()
        Assertions.assertEquals(1, response.size)
    }
}
