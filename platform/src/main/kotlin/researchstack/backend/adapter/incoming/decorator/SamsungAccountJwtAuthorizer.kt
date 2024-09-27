package researchstack.backend.adapter.incoming.decorator

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.stereotype.Component

@Component
class SamsungAccountJwtAuthorizer(
    @Qualifier("samsung-account")
    private val reactiveJwtDecoder: ReactiveJwtDecoder
) : JwtAuthorizer(reactiveJwtDecoder)
