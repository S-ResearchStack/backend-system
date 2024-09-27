package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.server.HttpService
import com.linecorp.armeria.server.cors.CorsService
import org.springframework.stereotype.Component

@Component
class CorsHandler {
    fun build(): java.util.function.Function<in HttpService, CorsService> =
        CorsService.builderForAnyOrigin()
            .allowCredentials()
            .allowAllRequestHeaders(true)
            .allowRequestMethods(HttpMethod.POST, HttpMethod.GET, HttpMethod.PATCH, HttpMethod.DELETE)
            .exposeHeaders("*")
            .disablePreflightResponseHeaders()
            .newDecorator()
}
