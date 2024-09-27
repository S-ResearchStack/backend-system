package researchstack.backend.adapter.incoming.mapper.version

import researchstack.backend.application.port.incoming.version.VersionResponse
import researchstack.backend.domain.version.Version

fun Version.toResponse(): VersionResponse =
    VersionResponse(
        minimum = minimum,
        latest = latest
    )
