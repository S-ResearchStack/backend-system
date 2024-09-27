package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.studydata.SubjectInfoResponse
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.domain.task.TaskSpecSummary
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
abstract class SubjectInfoMapper {
    abstract fun toResponse(
        subjectInfo: SubjectInfo,
        lastSyncTime: LocalDateTime?,
        totalTaskCount: Number?,
        undoneTaskList: List<TaskSpecSummary>?
    ): SubjectInfoResponse
}

private val converter = Mappers.getMapper(SubjectInfoMapper::class.java)

fun SubjectInfo.toResponse(
    lastSyncTime: LocalDateTime? = null,
    totalTaskCount: Number? = null,
    undoneTaskList: List<TaskSpecSummary>? = null
): SubjectInfoResponse =
    converter.toResponse(
        subjectInfo = this,
        lastSyncTime = lastSyncTime,
        totalTaskCount = totalTaskCount,
        undoneTaskList = undoneTaskList
    )
