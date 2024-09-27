package researchstack.backend.adapter.incoming.rest.study

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.study.CreateStudyCommand
import researchstack.backend.application.port.incoming.study.CreateStudyUseCase
import researchstack.backend.application.port.incoming.study.DeleteStudyUseCase
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.incoming.study.UpdateStudyCommand
import researchstack.backend.application.port.incoming.study.UpdateStudyUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.util.validateContext

@Component
class StudyRestController(
    private val createStudyUseCase: CreateStudyUseCase,
    private val getStudyUseCase: GetStudyUseCase,
    private val updateStudyUseCase: UpdateStudyUseCase,
    private val deleteStudyUseCase: DeleteStudyUseCase
) {
    @Post("/studies")
    suspend fun createStudy(@RequestObject createStudyCommand: CreateStudyCommand): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val createStudyResponse = createStudyUseCase.createStudy(userId, createStudyCommand)
        return HttpResponse.of(JsonHandler.toJson(createStudyResponse))
    }

    @Get("/study-create-commands")
    suspend fun getCreateStudyCommand(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val createStudyCommand = getStudyUseCase.getCreateStudyCommand()

        return HttpResponse.of(JsonHandler.toJson(createStudyCommand))
    }

    @Get("/studies")
    suspend fun getStudyList(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val studies = getStudyUseCase.getStudyListByUser(userId)
        return HttpResponse.of(JsonHandler.toJson(studies))
    }

    // TODO: Get taskInfoList from database
    private val dataIdList = listOf(
        "WatchIHRNRaw",
        "WatchECGRaw",
        "WatchBIARaw",
        "WatchBPRaw",
        "WatchSpO2Raw",
        "WatchStress"
    )
    private val collectionMethodList = listOf(
        "Random Check",
        "Spot Check"
    )
    private val taskInfoList = TaskInfoList(
        dataIdList,
        collectionMethodList
    )

    @Get("/task-info-list")
    suspend fun getStudyTaskInfoList(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        return HttpResponse.of(JsonHandler.toJson(taskInfoList))
    }

    @Get("/studies/{studyId}")
    suspend fun getStudy(@Param("studyId") studyId: String): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val study = getStudyUseCase.getStudy(studyId)
        return HttpResponse.of(JsonHandler.toJson(study))
    }

    @Patch("/studies/{studyId}")
    suspend fun updateStudy(
        @Param("studyId") studyId: String,
        @RequestObject updateStudyCommand: UpdateStudyCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        updateStudyUseCase.updateStudy(studyId, updateStudyCommand)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}")
    suspend fun deleteStudy(@Param("studyId") studyId: String): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        deleteStudyUseCase.deleteStudy(studyId)
        return HttpResponse.of(HttpStatus.OK)
    }

    companion object {
        data class TaskInfoList(
            val dataIdList: List<String>,
            val collectionMethodList: List<String>
        )
    }
}
