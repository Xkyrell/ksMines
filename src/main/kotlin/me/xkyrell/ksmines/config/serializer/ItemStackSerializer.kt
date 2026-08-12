package me.xkyrell.ksmines.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.inventory.ItemStack
import java.util.Base64

class ItemStackSerializer : KSerializer<ItemStack> {

    override val descriptor =
        PrimitiveSerialDescriptor(
            "item",
            PrimitiveKind.STRING
        )

    override fun deserialize(
        decoder: Decoder
    ): ItemStack {
        val value = decoder.decodeString()
        if (value.isBlank()) {
            return ItemStack.empty()
        }

        try {
            return ItemStack.deserializeBytes(
                Base64.getDecoder().decode(value)
            )
        }
        catch (exception: IllegalArgumentException) {
            throw SerializationException(
                "Invalid Base64 ItemStack", exception
            )
        }
        catch (exception: Exception) {
            throw SerializationException(
                "Failed to deserialize ItemStack", exception
            )
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: ItemStack
    ) {
        encoder.encodeString(
            Base64.getEncoder().encodeToString(value.serializeAsBytes())
        )
    }
}