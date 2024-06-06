package rs.raf.edu.rma.leaderboard.domain.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import rs.raf.edu.rma.leaderboard.domain.QuizCategory

object QuizCategorySerializer : KSerializer<QuizCategory> {

    override val descriptor = PrimitiveSerialDescriptor("QuizCategorySerializer", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: QuizCategory) {
        encoder.encodeInt(value.categoryId)
    }

    override fun deserialize(decoder: Decoder): QuizCategory {
        return when (decoder) {
            is JsonDecoder -> {
                val jsonElement = decoder.decodeJsonElement()
                if (jsonElement.jsonPrimitive.isString) {
                    when (val value = jsonElement.jsonPrimitive.content.lowercase()) {
                        "GuessTheFact".lowercase() -> QuizCategory.GuessTheFact
                        "GuessTheCat".lowercase() -> QuizCategory.GuessTheCat
                        "LeftOrRight".lowercase() -> QuizCategory.LeftOrRight
                        else -> throw IllegalArgumentException("Invalid QuizCategory value: $value")
                    }
                } else {
                    when (val value = jsonElement.jsonPrimitive.intOrNull) {
                        1 -> QuizCategory.GuessTheFact
                        2 -> QuizCategory.GuessTheCat
                        3 -> QuizCategory.LeftOrRight
                        else -> throw IllegalArgumentException("Invalid QuizCategory value: $value")
                    }
                }
            }
            else -> error("Only JSON supported for deserialization.")
        }
    }
}
