package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.auth.Authorizer
import org.springframework.stereotype.Component
import researchstack.backend.application.exception.UnauthorizedException
import java.util.concurrent.CompletionStage

@Component
class IntegratedJwtAuthorizer(
    private val cognitoJwtAuthorizer: CognitoJwtAuthorizer,
    private val googleJwtAuthorizer: GoogleJwtAuthorizer,
    private val samsungAccountJwtAuthorizer: SamsungAccountJwtAuthorizer,
    private val superTokensJwtAuthorizer: SuperTokensJwtAuthorizer
) : Authorizer<HttpRequest> {
    override fun authorize(ctx: ServiceRequestContext, data: HttpRequest): CompletionStage<Boolean> {
        val jwkType = data.headers().find {
            it.key.toString().equals("jwt-issuer", true)
        }?.value ?: throw UnauthorizedException("please set jwt-issuer header")

        return when (jwkType) {
            "cognito" -> cognitoJwtAuthorizer
            "google" -> googleJwtAuthorizer
            "samsung-account" -> samsungAccountJwtAuthorizer
            "super-tokens" -> superTokensJwtAuthorizer
            else -> {
                throw UnauthorizedException("unsupported jwt-issuer type")
            }
        }.authorize(ctx, data)
    }
}
