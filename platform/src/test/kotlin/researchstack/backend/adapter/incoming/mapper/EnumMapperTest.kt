package researchstack.backend.adapter.incoming.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals
import researchstack.backend.grpc.HealthData.HealthDataType as GrpcHealthDataType

@ExperimentalCoroutinesApi
internal class EnumMapperTest {
    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @EnumSource(value = StudyScope::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `studyScope mapper should map properly`(studyScope: StudyScope) = runTest {
        val studyScopePrefix = "STUDY_SCOPE_"
        val grpcStudyScope = studyScope.toGrpc()

        assertEquals(studyScopePrefix.plus(studyScope), grpcStudyScope.name)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `studyScope mapper should throw error when unspecified is used`() = runTest {
        val studyScope = StudyScope.UNSPECIFIED
        assertThrows<IllegalArgumentException> {
            studyScope.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = StudyStage::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `studyStage mapper should map properly`(studyStage: StudyStage) = runTest {
        val studyStagePrefix = "STUDY_STAGE_"
        val grpcStudyStage = studyStage.toGrpc()

        assertEquals(studyStagePrefix + studyStage.name, grpcStudyStage.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `studyStage mapper should throw error when unspecified is used`() = runTest {
        val studyStage = StudyStage.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            studyStage.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = IrbDecisionType::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `irbDecisionType mapper should map properly`(irbDecisionType: IrbDecisionType) = runTest {
        val irbDecisionTypePrefix = "IRB_DECISION_TYPE_"
        val grpcIrbDecisionType = irbDecisionType.toGrpc()

        assertEquals(irbDecisionTypePrefix.plus(irbDecisionType.name), grpcIrbDecisionType.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `irbDecisionType mapper should throw error when unspecified is used`() = runTest {
        val irbDecisionType = IrbDecisionType.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            irbDecisionType.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = StudyParticipationApprovalType::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `studyParticipationApprovalType mapper should map properly`(studyParticipationApprovalType: StudyParticipationApprovalType) =
        runTest {
            val studyParticipationApprovalTypePrefix = "STUDY_PARTICIPATION_APPROVAL_TYPE_"
            val grpcStudyParticipationApprovalType = studyParticipationApprovalType.toGrpc()

            assertEquals(
                studyParticipationApprovalTypePrefix.plus(studyParticipationApprovalType.name),
                grpcStudyParticipationApprovalType.name
            )
        }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `studyParticipationApprovalType mapper should throw error when unspecified is used`() = runTest {
        val studyParticipationApprovalType = StudyParticipationApprovalType.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            studyParticipationApprovalType.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = ActivityType::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `activityType mapper should map properly`(activityType: ActivityType) = runTest {
        val activityTypePrefix = "ACTIVITY_TYPE_"
        val grpcActivityType = activityType.toGrpc()

        assertEquals(activityTypePrefix.plus(activityType.name), grpcActivityType.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `activityType mapper should throw error when unspecified is used`() = runTest {
        val activityType = ActivityType.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            activityType.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = QuestionTag::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `questionTag mapper should map properly`(questionTag: QuestionTag) = runTest {
        val questionTagPrefix = "TAG_"
        val grpcTag = questionTag.toGrpc()

        assertEquals(questionTagPrefix.plus(questionTag.value.uppercase()), grpcTag.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `questionTag mapper should throw error when unspecified is used`() = runTest {
        val questionTag = QuestionTag.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            questionTag.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = HealthDataType::class, mode = EnumSource.Mode.EXCLUDE, names = ["UNSPECIFIED"])
    fun `healthDataType mapper should map properly`(healthDataType: HealthDataType) = runTest {
        val healthDataTypePrefix = "HEALTH_DATA_TYPE_"
        val grpcHealthDataType = healthDataType.toGrpc()

        assertEquals(healthDataTypePrefix.plus(healthDataType.name), grpcHealthDataType.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `healthDataType mapper should throw error when unspecified is used`() = runTest {
        val healthDataType = HealthDataType.UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            healthDataType.toGrpc()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(
        value = GrpcHealthDataType::class,
        mode = EnumSource.Mode.EXCLUDE,
        names = ["HEALTH_DATA_TYPE_UNSPECIFIED", "UNRECOGNIZED"]
    )
    fun `grpcHealthDataType mapper should map properly`(grpcHealthDataType: GrpcHealthDataType) = runTest {
        val grpcHealthDataTypePrefix = "HEALTH_DATA_TYPE_"
        val healthDataType = grpcHealthDataType.toDomain()

        assertEquals(grpcHealthDataType.name.replace(grpcHealthDataTypePrefix, ""), healthDataType.name)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `grpcHealthDataType mapper should throw error when unspecified is used`() = runTest {
        val grpcHealthDataType = GrpcHealthDataType.HEALTH_DATA_TYPE_UNSPECIFIED

        assertThrows<IllegalArgumentException> {
            grpcHealthDataType.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = SubjectStatus::class, mode = EnumSource.Mode.EXCLUDE, names = ["IDLE", "HIDDEN"])
    fun `subjectStatus mapper should map properly`(subjectStatus: SubjectStatus) = runTest {
        val subjectStatusPrefix = "SUBJECT_STATUS_"
        val grpcUserStatus = subjectStatus.toGrpc()

        assertEquals(subjectStatusPrefix.plus(subjectStatus.name), grpcUserStatus.name)
    }

    @Tag(NEGATIVE_TEST)
    @ParameterizedTest
    @EnumSource(value = SubjectStatus::class, mode = EnumSource.Mode.INCLUDE, names = ["IDLE", "HIDDEN"])
    fun `subjectStatus mapper should throw error when unsupported is used`(subjectStatus: SubjectStatus) = runTest {
        assertThrows<IllegalArgumentException> {
            subjectStatus.toGrpc()
        }
    }
}
