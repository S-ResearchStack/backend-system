package researchstack.backend.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class ValidationTest {
    private val userId = "test-subject-id"
    private val studyId = "test-study-id"
    private val subjectNumber = "test-subject-number"
    private val sessionId = "test-session-id"
    private val taskId = "test-task-id"
    private val trialId = "test-trial-id"
    private val email = "test-email"
    private val participationCode = "test-participation-code"
    private val filePath = "test-file-path"

    @Test
    @Tag(POSITIVE_TEST)
    fun `validateContext should work properly`() = runTest {
        assertDoesNotThrow {
            validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
            validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
            validateContext(subjectNumber, ExceptionMessage.EMPTY_SUBJECT_NUMBER)
            validateContext(sessionId, ExceptionMessage.EMPTY_SESSION_ID)
            validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
            validateContext(trialId, ExceptionMessage.EMPTY_TRIAL_ID)
            validateContext(email, ExceptionMessage.EMPTY_EMAIL)
            validateContext(participationCode, ExceptionMessage.EMPTY_PARTICIPATION_CODE)
            validateContext(filePath, ExceptionMessage.EMPTY_FILEPATH)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if subjectNumber is empty`(subjectNumber: String) =
        runTest {
            assertThrows<IllegalArgumentException> {
                validateContext(subjectNumber, ExceptionMessage.EMPTY_SUBJECT_NUMBER)
            }
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if sessionId is empty`(sessionId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(sessionId, ExceptionMessage.EMPTY_SESSION_ID)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if taskId is empty`(taskId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if trialId is empty`(trialId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(trialId, ExceptionMessage.EMPTY_TRIAL_ID)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if email is empty`(email: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(email, ExceptionMessage.EMPTY_EMAIL)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if participationCode is empty`(participationCode: String) =
        runTest {
            assertThrows<IllegalArgumentException> {
                validateContext(participationCode, ExceptionMessage.EMPTY_PARTICIPATION_CODE)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `validateGoogleAuthCode should work properly`() = runTest {
        assertDoesNotThrow {
            validateGoogleAuthCode("test-code")
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateGoogleAuthCode should throw IllegalArgumentException if code is empty`(code: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateGoogleAuthCode(code)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateContext should throw IllegalArgumentException if filePath is empty`(filePath: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateContext(filePath, ExceptionMessage.EMPTY_FILEPATH)
        }
    }

    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @ValueSource(strings = ["IDLE", "PARTICIPATING", "WITHDRAWN", "COMPLETED"])
    fun `validateStatus should work properly`(status: String) = runTest {
        assertDoesNotThrow {
            validateEnum<SubjectStatus>(status, ExceptionMessage.INVALID_SUBJECT_STATUS)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `validateStatus should throw IllegalArgumentException if status is empty`(status: String) = runTest {
        assertThrows<IllegalArgumentException> {
            validateEnum<SubjectStatus>(status, ExceptionMessage.INVALID_SUBJECT_STATUS)
        }
    }
}
