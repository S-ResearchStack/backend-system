package researchstack.backend.adapter.incoming.grpc.file

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.file.GetPresignedUrlResponse
import researchstack.backend.application.port.incoming.file.UploadObjectUseCase
import researchstack.backend.grpc.GetPresignedUrlRequest
import java.net.URL

@ExperimentalCoroutinesApi
internal class FileGrpcControllerTest {
    private val uploadObjectUseCase = mockk<UploadObjectUseCase>()
    private val fileGrpcController = FileGrpcController(uploadObjectUseCase)

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getPresignedUrl should throw IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            fileGrpcController.getPresignedUrl(
                GetPresignedUrlRequest.newBuilder().setStudyId(studyId).setFileName("file_name").build()
            )
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getPresignedUrl should throw IllegalArgumentException when fileName was empty`(fileName: String) = runTest {
        assertThrows<IllegalArgumentException> {
            fileGrpcController.getPresignedUrl(
                GetPresignedUrlRequest.newBuilder().setStudyId("study_id").setFileName(fileName).build()
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getPresignedUrl should work properly`() = runTest {
        val studyId = "study_id"
        val fileName = "file_name"
        coEvery {
            uploadObjectUseCase.getUploadPresignedUrl(studyId, fileName)
        } returns GetPresignedUrlResponse(URL("https://test"), mapOf())

        val response = fileGrpcController.getPresignedUrl(
            GetPresignedUrlRequest.newBuilder().setStudyId(studyId).setFileName(fileName).build()
        )

        Assertions.assertEquals("https://test", response.presignedUrl)
    }
}
