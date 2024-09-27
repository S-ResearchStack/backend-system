package researchstack.backend.adapter.incoming.rest.studydata

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.common.PaginationCommand
import researchstack.backend.application.port.incoming.studydata.AddStudyDataCommand
import researchstack.backend.application.port.incoming.studydata.AddStudyDataUseCase
import researchstack.backend.application.port.incoming.studydata.GetStudyDataUseCase
import researchstack.backend.application.port.incoming.studydata.GetSubjectUseCase
import researchstack.backend.application.port.incoming.studydata.UpdateSubjectUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.enums.StudyDataType
import researchstack.backend.enums.SubjectStatus
import researchstack.backend.util.validateContext
import researchstack.backend.util.validateEnum

@Component
class StudyDataRestController(
    private val getSubjectUseCase: GetSubjectUseCase,
    private val updateSubjectUseCase: UpdateSubjectUseCase,
    private val addStudyDataUseCase: AddStudyDataUseCase,
    private val getStudyDataUseCase: GetStudyDataUseCase
) {
    @Get("/studies/{studyId}/subjects")
    suspend fun getSubjectInfoList(
        @Param("studyId") studyId: String,
        @Param("includeTaskProgress") includeTaskProgress: Boolean? = false,
        @RequestObject paginationCommand: PaginationCommand? = null
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val users = getSubjectUseCase.getSubjectInfoList(
            studyId = studyId,
            includeTaskProgress = includeTaskProgress,
            paginationCommand = paginationCommand
        )
        return HttpResponse.of(JsonHandler.toJson(users))
    }

    @Get("/studies/{studyId}/subjects/count")
    suspend fun getSubjectInfoListCount(
        @Param("studyId") studyId: String
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val count = getSubjectUseCase.getSubjectInfoListCount(studyId)
        return HttpResponse.of(JsonHandler.toJson(count))
    }

    @Patch("/studies/{studyId}/subjects/{subjectNumber}")
    suspend fun setSubjectStatus(
        @Param("studyId") studyId: String,
        @Param("subjectNumber") subjectNumber: String,
        @Param("status") status: String
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(subjectNumber, ExceptionMessage.EMPTY_SUBJECT_NUMBER)
        validateEnum<SubjectStatus>(status, ExceptionMessage.INVALID_SUBJECT_STATUS)
        updateSubjectUseCase.updateSubjectStatus(
            studyId = studyId,
            subjectNumber = subjectNumber,
            status = SubjectStatus.valueOf(status)
        )
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/studies/{studyId}/study-data/{parentId}/children")
    suspend fun getStudyDataInfoList(
        @Param("studyId") studyId: String,
        @Param("parentId") parentId: String,
        @Param("studyDataType") studyDataType: String,
        @RequestObject paginationCommand: PaginationCommand? = null
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(parentId, ExceptionMessage.EMPTY_STUDY_DATA_PARENT_ID)
        validateEnum<StudyDataType>(studyDataType, ExceptionMessage.INVALID_STUDY_DATA_TYPE)

        val studyDataInfoList = getStudyDataUseCase.getStudyDataInfoList(
            studyId,
            parentId,
            StudyDataType.valueOf(studyDataType),
            paginationCommand
        )

        return HttpResponse.of(JsonHandler.toJsonWithStudyDataInfo(studyDataInfoList))
    }

    @Get("/studies/{studyId}/study-data/{parentId}/children/count")
    suspend fun getStudyDataInfoListCount(
        @Param("studyId") studyId: String,
        @Param("parentId") parentId: String,
        @Param("studyDataType") studyDataType: String
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(parentId, ExceptionMessage.EMPTY_STUDY_DATA_PARENT_ID)
        validateEnum<StudyDataType>(studyDataType, ExceptionMessage.INVALID_STUDY_DATA_TYPE)

        val studyDataCount = getStudyDataUseCase.getStudyDataInfoListCount(
            studyId = studyId,
            parentId = parentId,
            studyDataType = StudyDataType.valueOf(studyDataType)
        )
        return HttpResponse.of(JsonHandler.toJsonWithZonedDateTime(studyDataCount))
    }

    @Post("/studies/{studyId}/study-data")
    suspend fun addStudyDataInfo(
        @Param("studyId") studyId: String,
        @RequestObject studyDataCommand: AddStudyDataCommand
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(studyDataCommand.parentId, ExceptionMessage.EMPTY_STUDY_DATA_PARENT_ID)
        validateContext(studyDataCommand.name, ExceptionMessage.EMPTY_STUDY_DATA_NAME)

        addStudyDataUseCase.addStudyDataInfo(studyId, studyDataCommand)
        return HttpResponse.of(HttpStatus.OK)
    }
}
