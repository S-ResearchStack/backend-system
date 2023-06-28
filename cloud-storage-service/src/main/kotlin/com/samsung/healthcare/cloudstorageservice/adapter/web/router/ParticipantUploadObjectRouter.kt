package com.samsung.healthcare.cloudstorageservice.adapter.web.router

import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.handler.UploadObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ParticipantUploadObjectRouter {
    @Bean
    fun routeParticipantUploadObject(
        handler: UploadObjectHandler,
        idTokenFilterFunction: IdTokenFilterFunction,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET(
                PARTICIPANT_UPLOAD_OBJECT_URL_PATH,
                handler::participantUpload,
            )
            .filter(idTokenFilterFunction)
            .build()
}
