package researchstack.backend.adapter.outgoing.mongo.applog

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.AppLogTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.AppLogRepository

@ExperimentalCoroutinesApi
class CreateAppLogMongoAdapterTest {
    private val appLogRepository = mockk<AppLogRepository>()
    private val createAppLogMongoAdapter = CreateAppLogMongoAdapter(appLogRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `createAppLog should work properly`() = runTest {
        val appLog = AppLogTestUtil.createDummyAppLog()
        val appLogEntity = appLog.toEntity()

        every {
            appLogRepository.save(appLogEntity)
        } returns appLogEntity.toMono()

        assertDoesNotThrow {
            createAppLogMongoAdapter.createAppLog(appLog)
        }
    }
}
