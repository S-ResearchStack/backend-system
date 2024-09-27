package researchstack.backend.application.service.education

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.EducationTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.education.CreateEducationalContentOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.enums.ScratchContentBlockType

@ExperimentalCoroutinesApi
internal class CreateEducationalContentServiceTest {
    private val createEducationalContentOutPort = mockk<CreateEducationalContentOutPort>()
    private val createEducationalContentService = CreateEducationalContentService(
        createEducationalContentOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalPDFContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(EducationalContentType.PDF)

        coEvery {
            createEducationalContentOutPort.createEducationalContent(command.toDomain(userId))
        } returns EducationTestUtil.getEducationalContent(EducationTestUtil.contentId)

        assertDoesNotThrow {
            createEducationalContentService.createEducationalContent(
                studyId = studyId,
                investigatorId = userId,
                command = command
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalVideoContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(EducationalContentType.VIDEO)

        coEvery {
            createEducationalContentOutPort.createEducationalContent(command.toDomain(userId))
        } returns EducationTestUtil.getEducationalContent(EducationTestUtil.contentId)

        assertDoesNotThrow {
            createEducationalContentService.createEducationalContent(
                studyId = studyId,
                investigatorId = userId,
                command = command
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalScratchContent with TEXT block should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(
            EducationalContentType.SCRATCH,
            ScratchContentBlockType.TEXT
        )

        coEvery {
            createEducationalContentOutPort.createEducationalContent(command.toDomain(userId))
        } returns EducationTestUtil.getEducationalContent(EducationTestUtil.contentId)

        assertDoesNotThrow {
            createEducationalContentService.createEducationalContent(
                studyId = studyId,
                investigatorId = userId,
                command = command
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalScratchContent with IMAGE block should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(
            EducationalContentType.SCRATCH,
            ScratchContentBlockType.IMAGE
        )

        coEvery {
            createEducationalContentOutPort.createEducationalContent(command.toDomain(userId))
        } returns EducationTestUtil.getEducationalContent(EducationTestUtil.contentId)

        assertDoesNotThrow {
            createEducationalContentService.createEducationalContent(
                studyId = studyId,
                investigatorId = userId,
                command = command
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalScratchContent with VIDEO block should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(
            EducationalContentType.SCRATCH,
            ScratchContentBlockType.VIDEO
        )

        coEvery {
            createEducationalContentOutPort.createEducationalContent(command.toDomain(userId))
        } returns EducationTestUtil.getEducationalContent(EducationTestUtil.contentId)

        assertDoesNotThrow {
            createEducationalContentService.createEducationalContent(
                studyId = studyId,
                investigatorId = userId,
                command = command
            )
        }
    }
}
