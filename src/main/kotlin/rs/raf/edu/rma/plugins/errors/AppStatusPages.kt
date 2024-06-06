package rs.raf.edu.rma.plugins.errors

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import rs.raf.edu.rma.plugins.errors.model.AppExceptionResponse

fun Application.configureErrorStatusPages() {

    install(StatusPages) {
        exception<AppException> { call, appException ->
            call.respond(
                status = appException.httpStatusCode,
                message = AppExceptionResponse(
                    error = appException.error,
                    httpCode = appException.httpStatusCode.value,
                    message = appException.message,
                    description = appException.description,
                    suggestion = appException.suggestion,
                    cause = appException.cause?.stackTraceToString(),
                )
            )
        }

        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = AppExceptionResponse(
                    error = HttpStatusCode.BadRequest.description,
                    httpCode = HttpStatusCode.BadRequest.value,
                    message = cause.reasons.joinToString()
                )
            )
        }

        exception<SerializationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.UnprocessableEntity,
                message = AppExceptionResponse(
                    error = HttpStatusCode.UnprocessableEntity.description,
                    httpCode = HttpStatusCode.UnprocessableEntity.value,
                    cause = cause.stackTraceToString()
                )
            )
        }

        exception<Exception> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = AppExceptionResponse(
                    error = "unknown",
                    httpCode = HttpStatusCode.InternalServerError.value,
                    cause = cause.stackTraceToString(),
                )
            )
        }
    }

}
