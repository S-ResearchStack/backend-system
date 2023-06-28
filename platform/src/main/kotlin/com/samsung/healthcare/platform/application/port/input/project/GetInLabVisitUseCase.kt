package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.flow.Flow

interface GetInLabVisitUseCase {
    suspend fun getInLabVisitById(projectId: String, inLabVisitId: Int): InLabVisit

    suspend fun getInLabVisits(projectId: String): Flow<InLabVisit>
}
