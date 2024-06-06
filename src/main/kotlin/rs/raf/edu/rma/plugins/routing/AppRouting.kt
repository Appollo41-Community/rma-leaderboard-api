package rs.raf.edu.rma.plugins.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rs.raf.edu.rma.core.readReleaseVersion
import rs.raf.edu.rma.leaderboard.routing.leaderboardRouting

fun Application.configureRouting() {

    val appVersion = readReleaseVersion()

    routing {
        get("/") {
            call.respondText(
                """
                    RMA Quiz Catapult Leaderboard API $appVersion.
                """.trimIndent()
            )
        }

        leaderboardRouting(path = "leaderboard")
    }
}
