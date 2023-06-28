package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.InLabVisit

interface CreateInLabVisitUseCase {
    suspend fun createInLabVisit(projectId: String, command: CreateInLabVisitCommand): InLabVisit
}
