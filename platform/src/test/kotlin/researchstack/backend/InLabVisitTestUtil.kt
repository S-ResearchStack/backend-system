package researchstack.backend

import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitCommand
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitCommand
import researchstack.backend.domain.inlabvisit.InLabVisit
import java.time.LocalDateTime

class InLabVisitTestUtil {
    companion object {
        const val id = "testId"
        const val studyId = "testStudy"
        const val creatorId = "testCreator"
        const val picId = "testPic"
        const val subjectNumber = "testSubject"
        val startTime = LocalDateTime.now()
        val endTime = startTime.plusHours(1)
        val createdAt = LocalDateTime.now()

        fun getCreateInLabVisitCommand() = CreateInLabVisitCommand(
            picId = picId,
            subjectNumber = subjectNumber,
            startTime = startTime,
            endTime = endTime
        )

        fun getUpdateInLabVisitCommand() = UpdateInLabVisitCommand(
            picId = picId,
            startTime = startTime,
            endTime = endTime
        )

        fun getInLabVisit() = InLabVisit(
            id = id,
            picId = picId,
            subjectNumber = subjectNumber,
            creatorId = creatorId,
            startTime = startTime,
            endTime = endTime,
            createdAt = createdAt
        )
    }
}
