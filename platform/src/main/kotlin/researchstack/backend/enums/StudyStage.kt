package researchstack.backend.enums

enum class StudyStage(val value: String) {
    UNSPECIFIED("Unspecified"),
    CREATED("Created"),
    STARTED_OPEN("StartedOpen"),
    STARTED_CLOSED("StartedClosed"),
    STOPPED_REQUEST("StoppedRequest"),
    STOPPED_FORCE("StoppedForce"),
    COMPLETED("Completed");
}
