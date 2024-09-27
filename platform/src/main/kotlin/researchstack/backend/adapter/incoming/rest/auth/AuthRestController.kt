package researchstack.backend.adapter.incoming.rest.auth

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.auth.SignInCommand
import researchstack.backend.application.port.incoming.auth.SignInUseCase
import researchstack.backend.application.port.incoming.auth.SignUpCommand
import researchstack.backend.application.port.incoming.auth.SignUpUseCase

@Component
class AuthRestController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) {
    @Post("/auth/signin")
    suspend fun signIn(
        @RequestObject command: SignInCommand
    ): HttpResponse {
        return HttpResponse.of(
            JsonHandler.toJson(
                signInUseCase.signIn(command)
            )
        )
    }

    @Post("/auth/signup")
    suspend fun signUp(
        @RequestObject command: SignUpCommand
    ): HttpResponse {
        return HttpResponse.of(
            JsonHandler.toJson(
                signUpUseCase.signUp(command)
            )
        )
    }
}
