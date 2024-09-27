package researchstack.backend.application.port.outgoing.storage

import java.net.URL

data class UploadPresignedUrlResponse(
    val url: URL,
    val headers: Map<String, String>
)
