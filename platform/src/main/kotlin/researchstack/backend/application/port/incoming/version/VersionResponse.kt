package researchstack.backend.application.port.incoming.version

data class VersionResponse(
    val minimum: String,
    val latest: String
)
