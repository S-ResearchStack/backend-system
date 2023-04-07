package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.config.EmailVerificationProperties
import com.samsung.healthcare.account.application.config.InvitationProperties
import com.samsung.healthcare.account.application.config.PasswordResetProperties
import com.samsung.healthcare.account.domain.Email
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.net.URLEncoder

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val invitationProperties: InvitationProperties,
    private val emailVerificationProperties: EmailVerificationProperties,
    private val passwordResetProperties: PasswordResetProperties,
) {

    companion object {
        // TODO use html template?
        private const val MESSAGE_TEMPLATE = """
            %s <a href="%s">%s</a>
        """
    }

    internal fun sendInvitationMail(email: Email, resetToken: String): Mono<Void> {
        sendMail(
            email,
            "Account activation request",
            htmlMessage(
                invitationProperties.url,
                email,
                resetToken,
                "Please activate your account from"
            )
        )
        return Mono.empty()
    }

    internal fun sendPasswordResetMail(email: Email, resetToken: String): Mono<Void> {
        sendMail(
            email,
            "Password reset request",
            htmlMessage(
                passwordResetProperties.url,
                email,
                resetToken,
                "A request has been made to reset your password on the Samsung Health Stack portal. " +
                    "Click reset password and follow the prompts."
            )
        )
        return Mono.empty()
    }

    internal fun sendVerificationMail(email: Email, token: String): Mono<Void> {
        sendMail(
            email,
            "Please verify your email address",
            htmlMessage(
                emailVerificationProperties.url,
                email,
                token,
                "Please activate your account from"
            )
        )
        return Mono.empty()
    }

    internal fun sendMail(email: Email, subject: String, htmlMessage: String) {
        Mono.fromCallable {
            mailSender.send(
                mailSender.createMimeMessage().apply {
                    MimeMessageHelper(this, true, "UTF-8")
                        .apply {
                            setTo(email.value)
                            // TODO
                            setSubject(subject)
                            setText(htmlMessage, true)
                        }
                }
            )
            // TODO config repeat
        }.retry(3)
            .subscribeOn(Schedulers.boundedElastic())
            .doOnError { it.printStackTrace() }
            .subscribe()
    }

    private fun htmlMessage(url: String, email: Email, resetToken: String, message: String): String =
        URLEncoder.encode(email.value, "utf-8").let { encodedEmail ->
            "$url?reset-token=$resetToken&email=$encodedEmail".let { path ->
                MESSAGE_TEMPLATE.format(message, path, path)
            }
        }
}
