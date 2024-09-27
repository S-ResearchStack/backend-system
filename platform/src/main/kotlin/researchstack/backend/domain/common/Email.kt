package researchstack.backend.domain.common

import com.google.gson.annotations.JsonAdapter
import researchstack.backend.adapter.incoming.rest.common.EmailSerializer

val EMAIL_REGEX: Regex =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$".toRegex()

@JsonAdapter(EmailSerializer::class)
data class Email(val value: String) {
    init {
        require(EMAIL_REGEX matches value)
    }

    override fun toString(): String = value
}
