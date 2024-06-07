package rs.raf.edu.rma.leaderboard.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import rs.raf.edu.rma.core.quizCategoryParamOrThrow
import rs.raf.edu.rma.core.readDeleteSecretToken
import rs.raf.edu.rma.leaderboard.domain.QuizCategory
import rs.raf.edu.rma.leaderboard.domain.QuizResult
import rs.raf.edu.rma.leaderboard.repository.LeaderboardRepository
import rs.raf.edu.rma.leaderboard.routing.model.CreateQuizResultRequestBody
import rs.raf.edu.rma.leaderboard.routing.model.CreateQuizResultResponse
import rs.raf.edu.rma.leaderboard.utils.isNicknameValid
import rs.raf.edu.rma.plugins.errors.AppException

private const val Category = "category"

fun Route.leaderboardRouting(path: String) = route(path) {

    val leaderboardRepository by inject<LeaderboardRepository>()

    install(RequestValidation) {
        validateCreateQuizResultRequest()
    }

    get {
        val category = call.quizCategoryParamOrThrow("category")
        val results = leaderboardRepository.getSortedResults(category)
        call.respond(results)
    }

    post<CreateQuizResultRequestBody> {
        val category = QuizCategory.fromInt(it.category)
        checkNotNull(category)

        val quizResult = QuizResult(
            category = category,
            nickname = it.nickname,
            result = it.result,
        )
        val ranking = leaderboardRepository.insertQuizResult(quizResult)

        call.respond(
            CreateQuizResultResponse(
                result = quizResult,
                ranking = ranking,
            )
        )
    }

    delete(path = "{$Category}") {
        val category = call.quizCategoryParamOrThrow(Category)
        val deleteToken = call.request.header("Authorization")
        if (deleteToken != readDeleteSecretToken()) {
            throw AppException.UnauthorizedException("Invalid Authorization!")
        }
        leaderboardRepository.deleteAllQuizResults(category)
        call.respond(HttpStatusCode.NoContent)
    }

    get("admin") {
        val leaderboard = leaderboardRepository.getLeaderboard()
        call.respond(leaderboard)
    }
}

private fun RequestValidationConfig.validateCreateQuizResultRequest() {
    validate<CreateQuizResultRequestBody> {
        when {
            it.result < 0 || it.result > 100 -> ValidationResult.Invalid(
                reason = "result must be within 0.00..100.00 range."
            )

            QuizCategory.fromInt(it.category) == null -> ValidationResult.Invalid(
                reason = "category must be one of ${QuizCategory.values().map { it.categoryId }} values."
            )

            !isNicknameValid(it.nickname) -> ValidationResult.Invalid(
                reason = "nickname must not be null or empty. Only letters, digits or '_' are accepted."
            )

            else -> ValidationResult.Valid
        }
    }
}
