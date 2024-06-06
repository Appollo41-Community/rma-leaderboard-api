package rs.raf.edu.rma.leaderboard.store.serialization

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import rs.raf.edu.rma.leaderboard.domain.Leaderboard
import rs.raf.edu.rma.plugins.serialization.AppJson
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class LeaderboardSerializer : Serializer<Leaderboard> {

    private val json = AppJson

    override val defaultValue: Leaderboard = Leaderboard()

    override suspend fun readFrom(input: InputStream): Leaderboard {
        val text = String(input.readBytes(), StandardCharsets.UTF_8)
        return withContext(Dispatchers.IO) {
            try {
                json.decodeFromString(text)
            } catch (error: SerializationException) {
                throw CorruptionException("Unable to deserialize decrypted value.", error)
            } catch (error: IllegalArgumentException) {
                throw CorruptionException("Unable to deserialize decrypted value.", error)
            }
        }
    }

    override suspend fun writeTo(t: Leaderboard, output: OutputStream) {
        val text = json.encodeToString(t)
        withContext(Dispatchers.IO) {
            output.write(text.toByteArray(charset = StandardCharsets.UTF_8))
        }
    }
}