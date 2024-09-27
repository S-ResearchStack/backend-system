package researchstack.backend.adapter.incoming.mapper

import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import researchstack.backend.enums.SubjectStatus
import researchstack.backend.grpc.ActivityType as GrpcActivityType
import researchstack.backend.grpc.HealthData.HealthDataType as GrpcHealthDataType
import researchstack.backend.grpc.IrbDecisionType as GrpcIrbDecisionType
import researchstack.backend.grpc.Question.TAG as GrpcTag
import researchstack.backend.grpc.StudyParticipationApprovalType as GrpcStudyParticipationApprovalType
import researchstack.backend.grpc.StudyScope as GrpcStudyScope
import researchstack.backend.grpc.StudyStage as GrpcStudyStage
import researchstack.backend.grpc.SubjectStatus as GrpcSubjectStatus

fun StudyScope.toGrpc(): GrpcStudyScope =
    when (this) {
        StudyScope.PUBLIC -> GrpcStudyScope.STUDY_SCOPE_PUBLIC
        StudyScope.PRIVATE -> GrpcStudyScope.STUDY_SCOPE_PRIVATE
        else -> throw IllegalArgumentException("unsupported StudyScope")
    }

fun StudyStage.toGrpc(): GrpcStudyStage =
    when (this) {
        StudyStage.CREATED -> GrpcStudyStage.STUDY_STAGE_CREATED
        StudyStage.STARTED_OPEN -> GrpcStudyStage.STUDY_STAGE_STARTED_OPEN
        StudyStage.STARTED_CLOSED -> GrpcStudyStage.STUDY_STAGE_STARTED_CLOSED
        StudyStage.STOPPED_REQUEST -> GrpcStudyStage.STUDY_STAGE_STOPPED_REQUEST
        StudyStage.STOPPED_FORCE -> GrpcStudyStage.STUDY_STAGE_STOPPED_FORCE
        StudyStage.COMPLETED -> GrpcStudyStage.STUDY_STAGE_COMPLETED
        else -> throw IllegalArgumentException("unsupported StudyStage")
    }

fun IrbDecisionType.toGrpc(): GrpcIrbDecisionType =
    when (this) {
        IrbDecisionType.EXEMPT -> GrpcIrbDecisionType.IRB_DECISION_TYPE_EXEMPT
        IrbDecisionType.APPROVED -> GrpcIrbDecisionType.IRB_DECISION_TYPE_APPROVED
        else -> throw IllegalArgumentException("unsupported IrbDecisionType")
    }

fun StudyParticipationApprovalType.toGrpc(): GrpcStudyParticipationApprovalType =
    when (this) {
        StudyParticipationApprovalType.AUTO -> GrpcStudyParticipationApprovalType.STUDY_PARTICIPATION_APPROVAL_TYPE_AUTO
        StudyParticipationApprovalType.MANUAL -> GrpcStudyParticipationApprovalType.STUDY_PARTICIPATION_APPROVAL_TYPE_MANUAL
        else -> throw IllegalArgumentException("unsupported StudyParticipationApprovalType")
    }

fun ActivityType.toGrpc(): GrpcActivityType =
    when (this) {
        ActivityType.TAPPING_SPEED -> GrpcActivityType.ACTIVITY_TYPE_TAPPING_SPEED
        ActivityType.REACTION_TIME -> GrpcActivityType.ACTIVITY_TYPE_REACTION_TIME
        ActivityType.GUIDED_BREATHING -> GrpcActivityType.ACTIVITY_TYPE_GUIDED_BREATHING
        ActivityType.RANGE_OF_MOTION -> GrpcActivityType.ACTIVITY_TYPE_RANGE_OF_MOTION
        ActivityType.GAIT_AND_BALANCE -> GrpcActivityType.ACTIVITY_TYPE_GAIT_AND_BALANCE
        ActivityType.STROOP_TEST -> GrpcActivityType.ACTIVITY_TYPE_STROOP_TEST
        ActivityType.SPEECH_RECOGNITION -> GrpcActivityType.ACTIVITY_TYPE_SPEECH_RECOGNITION
        ActivityType.MOBILE_SPIROMETRY -> GrpcActivityType.ACTIVITY_TYPE_MOBILE_SPIROMETRY
        ActivityType.SUSTAINED_PHONATION -> GrpcActivityType.ACTIVITY_TYPE_SUSTAINED_PHONATION
        ActivityType.FIVE_METER_WALK_TEST -> GrpcActivityType.ACTIVITY_TYPE_FIVE_METER_WALK_TEST
        ActivityType.STATE_BALANCE_TEST -> GrpcActivityType.ACTIVITY_TYPE_STATE_BALANCE_TEST
        ActivityType.ROMBERG_TEST -> GrpcActivityType.ACTIVITY_TYPE_ROMBERG_TEST
        ActivityType.SIT_TO_STAND -> GrpcActivityType.ACTIVITY_TYPE_SIT_TO_STAND
        ActivityType.ORTHOSTATIC_BP -> GrpcActivityType.ACTIVITY_TYPE_ORTHOSTATIC_BP
        ActivityType.BIA_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_BIA_MEASUREMENT
        ActivityType.BP_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_BP_MEASUREMENT
        ActivityType.ECG_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_ECG_MEASUREMENT
        ActivityType.PPG_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_PPG_MEASUREMENT
        ActivityType.SPO2_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_SPO2_MEASUREMENT
        ActivityType.BP_AND_BIA_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_BP_AND_BIA_MEASUREMENT
        ActivityType.STABLE_MEASUREMENT -> GrpcActivityType.ACTIVITY_TYPE_STABLE_MEASUREMENT
        ActivityType.SHAPE_PAINTING -> GrpcActivityType.ACTIVITY_TYPE_SHAPE_PAINTING
        ActivityType.CATCH_LADYBUG -> GrpcActivityType.ACTIVITY_TYPE_CATCH_LADYBUG
        ActivityType.MEMORIZE -> GrpcActivityType.ACTIVITY_TYPE_MEMORIZE
        ActivityType.MEMORIZE_WORDS_START -> GrpcActivityType.ACTIVITY_TYPE_MEMORIZE_WORDS_START
        ActivityType.MEMORIZE_WORDS_END -> GrpcActivityType.ACTIVITY_TYPE_MEMORIZE_WORDS_END
        ActivityType.DESCRIBE_IMAGE -> GrpcActivityType.ACTIVITY_TYPE_DESCRIBE_IMAGE
        ActivityType.READ_TEXT_ALOUD -> GrpcActivityType.ACTIVITY_TYPE_READ_TEXT_ALOUD
        ActivityType.ANSWER_VERBALLY -> GrpcActivityType.ACTIVITY_TYPE_ANSWER_VERBALLY
        ActivityType.ANSWER_WRITTEN -> GrpcActivityType.ACTIVITY_TYPE_ANSWER_WRITTEN
        else -> throw IllegalArgumentException("unsupported ActivityType")
    }

fun QuestionTag.toGrpc(): GrpcTag =
    when (this) {
        QuestionTag.SLIDER -> GrpcTag.TAG_SLIDER
        QuestionTag.RADIO -> GrpcTag.TAG_RADIO
        QuestionTag.CHECKBOX -> GrpcTag.TAG_CHECKBOX
        QuestionTag.IMAGE -> GrpcTag.TAG_IMAGE
        QuestionTag.DROPDOWN -> GrpcTag.TAG_DROPDOWN
        QuestionTag.DATETIME -> GrpcTag.TAG_DATETIME
        QuestionTag.TEXT -> GrpcTag.TAG_TEXT
        QuestionTag.RANKING -> GrpcTag.TAG_RANK
        else -> throw IllegalArgumentException("unsupported QuestionTag")
    }

fun HealthDataType.toGrpc(): GrpcHealthDataType =
    when (this) {
        HealthDataType.BLOOD_PRESSURE -> GrpcHealthDataType.HEALTH_DATA_TYPE_BLOOD_PRESSURE
        HealthDataType.HEART_RATE -> GrpcHealthDataType.HEALTH_DATA_TYPE_HEART_RATE
        HealthDataType.SLEEP_SESSION -> GrpcHealthDataType.HEALTH_DATA_TYPE_SLEEP_SESSION
        HealthDataType.SLEEP_STAGE -> GrpcHealthDataType.HEALTH_DATA_TYPE_SLEEP_STAGE
        HealthDataType.STEPS -> GrpcHealthDataType.HEALTH_DATA_TYPE_STEPS
        HealthDataType.WEIGHT -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEIGHT
        HealthDataType.OXYGEN_SATURATION -> GrpcHealthDataType.HEALTH_DATA_TYPE_OXYGEN_SATURATION
        HealthDataType.HEIGHT -> GrpcHealthDataType.HEALTH_DATA_TYPE_HEIGHT
        HealthDataType.RESPIRATORY_RATE -> GrpcHealthDataType.HEALTH_DATA_TYPE_RESPIRATORY_RATE
        HealthDataType.TOTAL_CALORIES_BURNED -> GrpcHealthDataType.HEALTH_DATA_TYPE_TOTAL_CALORIES_BURNED
        HealthDataType.BLOOD_GLUCOSE -> GrpcHealthDataType.HEALTH_DATA_TYPE_BLOOD_GLUCOSE
        HealthDataType.LIGHT -> GrpcHealthDataType.HEALTH_DATA_TYPE_LIGHT
        HealthDataType.ACCELEROMETER -> GrpcHealthDataType.HEALTH_DATA_TYPE_ACCELEROMETER
        HealthDataType.EXERCISE -> GrpcHealthDataType.HEALTH_DATA_TYPE_EXERCISE
        HealthDataType.WEAR_ACCELEROMETER -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_ACCELEROMETER
        HealthDataType.WEAR_BIA -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BIA
        HealthDataType.WEAR_ECG -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_ECG
        HealthDataType.WEAR_PPG_GREEN -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_GREEN
        HealthDataType.WEAR_PPG_IR -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_IR
        HealthDataType.WEAR_PPG_RED -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_RED
        HealthDataType.WEAR_SPO2 -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_SPO2
        HealthDataType.WEAR_SWEAT_LOSS -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS
        HealthDataType.WEAR_HEART_RATE -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_HEART_RATE
        HealthDataType.WEAR_HEALTH_EVENT -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_HEALTH_EVENT
        HealthDataType.WEAR_GYROSCOPE -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_GYROSCOPE
        HealthDataType.OFF_BODY -> GrpcHealthDataType.HEALTH_DATA_TYPE_OFF_BODY
        HealthDataType.BATTERY -> GrpcHealthDataType.HEALTH_DATA_TYPE_BATTERY
        HealthDataType.WEAR_BATTERY -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BATTERY
        HealthDataType.GYROSCOPE -> GrpcHealthDataType.HEALTH_DATA_TYPE_GYROSCOPE
        HealthDataType.WEAR_BLOOD_PRESSURE -> GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BLOOD_PRESSURE
        HealthDataType.DEVICE_STAT_MOBILE_WEAR_CONNECTION -> GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_MOBILE_WEAR_CONNECTION
        HealthDataType.DEVICE_STAT_WEAR_BATTERY -> GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_BATTERY
        HealthDataType.DEVICE_STAT_WEAR_OFF_BODY -> GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_OFF_BODY
        HealthDataType.DEVICE_STAT_WEAR_POWER_ON_OFF -> GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_POWER_ON_OFF
        else -> throw IllegalArgumentException("unsupported HealthDataType")
    }

fun GrpcHealthDataType.toDomain(): HealthDataType =
    when (this) {
        GrpcHealthDataType.HEALTH_DATA_TYPE_BLOOD_PRESSURE -> HealthDataType.BLOOD_PRESSURE
        GrpcHealthDataType.HEALTH_DATA_TYPE_HEART_RATE -> HealthDataType.HEART_RATE
        GrpcHealthDataType.HEALTH_DATA_TYPE_SLEEP_SESSION -> HealthDataType.SLEEP_SESSION
        GrpcHealthDataType.HEALTH_DATA_TYPE_SLEEP_STAGE -> HealthDataType.SLEEP_STAGE
        GrpcHealthDataType.HEALTH_DATA_TYPE_STEPS -> HealthDataType.STEPS
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEIGHT -> HealthDataType.WEIGHT
        GrpcHealthDataType.HEALTH_DATA_TYPE_OXYGEN_SATURATION -> HealthDataType.OXYGEN_SATURATION
        GrpcHealthDataType.HEALTH_DATA_TYPE_HEIGHT -> HealthDataType.HEIGHT
        GrpcHealthDataType.HEALTH_DATA_TYPE_RESPIRATORY_RATE -> HealthDataType.RESPIRATORY_RATE
        GrpcHealthDataType.HEALTH_DATA_TYPE_TOTAL_CALORIES_BURNED -> HealthDataType.TOTAL_CALORIES_BURNED
        GrpcHealthDataType.HEALTH_DATA_TYPE_BLOOD_GLUCOSE -> HealthDataType.BLOOD_GLUCOSE
        GrpcHealthDataType.HEALTH_DATA_TYPE_LIGHT -> HealthDataType.LIGHT
        GrpcHealthDataType.HEALTH_DATA_TYPE_ACCELEROMETER -> HealthDataType.ACCELEROMETER
        GrpcHealthDataType.HEALTH_DATA_TYPE_EXERCISE -> HealthDataType.EXERCISE
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_ACCELEROMETER -> HealthDataType.WEAR_ACCELEROMETER
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BIA -> HealthDataType.WEAR_BIA
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_ECG -> HealthDataType.WEAR_ECG
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_GREEN -> HealthDataType.WEAR_PPG_GREEN
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_IR -> HealthDataType.WEAR_PPG_IR
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_RED -> HealthDataType.WEAR_PPG_RED
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_SPO2 -> HealthDataType.WEAR_SPO2
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS -> HealthDataType.WEAR_SWEAT_LOSS
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_HEART_RATE -> HealthDataType.WEAR_HEART_RATE
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_HEALTH_EVENT -> HealthDataType.WEAR_HEALTH_EVENT
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_GYROSCOPE -> HealthDataType.WEAR_GYROSCOPE
        GrpcHealthDataType.HEALTH_DATA_TYPE_OFF_BODY -> HealthDataType.OFF_BODY
        GrpcHealthDataType.HEALTH_DATA_TYPE_BATTERY -> HealthDataType.BATTERY
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BATTERY -> HealthDataType.WEAR_BATTERY
        GrpcHealthDataType.HEALTH_DATA_TYPE_GYROSCOPE -> HealthDataType.GYROSCOPE
        GrpcHealthDataType.HEALTH_DATA_TYPE_WEAR_BLOOD_PRESSURE -> HealthDataType.WEAR_BLOOD_PRESSURE
        GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_MOBILE_WEAR_CONNECTION -> HealthDataType.DEVICE_STAT_MOBILE_WEAR_CONNECTION
        GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_BATTERY -> HealthDataType.DEVICE_STAT_WEAR_BATTERY
        GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_OFF_BODY -> HealthDataType.DEVICE_STAT_WEAR_OFF_BODY
        GrpcHealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_POWER_ON_OFF -> HealthDataType.DEVICE_STAT_WEAR_POWER_ON_OFF
        else -> throw IllegalArgumentException("unsupported HealthDataType")
    }

fun SubjectStatus.toGrpc(): GrpcSubjectStatus =
    when (this) {
        SubjectStatus.COMPLETED -> GrpcSubjectStatus.SUBJECT_STATUS_COMPLETED
        SubjectStatus.DROP -> GrpcSubjectStatus.SUBJECT_STATUS_DROP
        SubjectStatus.PARTICIPATING -> GrpcSubjectStatus.SUBJECT_STATUS_PARTICIPATING
        SubjectStatus.WITHDRAWN -> GrpcSubjectStatus.SUBJECT_STATUS_WITHDRAWN
        else -> throw IllegalArgumentException("unsupported UserStatus")
    }
