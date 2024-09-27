package researchstack.backend.application.service.applog

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.AppLogTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.applog.CreateAppLogOutPort
import researchstack.backend.application.service.AppLogService

class AppLogServiceTest {
    private val createAppLogOutPort = mockk<CreateAppLogOutPort>()
    private val appLogService = AppLogService(
        createAppLogOutPort
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @Tag(POSITIVE_TEST)
    fun `sendAppLog should work properly`() = runTest {
        val sendAppLogCommand = AppLogTestUtil.createSendAppLogCommand()

        val appLog = sendAppLogCommand.toDomain()

        coEvery { createAppLogOutPort.createAppLog(appLog) } returns Unit

        appLogService.sendAppLog(sendAppLogCommand)
    }
}
