package researchstack.backend.application.service.studydata

import org.quartz.CronExpression
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_STUDY_DATA
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.common.PaginationCommand
import researchstack.backend.application.port.incoming.studydata.GetStudyDataUseCase
import researchstack.backend.application.port.incoming.studydata.GetSubjectUseCase
import researchstack.backend.application.port.incoming.studydata.StudyDataCountResponse
import researchstack.backend.application.port.incoming.studydata.SubjectInfoResponse
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.studydata.GetStudyDataOutPort
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.task.GetTaskResultOutPort
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.application.service.mapper.toResponse
import researchstack.backend.domain.studydata.StudyDataFolder
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.domain.task.TaskSpecSummary
import researchstack.backend.enums.StudyDataType
import researchstack.backend.enums.TaskType
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class GetStudyDataService(
    private val getSubjectInfoOutPort: GetSubjectInfoOutPort,
    private val downloadObjectPort: DownloadObjectPort,
    private val getTaskSpecOutPort: GetTaskSpecOutPort,
    private val getTaskResultOutPort: GetTaskResultOutPort,
    private val getStudyDataOutPort: GetStudyDataOutPort
) : StudyDataBaseService(downloadObjectPort),
    GetSubjectUseCase,
    GetStudyDataUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_STUDY_DATA])
    override suspend fun getSubjectInfoList(
        @Tenants
        studyId: String,
        includeTaskProgress: Boolean?,
        paginationCommand: PaginationCommand?
    ): List<SubjectInfoResponse> {
        val subjectInfoList = getSubjectInfoOutPort.getSubjectInfoList(
            studyId = studyId,
            page = paginationCommand?.page,
            size = paginationCommand?.size
        )

        return if (includeTaskProgress == true) {
            val now = Instant.now()
            val twoWeeksAgo = now.minus(14, ChronoUnit.DAYS)
            val taskSpecList = getTaskSpecOutPort.getTaskSpecs(studyId)
            subjectInfoList.map { getSubjectInfoResponse(it, now, twoWeeksAgo, taskSpecList) }
        } else {
            subjectInfoList.map { it.toResponse() }
        }
    }

    private suspend fun getSubjectInfoResponse(
        subjectInfo: SubjectInfo,
        now: Instant,
        twoWeeksAgo: Instant,
        taskSpecList: List<TaskSpec>
    ): SubjectInfoResponse {
        val taskResultMap = mutableMapOf<String, MutableList<TaskResult>>()
        val taskResultList = getTaskResultOutPort.getTaskResultList(
            studyId = subjectInfo.studyId,
            subjectId = subjectInfo.subjectId,
            startTime = twoWeeksAgo.atOffset(ZoneOffset.UTC).toLocalDateTime(),
            endTime = now.plus(1, ChronoUnit.DAYS).atOffset(ZoneOffset.UTC).toLocalDateTime()
        ).map { taskResult ->
            if (taskResultMap.containsKey(taskResult.taskId)) {
                taskResultMap[taskResult.taskId]?.add(taskResult)
            } else {
                taskResultMap[taskResult.taskId] = mutableListOf(taskResult)
            }
            taskResult
        }
        var totalTaskCount = 0
        val undoneTaskList = mutableListOf<TaskSpecSummary>()
        taskSpecList.map { task ->
            var time = Instant.from(twoWeeksAgo)
            val cronJob = CronExpression(task.schedule)

            while (true) {
                time = cronJob.getNextValidTimeAfter(Date.from(time))?.toInstant()
                if (time == null || time > now) break

                val startTime = time
                val endTime = time.plus(task.validMin, ChronoUnit.MINUTES)

                val result = taskResultMap[task.id]?.find { taskResult ->
                    val taskResultTime = Instant.ofEpochMilli(taskResult.startedAt)
                    taskResultTime in startTime..endTime
                }
                ++totalTaskCount
                if (result == null) {
                    val name =
                        if (task.taskType == TaskType.SURVEY) {
                            task.title
                        } else {
                            (task.task as ActivityTask).type.name
                        }

                    undoneTaskList.add(
                        TaskSpecSummary(
                            id = task.id,
                            type = task.taskType,
                            name = name,
                            time = startTime.atOffset(ZoneOffset.UTC).toLocalDateTime()
                        )
                    )
                }
            }
        }

        // Currently, only the last task within 2 weeks is retrieved.
        val lastTask = taskResultList.maxByOrNull { taskResult -> taskResult.finishedAt }
        val lastSyncTime = lastTask?.let {
            Instant.ofEpochMilli(it.finishedAt).atOffset(ZoneOffset.UTC).toLocalDateTime()
        }

        return subjectInfo.toResponse(
            lastSyncTime = lastSyncTime,
            totalTaskCount = totalTaskCount,
            undoneTaskList = undoneTaskList
        )
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_STUDY_DATA])
    override suspend fun getSubjectInfoListCount(@Tenants studyId: String): StudyDataCountResponse {
        val count = getSubjectInfoOutPort.getSubjectInfoListCount(studyId = studyId)
        return StudyDataCountResponse(totalCount = count)
    }

    override suspend fun getStudyDataInfoList(
        studyId: String,
        parentId: String,
        studyDataType: StudyDataType,
        paginationCommand: PaginationCommand?
    ): List<StudyDataFolder> {
        return if (studyDataType == StudyDataType.FOLDER) {
            getStudyDataOutPort.getStudyDataFolderList(
                studyId,
                parentId,
                paginationCommand?.page,
                paginationCommand?.size
            )
        } else {
            getStudyDataOutPort.getStudyDataFileList(
                studyId,
                parentId,
                paginationCommand?.page,
                paginationCommand?.size
            )
        }
    }

    override suspend fun getStudyDataInfoListCount(
        studyId: String,
        parentId: String,
        studyDataType: StudyDataType
    ): StudyDataCountResponse {
        val count = if (studyDataType == StudyDataType.FOLDER) {
            getStudyDataOutPort.getStudyDataFolderListCount(
                studyId,
                parentId
            )
        } else {
            getStudyDataOutPort.getStudyDataFileListCount(
                studyId,
                parentId
            )
        }
        return StudyDataCountResponse(totalCount = count)
    }
}
