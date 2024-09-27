package researchstack.backend.domain.common

data class Url(val url: String) {
    init {
        require(url.isNotBlank())
    }
}
