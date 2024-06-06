package rs.raf.edu.rma.leaderboard.store

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import rs.raf.edu.rma.leaderboard.domain.Leaderboard
import rs.raf.edu.rma.leaderboard.domain.QuizCategory
import rs.raf.edu.rma.leaderboard.domain.QuizResult

class LeaderboardDataStore(
    private val persistence: DataStore<Leaderboard>,
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val writeMutex = Mutex()

    val data = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first() }
        )

    suspend fun addQuizResult(quizResult: QuizResult): Leaderboard {
        return writeMutex.withLock {
            persistence.updateData {
                when (quizResult.category) {
                    QuizCategory.GuessTheFact -> it.copy(
                        guessTheFactResults = it.guessTheFactResults.appendResult(quizResult)
                    )

                    QuizCategory.GuessTheCat -> it.copy(
                        guessTheCatResults = it.guessTheCatResults.appendResult(quizResult)
                    )

                    QuizCategory.LeftOrRight -> it.copy(
                        leftOrRightResults = it.leftOrRightResults.appendResult(quizResult)
                    )
                }
            }
        }
    }

    private fun List<QuizResult>.appendResult(result: QuizResult): List<QuizResult> {
        return toMutableList().apply { add(result) }
    }

    suspend fun clearResults(category: QuizCategory) {
        writeMutex.withLock {
            persistence.updateData {
                when (category) {
                    QuizCategory.GuessTheFact -> it.copy(guessTheFactResults = emptyList())
                    QuizCategory.GuessTheCat -> it.copy(guessTheCatResults = emptyList())
                    QuizCategory.LeftOrRight -> it.copy(leftOrRightResults = emptyList())
                }
            }
        }
    }
}
