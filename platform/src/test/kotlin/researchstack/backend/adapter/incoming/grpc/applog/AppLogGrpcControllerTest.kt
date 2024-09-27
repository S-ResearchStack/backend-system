package researchstack.backend.adapter.incoming.grpc.applog

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.AppLogTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.applog.SendAppLogUseCase
import researchstack.backend.grpc.AppLog

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppLogGrpcControllerTest {
    private val sendAppLogUseCase = mockk<SendAppLogUseCase>()
    private val appLogGrpcController = AppLogGrpcController(sendAppLogUseCase)

    private val appLog = AppLog.newBuilder()
        .setName(AppLogTestUtil.name)
        .setTimestamp(AppLogTestUtil.now.toGrpc())
        .putAllData(AppLogTestUtil.data)
        .build()

    @Test
    @Tag(POSITIVE_TEST)
    fun `sendAppLog should work properly`() = runTest {
        coEvery {
            sendAppLogUseCase.sendAppLog(any())
        } returns Unit

        assertDoesNotThrow {
            appLogGrpcController.sendAppLog(appLog)
        }
    }
}
