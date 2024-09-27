package researchstack.backend.enums

enum class SubjectStatus(val value: String) {
    IDLE("Idle"),
    PARTICIPATING("Participating"),
    WITHDRAWN("Withdrawn"),
    DROP("Drop"),
    COMPLETED("Completed"),
    HIDDEN("Hidden")
}
