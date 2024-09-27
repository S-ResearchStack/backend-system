package researchstack.backend.application.port.incoming.file

import java.net.URL

data class GetPresignedUrlResponse(
    val presignedUrl: URL,
    val headers: Map<String, String>
)
