package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.InLabVisitEntity
import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.domain.project.InLabVisit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class InLabVisitDatabaseAdapterTest {
    private val inLabVisitRepository = mockk<InLabVisitRepository>()

    private val inLabVisitDatabaseAdapter = InLabVisitDatabaseAdapter(inLabVisitRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `create should not emit event`() = runTest {
        val time = LocalDateTime.now()
        val reqInLabVisit = InLabVisit(1, "u1", "c1", time, time, "note", null)
        val resInLabVisitEntity = reqInLabVisit.toEntity()

        Assertions.assertThat(resInLabVisitEntity.id).isEqualTo(reqInLabVisit.id)
        Assertions.assertThat(resInLabVisitEntity.userId).isEqualTo(reqInLabVisit.userId)
        Assertions.assertThat(resInLabVisitEntity.checkedInBy).isEqualTo(reqInLabVisit.checkedInBy)
        Assertions.assertThat(resInLabVisitEntity.startTime).isEqualTo(reqInLabVisit.startTime)
        Assertions.assertThat(resInLabVisitEntity.endTime).isEqualTo(reqInLabVisit.endTime)
        Assertions.assertThat(resInLabVisitEntity.notes).isEqualTo(reqInLabVisit.notes)

        coEvery {
            inLabVisitRepository.save(reqInLabVisit.toEntity())
        } returns resInLabVisitEntity

        val result = inLabVisitDatabaseAdapter.create(reqInLabVisit)
        Assertions.assertThat(result).isEqualTo(resInLabVisitEntity.toDomain())
        Assertions.assertThat(result.filesPath).isEqualTo("in-lab-visit/1")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw IllegalArgumentException if the given id was null`() = runTest {
        val time = LocalDateTime.now()
        val reqInLabVisit = InLabVisit(1, "u1", "c1", time, time, "note", null)

        coEvery {
            inLabVisitRepository.findById(1)
        } returns null

        assertThrows<java.lang.IllegalArgumentException> { inLabVisitDatabaseAdapter.update(reqInLabVisit) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw IllegalArgumentException if no data found for given id`() = runTest {
        val time = LocalDateTime.now()
        val reqInLabVisit = InLabVisit(null, "u1", "c1", time, time, "note", null)

        assertThrows<java.lang.IllegalArgumentException> { inLabVisitDatabaseAdapter.update(reqInLabVisit) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `update should not emit event`() = runTest {
        val time = LocalDateTime.now()
        val reqInLabVisit = InLabVisit(1, "u1-new", "c1-new", time.plusDays(1), time.plusDays(1), "new", null)
        val beforeInLabVisitEntity = InLabVisitEntity(1, "u1", "c1", time, time, "note")
        val afterInLabVisitEntity = InLabVisitEntity(1, "u1-new", "c1-new", time.plusDays(1), time.plusDays(1), "new")

        coEvery {
            inLabVisitRepository.findById(1)
        } returns beforeInLabVisitEntity
        coEvery {
            inLabVisitRepository.save(afterInLabVisitEntity)
        } returns afterInLabVisitEntity

        Assertions.assertThat(inLabVisitDatabaseAdapter.update(reqInLabVisit))
            .isEqualTo(afterInLabVisitEntity.toDomain())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findById should not emit event`() = runTest {
        coEvery {
            inLabVisitRepository.findById(1)
        } returns null

        Assertions.assertThat(inLabVisitDatabaseAdapter.findById(1)).isEqualTo(null)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findAll should not emit event`() = runTest {
        coEvery {
            inLabVisitRepository.findAll()
        } returns emptyFlow()

        Assertions.assertThat(inLabVisitDatabaseAdapter.findAll().toList()).isEqualTo(emptyList<InLabVisit>())
    }
}
