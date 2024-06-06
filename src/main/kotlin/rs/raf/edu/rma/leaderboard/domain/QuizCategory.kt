package rs.raf.edu.rma.leaderboard.domain

import kotlinx.serialization.Serializable
import rs.raf.edu.rma.leaderboard.domain.serializer.QuizCategorySerializer

@Serializable(with = QuizCategorySerializer::class)
enum class QuizCategory(val categoryId: Int) {
    GuessTheFact(categoryId = 1),
    GuessTheCat(categoryId = 2),
    LeftOrRight(categoryId = 3),
    ;

    companion object {
        private val map = values().associateBy { it.categoryId }
        fun fromInt(categoryId: Int) = map[categoryId]
    }
}
