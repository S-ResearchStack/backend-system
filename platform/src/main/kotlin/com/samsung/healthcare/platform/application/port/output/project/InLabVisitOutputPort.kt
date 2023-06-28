package com.samsung.healthcare.platform.application.port.output.project

import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.flow.Flow

interface InLabVisitOutputPort {
    suspend fun create(inLabVisit: InLabVisit): InLabVisit

    suspend fun update(inLabVisit: InLabVisit): InLabVisit

    suspend fun findById(id: Int): InLabVisit?

    suspend fun findAll(): Flow<InLabVisit>
}
