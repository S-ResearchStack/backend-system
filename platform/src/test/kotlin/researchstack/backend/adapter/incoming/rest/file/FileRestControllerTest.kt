package researchstack.backend.adapter.incoming.rest.file

import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.file.GetFileUseCase
import researchstack.backend.application.port.incoming.file.GetZippedFileUseCase
import researchstack.backend.application.port.incoming.file.UploadObjectUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.common.Url
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class FileRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val getFileUseCase = mockk<GetFileUseCase>()
    private val uploadObjectUseCase = mockk<UploadObjectUseCase>()
    private val getZippedFileUseCase = mockk<GetZippedFileUseCase>()
    private val fileRestController = FileRestController(getFileUseCase, uploadObjectUseCase, getZippedFileUseCase)
    private val filePath = "test-study/test-subject/test-session/test-task/test-trial"
    private val investigatorId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val studyId = "test-mental-care-study"
    private val presignedUrl = "https://test.s3.test.aws.com/test-data.zip"

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery { ServiceRequestContext.current() } returns serviceRequestContext
        coEvery { serviceRequestContext.getUserId() } returns investigatorId
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getFileDownloadUrls should throw IllegalArgumentException if filePath is empty`() = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            fileRestController.getDownloadUrls(studyId, listOf())
        }
        assertEquals(ExceptionMessage.EMPTY_FILEPATH_LIST, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getFileDownloadUrls should throw IllegalArgumentException if one of filePath in filePaths is empty`() =
        runTest {
            val exception = assertThrows<IllegalArgumentException> {
                fileRestController.getDownloadUrls(studyId, listOf(filePath, ""))
            }
            assertEquals(ExceptionMessage.EMPTY_FILEPATH_LIST, exception.message)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getFileDownloadUrls should work properly`() = runTest {
        coEvery {
            getFileUseCase.getDownloadPresignedUrl(studyId, filePath)
        } returns Url(filePath)

        val actual = fileRestController.getDownloadUrls(studyId, listOf(filePath)).aggregate().join()
        assertEquals(HttpStatus.OK, actual.status())
    }
}
