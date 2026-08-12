package me.xkyrell.ksmines.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.xkyrell.ksmines.util.TemporalStyles
import me.xkyrell.temporal.Temporal

class TemporalSerializer : KSerializer<Temporal> {

    override val descriptor = PrimitiveSerialDescriptor("temporal", PrimitiveKind.STRING)

    override fun deserialize(
        decoder: Decoder
    ): Temporal {
        val value = decoder.decodeString()
        if (value.isBlank()) {
            return Temporal.ZERO
        }

        try {
            return Temporal.parse(value,
                TemporalStyles.DEFAULT_STYLE
            )
        }
        catch (exception: Exception) {
            throw IllegalArgumentException(
                "Invalid Temporal value: $value",
                exception
            )
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: Temporal
    ) {
        val format = value.formatAs(TemporalStyles.DEFAULT_STYLE)
        encoder.encodeString(normalize(format))
    }

    // Temporary workaround
    private fun normalize(value: String): String =
        Regex("(\\d+)\\s*([A-Za-z]+)")
            .findAll(value)
            .joinToString(" ") {
                "${it.groupValues[1]}${it.groupValues[2]}"
            }
}