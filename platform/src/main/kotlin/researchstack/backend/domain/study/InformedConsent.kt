package researchstack.backend.domain.study

data class InformedConsent(
    val imagePath: String
) {
    companion object {
        fun new(
            imagePath: String
        ): InformedConsent = InformedConsent(
            imagePath = imagePath
        )
    }
}
