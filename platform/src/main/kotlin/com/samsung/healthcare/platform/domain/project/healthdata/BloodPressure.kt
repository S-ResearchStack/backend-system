package com.samsung.healthcare.platform.domain.project.healthdata

import com.fasterxml.jackson.annotation.JsonValue
import java.time.Instant

/**
 * Represents recorded blood pressure data.
 *
 * @property systolic Recorded systolic blood pressure.
 * @property diastolic Recorded diastolic blood pressure.
 * @property bodyPosition Body position of user at time of recording. Defaults to null.
 * @property measurementLocation Loation where blood pressure measurement was taken. Defaults to null.
 * @see SampleData
 */
data class BloodPressure(
    override val id: HealthDataId?,
    override val time: Instant,
    val systolic: Double,
    val diastolic: Double,
    val bodyPosition: BodyPosition? = null,
    val measurementLocation: MeasurementLocation? = null,
) : SampleData(id, time, HealthDataType.BLOOD_PRESSURE) {
    companion object {
        fun newBloodPressure(
            time: Instant,
            systolic: Double,
            diastolic: Double,
            bodyPosition: BodyPosition? = null,
            measurementLocation: MeasurementLocation? = null,
        ): BloodPressure = BloodPressure(null, time, systolic, diastolic, bodyPosition, measurementLocation)
    }

    enum class BodyPosition(@JsonValue val value: String) {
        STANDING_UP("standing_up"),
        SITTING_DOWN("sitting_down"),
        LYING_DOWN("lying_down"),
        RECLINING("reclining"),
    }

    enum class MeasurementLocation(@JsonValue val value: String) {
        LEFT_WRIST("left_wrist"),
        RIGHT_WRIST("right_wrist"),
        LEFT_UPPER_ARM("left_upper_arm"),
        RIGHT_UPPER_ARM("right_upper_arm"),
    }
}
