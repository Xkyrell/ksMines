package me.xkyrell.ksmines.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry

abstract class KeyedSerializer<T : Keyed>(
    type: String,
    private val registry: Registry<T>
) : KSerializer<T> {

    override val descriptor = PrimitiveSerialDescriptor(type, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): T {
        val value = decoder.decodeString()

        val key = NamespacedKey.fromString(value)
            ?: throw IllegalArgumentException(
                "Invalid $descriptor key: $value"
            )

        return registry.get(key)
            ?: throw IllegalArgumentException(
                "Unknown $descriptor key: $value"
            )
    }

    override fun serialize(
        encoder: Encoder,
        value: T
    ) {
        encoder.encodeString(value.key.asMinimalString())
    }
}