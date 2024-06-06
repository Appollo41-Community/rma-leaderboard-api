package rs.raf.edu.rma.leaderboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class Leaderboard(
    val guessTheFactResults: List<QuizResult> = emptyList(),
    val guessTheCatResults: List<QuizResult> = emptyList(),
    val leftOrRightResults: List<QuizResult> = emptyList(),
)
