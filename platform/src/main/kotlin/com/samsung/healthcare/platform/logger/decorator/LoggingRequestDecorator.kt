package com.samsung.healthcare.platform.logger.decorator

import com.samsung.healthcare.platform.logger.asString
import mu.KotlinLogging
import org.apache.logging.log4j.util.Strings
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingRequestDecorator internal constructor(
    delegate: ServerHttpRequest,
    private val requestId: String
) : ServerHttpRequestDecorator(delegate) {
    private val logger = KotlinLogging.logger {}
    private val body: Flux<DataBuffer>

    override fun getBody(): Flux<DataBuffer> = body

    init {
        if (logger.isInfoEnabled) {
            val path = delegate.uri.path
            val query = delegate.uri.query ?: Strings.EMPTY
            val method = (delegate.method ?: HttpMethod.GET).name
            val host = delegate.headers.host?.asString()
            logger.info("$requestId | $method $path/$query | host: $host")

            body = super.getBody().doOnNext { buffer: DataBuffer ->
                val bodyStream = ByteArrayOutputStream()
                Channels.newChannel(bodyStream).write(buffer.asByteBuffer().asReadOnlyBuffer())
                val requestBody = String(bodyStream.toByteArray()).replace("\\s+".toRegex(), "")
                logger.info("$requestId | request: $requestBody")
            }
        } else {
            body = super.getBody()
        }
    }
}
