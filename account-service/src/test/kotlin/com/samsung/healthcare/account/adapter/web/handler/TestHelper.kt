package com.samsung.healthcare.account.adapter.web.handler

import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec
import org.springframework.web.reactive.function.BodyInserters

internal fun WebTestClient.get(path: String) =
    this.get().uri(path).exchange()

internal fun WebTestClient.post(path: String, param: Any) =
    exchangeWithParam(
        this.post().uri(path),
        param
    )

internal fun WebTestClient.put(path: String, param: Any) =
    exchangeWithParam(
        this.put().uri(path),
        param
    )

private fun exchangeWithParam(uriSpec: RequestBodySpec, param: Any) =
    uriSpec.contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(param))
        .exchange()
