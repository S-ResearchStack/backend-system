package researchstack.backend.application.service.file

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.storage.UploadObjectPort
import researchstack.backend.application.port.outgoing.storage.UploadPresignedUrlResponse
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort
import researchstack.backend.domain.common.Url
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

@ExperimentalCoroutinesApi
internal class FileServiceTest {
    private val uploadObjectPort = mockk<UploadObjectPort>()
    private val downloadObjectPort = mockk<DownloadObjectPort>()
    private val getStudyOutPort = mockk<GetStudyOutPort>()
    private val fileService = FileService(uploadObjectPort, downloadObjectPort)

    private val studyId = "study_id"
    private val firstStudy = StudyTestUtil.createDummyStudy(studyId)
    private val presignedUrl = URL("https://test")

    @BeforeEach
    fun setUp() {
        coEvery { getStudyOutPort.getStudy(any()) } returns firstStudy

        mockkStatic(RandomStringUtils::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(RandomStringUtils::class)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUploadPresignedUrl should work properly`() = runTest {
        val filePath = "file_path"
        coEvery {
            uploadObjectPort.getUploadPresignedUrl(any())
        } returns UploadPresignedUrlResponse(presignedUrl, mapOf())

        val response = fileService.getUploadPresignedUrl(studyId, filePath)
        Assertions.assertEquals(presignedUrl, response.presignedUrl)
        Assertions.assertEquals(mapOf<String, String>(), response.headers)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDownloadPresignedUrl should work properly`() = runTest {
        val filePath = "file_path"
        coEvery {
            downloadObjectPort.getDownloadPresignedUrl(any())
        } returns presignedUrl

        Assertions.assertEquals(Url(presignedUrl.toString()), fileService.getDownloadPresignedUrl(studyId, filePath))
    }

    private fun mockDownloadAndUpload(zipFilePath: Path, downloadDirectoryPath: Path) {
        coEvery {
            downloadObjectPort.doesObjectExist(any())
        } returns zipFilePath.exists()

        every {
            RandomStringUtils.randomAlphanumeric(any())
        } returns downloadDirectoryPath.toString()

        coEvery {
            downloadObjectPort.downloadDirectory(any(), any())
        } answers {
            Files.createFile(zipFilePath)
            Assertions.assertTrue(zipFilePath.exists())
            CompletedDirectoryDownload.builder().build()
        }

        coEvery {
            uploadObjectPort.uploadFile(any(), any())
        } answers {}

        coEvery {
            downloadObjectPort.getDownloadPresignedUrl(any())
        } returns presignedUrl
    }
}
