package researchstack.backend.application.port.incoming.common

interface SendInvitationEmailUseCase {
    suspend fun sendInvitationEmail(command: SendInvitationEmailCommand)
}
