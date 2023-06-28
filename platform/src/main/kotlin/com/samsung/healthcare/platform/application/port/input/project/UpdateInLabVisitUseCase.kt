package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.InLabVisit

interface UpdateInLabVisitUseCase {
    suspend fun updateInLabVisit(projectId: String, inLabVisitId: Int, command: UpdateInLabVisitCommand): InLabVisit
}
