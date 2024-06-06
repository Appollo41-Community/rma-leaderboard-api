package rs.raf.edu.rma.leaderboard.di

import androidx.datastore.core.DataStoreFactory
import org.koin.dsl.module
import rs.raf.edu.rma.leaderboard.repository.LeaderboardRepository
import rs.raf.edu.rma.leaderboard.store.LeaderboardDataStore
import rs.raf.edu.rma.leaderboard.store.serialization.LeaderboardSerializer
import java.io.File

val leaderboard = module {
    single {
        DataStoreFactory.create(
            produceFile = { File("leaderboard.json") },
            serializer = LeaderboardSerializer(),
        )
    }

    single {
        LeaderboardDataStore(
            persistence = get(),
        )
    }

    single {
        LeaderboardRepository(
            leaderboardDataStore = get(),
        )
    }
}
