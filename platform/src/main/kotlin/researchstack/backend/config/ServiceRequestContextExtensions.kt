package researchstack.backend.config

import com.linecorp.armeria.server.ServiceRequestContext
import researchstack.backend.adapter.exception.ExceptionMessage

fun ServiceRequestContext.getStudyId() =
    this.attr(STUDY_ID_KEY) ?: throw IllegalArgumentException(ExceptionMessage.EMPTY_STUDY_ID)

fun ServiceRequestContext.getUserId() =
    this.attr(USER_ID_KEY) ?: throw IllegalArgumentException(ExceptionMessage.EMPTY_USER_ID)

fun ServiceRequestContext.getEmail() =
    this.attr(USER_EMAIL_KEY) ?: throw IllegalArgumentException(ExceptionMessage.EMPTY_EMAIL)
