package rs.raf.edu.rma.leaderboard.routing.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateQuizResultRequestBody(
    val category: Int,
    val nickname: String,
    val result: Float,
)
