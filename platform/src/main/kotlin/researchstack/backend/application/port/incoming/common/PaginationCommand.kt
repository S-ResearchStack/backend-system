package researchstack.backend.application.port.incoming.common

import com.linecorp.armeria.server.annotation.Param

data class PaginationCommand(
    @Param val page: Long?,
    @Param val size: Long?
)
