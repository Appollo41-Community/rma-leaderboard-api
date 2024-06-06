package rs.raf.edu.rma.leaderboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val category: QuizCategory,
    val nickname: String,
    val result: Float,
    val createdAt: Long = System.currentTimeMillis(),
)
