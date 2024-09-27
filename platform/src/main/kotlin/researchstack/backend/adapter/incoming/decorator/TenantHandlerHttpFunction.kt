package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.DecoratingHttpServiceFunction
import com.linecorp.armeria.server.HttpService
import com.linecorp.armeria.server.ServiceRequestContext
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.config.STUDY_ID_KEY

class TenantHandlerHttpFunction : DecoratingHttpServiceFunction {
    override fun serve(delegate: HttpService, ctx: ServiceRequestContext, req: HttpRequest): HttpResponse {
        val studyId = ctx.pathParams()["studyId"] ?: throw IllegalArgumentException(ExceptionMessage.EMPTY_STUDY_ID)
        ServiceRequestContext.current().setAttr(STUDY_ID_KEY, studyId)

        return delegate.serve(ctx, req)
    }
}
