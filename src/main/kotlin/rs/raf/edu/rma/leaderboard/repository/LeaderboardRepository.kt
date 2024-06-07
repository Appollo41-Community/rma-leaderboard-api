package rs.raf.edu.rma.leaderboard.repository

import rs.raf.edu.rma.leaderboard.domain.Leaderboard
import rs.raf.edu.rma.leaderboard.domain.QuizCategory
import rs.raf.edu.rma.leaderboard.domain.QuizResult
import rs.raf.edu.rma.leaderboard.store.LeaderboardDataStore

class LeaderboardRepository(
    val leaderboardDataStore: LeaderboardDataStore,
) {

    fun getLeaderboard() = leaderboardDataStore.data.value

    private fun Leaderboard.getSortedByDescendingResults(category: QuizCategory): List<QuizResult> {
        val result = when (category) {
            QuizCategory.GuessTheFact -> this.guessTheFactResults
            QuizCategory.GuessTheCat -> this.guessTheCatResults
            QuizCategory.LeftOrRight -> this.leftOrRightResults
        }
        return result.sortedByDescending { it.result }
    }

    fun getSortedResults(category: QuizCategory): List<QuizResult> {
        val leaderboard = leaderboardDataStore.data.value
        return leaderboard.getSortedByDescendingResults(category)
    }

    suspend fun insertQuizResult(quizResult: QuizResult): Int {
        val latestLeaderboard = leaderboardDataStore.addQuizResult(quizResult)
        val sortedResults = latestLeaderboard.getSortedByDescendingResults(quizResult.category)
        return sortedResults.indexOf(quizResult) + 1
    }

    suspend fun deleteAllQuizResults(category: QuizCategory) {
        leaderboardDataStore.clearResults(category)
    }
}
