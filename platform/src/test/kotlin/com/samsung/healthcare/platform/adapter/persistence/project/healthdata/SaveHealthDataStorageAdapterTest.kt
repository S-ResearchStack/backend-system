package com.samsung.healthcare.platform.adapter.persistence.project.healthdata

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.OXYGEN_SATURATION
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.WEIGHT
import com.samsung.healthcare.platform.domain.project.healthdata.OxygenSaturation
import com.samsung.healthcare.platform.domain.project.healthdata.Weight
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.UUID

@ExtendWith(SpringExtension::class)
@DataR2dbcTest
@Import(
    HealthDataRepositoryLookup::class,
    SaveHealthDataStorageAdapter::class
)
@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class SaveHealthDataStorageAdapterTest @Autowired constructor(
    private val healthDataStorageAdapter: SaveHealthDataStorageAdapter
) {
    @MockkBean(relaxed = true)
    private lateinit var oxygenSaturationRepository: OxygenSaturationRepository

    @MockkBean(relaxed = true)
    private lateinit var weightRepository: WeightRepository

    private val userId = UserId.from(UUID.randomUUID().toString())

    @Test
    @Tag(POSITIVE_TEST)
    fun `should call saveAll method of OxygenSaturationRepository when health-data type is SPO2`() = runTest {
        val spo2 = OxygenSaturation.newOxygenSaturation(Instant.now(), 77.0)

        healthDataStorageAdapter.save(userId, OXYGEN_SATURATION, listOf(spo2))

        verify(exactly = 1) {
            oxygenSaturationRepository.saveAll(listOf(spo2.toEntity(userId)))
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should call saveAll method of WeightRepository when health-data type is weight`() = runTest {
        val weight = Weight.newWeight(Instant.now(), 35.2)

        healthDataStorageAdapter.save(userId, WEIGHT, listOf(weight))

        verify(exactly = 1) {
            weightRepository.saveAll(listOf(weight.toEntity(userId)))
        }
    }
}
