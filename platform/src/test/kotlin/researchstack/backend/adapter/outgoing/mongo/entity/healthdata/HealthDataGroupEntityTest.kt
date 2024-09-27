package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST

@ExperimentalCoroutinesApi
internal class HealthDataGroupEntityTest {
    private val healthDataGroupEntity = HealthDataGroupEntity(
        name = "Heart Rate",
        types = listOf("HR", "BPM")
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `HealthDataGroupEntity should have all properties set correctly`() = runTest {
        assertEquals("Heart Rate", healthDataGroupEntity.name)
        assertEquals(listOf("HR", "BPM"), healthDataGroupEntity.types)
    }
}
