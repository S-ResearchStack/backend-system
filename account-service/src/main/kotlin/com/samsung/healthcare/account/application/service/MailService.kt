package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.config.EmailVerificationProperties
import com.samsung.healthcare.account.application.config.InvitationProperties
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
) {

    companion object {
        // TODO use html template?
        private const val MESSAGE_TEMPLATE = """
            Please activate your account from <a href="%s">%s</a>
        """
    }

    internal fun sendResetPasswordMail(email: Email, resetToken: String): Mono<Void> {
        sendMail(email, "Account activation request", resetPasswordHtmlMessage(email, resetToken))
        return Mono.empty()
    }

    internal fun sendVerificationMail(email: Email, token: String): Mono<Void> {
        sendMail(email, "Please verify your email address", verificationHtmlMessage(email, token))
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

    private fun resetPasswordHtmlMessage(email: Email, resetToken: String): String =
        URLEncoder.encode(email.value, "utf-8").let { encodedEmail ->
            "${invitationProperties.url}?reset-token=$resetToken&email=$encodedEmail".let { path ->
                MESSAGE_TEMPLATE.format(path, path)
            }
        }

    private fun verificationHtmlMessage(email: Email, token: String): String =
        URLEncoder.encode(email.value, "utf-8").let { encodedEmail ->
            "${emailVerificationProperties.url}?token=$token&email=$encodedEmail".let { path ->
                MESSAGE_TEMPLATE.format(path, path)
            }
        }
}
