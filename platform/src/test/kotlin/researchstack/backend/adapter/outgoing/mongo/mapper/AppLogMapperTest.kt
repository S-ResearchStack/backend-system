package researchstack.backend.adapter.outgoing.mongo.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.AppLogTestUtil
import researchstack.backend.POSITIVE_TEST
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class AppLogMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should map AppLog to AppLogEntity`() = runTest {
        val appLog = AppLogTestUtil.createDummyAppLog()
        val actual = appLog.toEntity()

        assertEquals(appLog.name, actual.name)
        assertEquals(appLog.timestamp, actual.timestamp)
        assertEquals(appLog.data["Note"], actual.data["Note"])
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map AppLogEntity to AppLog`() = runTest {
        val appLogEntity = AppLogTestUtil.createDummyAppLog().toEntity()
        val actual = appLogEntity.toDomain()

        assertEquals(appLogEntity.name, actual.name)
        assertEquals(appLogEntity.timestamp, actual.timestamp)
        assertEquals(appLogEntity.data["Note"], actual.data["Note"])
    }
}
