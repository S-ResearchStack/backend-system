package researchstack.backend.enums

enum class QuestionTag(val value: String) {
    UNSPECIFIED("Unspecified"),
    SLIDER("Slider"),
    RADIO("Radio"),
    CHECKBOX("Checkbox"),
    IMAGE("Image"),
    DROPDOWN("Dropdown"),
    DATETIME("Datetime"),
    TEXT("Text"),
    RANKING("Rank");
}
