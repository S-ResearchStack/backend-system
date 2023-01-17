package com.samsung.healthcare.dataqueryservice.adapter.web.exception

import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.ErrorType.BAD_REQUEST
import com.netflix.graphql.types.errors.ErrorType.NOT_FOUND
import com.netflix.graphql.types.errors.ErrorType.UNKNOWN
import com.netflix.graphql.types.errors.TypedGraphQLError
import com.samsung.healthcare.dataqueryservice.application.exception.TableNotFoundException
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

@Component
class DataFetchingExceptionHandler : DataFetcherExceptionHandler {

    private val errorStatusMap: Map<KClass<out RuntimeException>, ErrorType> = mapOf(
        IllegalArgumentException::class to BAD_REQUEST,
        TableNotFoundException::class to NOT_FOUND
    )

    override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters):
        CompletableFuture<DataFetcherExceptionHandlerResult> {

        return CompletableFuture.completedFuture(
            DataFetcherExceptionHandlerResult.newResult()
                .error(
                    typedGraphQLError(handlerParameters)
                )
                .build()
        )
    }

    private fun typedGraphQLError(handlerParameters: DataFetcherExceptionHandlerParameters): TypedGraphQLError {
        val errorType = errorStatusMap[handlerParameters.exception::class] ?: UNKNOWN
        if (errorType == UNKNOWN)
            handlerParameters.exception.printStackTrace()

        return TypedGraphQLError.newBuilder()
            .errorType(errorType)
            .path(handlerParameters.path)
            .build()
    }
}
