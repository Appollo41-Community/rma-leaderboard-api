package rs.raf.edu.rma.leaderboard.routing.model

import kotlinx.serialization.Serializable
import rs.raf.edu.rma.leaderboard.domain.QuizResult

@Serializable
data class CreateQuizResultResponse(
    val result: QuizResult,
    val ranking: Int,
)
