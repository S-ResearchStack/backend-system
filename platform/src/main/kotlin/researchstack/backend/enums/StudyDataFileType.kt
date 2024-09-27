package researchstack.backend.enums

enum class StudyDataFileType(val value: String) {
    UNSPECIFIED("Unspecified"),
    META_INFO("MetaInfo"),
    MESSAGE_LOG("MessageLog"),
    RAW_DATA("RawData"),
    ATTACHMENT("Attachment")
}
