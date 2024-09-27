package researchstack.backend.application.service.common

import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMultipart
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import researchstack.backend.application.port.incoming.common.SendInvitationEmailCommand
import researchstack.backend.application.port.incoming.common.SendInvitationEmailUseCase
import researchstack.backend.config.InvitationProperties

@Component
class EmailService(
    private val mailSender: JavaMailSender,
    private val invitationProperties: InvitationProperties
) : SendInvitationEmailUseCase {

    companion object {
        private const val INVITATION_MESSAGE_TEMPLATE = """
            "<html><body><h1>" + %s + "</h1><p>" + %s + "</p></body></html>";
        """

        private const val INVITATION_MESSAGE_TITLE = "Study Invitation"
    }

    private val invitationMessageBody: String = """
            You're invited to join the %s with the role of %s.
            Click the button to process to the study.

            <a href=${invitationProperties.url}">Join Study</a>
        """

    override suspend fun sendInvitationEmail(command: SendInvitationEmailCommand) {
        sendEmail(
            command.email,
            "${command.sender} has invited you to join a study",
            INVITATION_MESSAGE_TEMPLATE.format(
                INVITATION_MESSAGE_TITLE,
                invitationMessageBody.format(command.studyName, command.role)
            )
        )
    }

    private suspend fun sendEmail(email: String, subject: String, htmlMessage: String) {
        val multipart = MimeMultipart()
        val bodyPart = MimeBodyPart()

        bodyPart.setContent(htmlMessage, "text/html; charset=utf-8")
        multipart.addBodyPart(bodyPart)

        Mono.fromCallable {
            mailSender.send(
                mailSender.createMimeMessage().apply {
                    MimeMessageHelper(this, true, "UTF-8")
                        .apply {
                            setTo(email)
                            setSubject(subject)
                            setContent(multipart)
                        }
                }
            )
            // TODO config repeat
        }.retry(3)
            .subscribeOn(Schedulers.boundedElastic())
            .doOnError { it.printStackTrace() }
            .subscribe()
    }
}
