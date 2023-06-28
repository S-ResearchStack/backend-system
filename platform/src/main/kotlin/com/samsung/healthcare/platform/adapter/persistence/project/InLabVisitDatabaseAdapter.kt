package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class InLabVisitDatabaseAdapter(
    private val inLabVisitRepository: InLabVisitRepository,
) : InLabVisitOutputPort {
    override suspend fun create(inLabVisit: InLabVisit): InLabVisit =
        inLabVisitRepository.save(inLabVisit.toEntity()).toDomain()

    override suspend fun update(inLabVisit: InLabVisit): InLabVisit {
        requireNotNull(inLabVisit.id)
        return inLabVisitRepository.findById(inLabVisit.id)?.copy(
            userId = inLabVisit.userId,
            checkedInBy = inLabVisit.checkedInBy,
            startTime = inLabVisit.startTime,
            endTime = inLabVisit.endTime,
            notes = inLabVisit.notes,
        ).let {
            requireNotNull(it)
            inLabVisitRepository.save(it).toDomain()
        }
    }

    override suspend fun findById(id: Int): InLabVisit? = inLabVisitRepository.findById(id)?.toDomain()

    override suspend fun findAll(): Flow<InLabVisit> = inLabVisitRepository.findAll().map { it.toDomain() }
}
