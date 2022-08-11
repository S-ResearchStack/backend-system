package com.samsung.healthcare.platform.logger.decorator

import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingResponseDecorator internal constructor(
    delegate: ServerHttpResponse,
    private val requestId: String
) : ServerHttpResponseDecorator(delegate) {
    private val logger = KotlinLogging.logger {}

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(
            Flux.from(body)
                .doOnNext { buffer: DataBuffer ->
                    if (logger.isInfoEnabled) {
                        val bodyStream = ByteArrayOutputStream()
                        Channels.newChannel(bodyStream)
                            .write(buffer.asByteBuffer().asReadOnlyBuffer())
                        logger.info("$requestId | response: ${String(bodyStream.toByteArray())}")
                    }
                }
        )
    }
}
