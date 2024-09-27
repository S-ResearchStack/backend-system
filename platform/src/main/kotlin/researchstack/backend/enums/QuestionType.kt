package researchstack.backend.enums

enum class QuestionType(val value: String) {
    UNSPECIFIED("Unspecified"),
    CHOICE("Choice"),
    SCALE("Scale"),
    TEXT("Text"),
    RANKING("Ranking"),
    DATETIME("DateTime")
}
