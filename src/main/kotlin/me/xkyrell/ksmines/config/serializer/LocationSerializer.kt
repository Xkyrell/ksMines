package me.xkyrell.ksmines.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.Bukkit
import org.bukkit.Location

class LocationSerializer : KSerializer<Location> {

    override val descriptor = buildClassSerialDescriptor("location") {
        element<String>("world")
        element<Double>("x")
        element<Double>("y")
        element<Double>("z")
        element<Float>("yaw")
        element<Float>("pitch")
    }

    override fun deserialize(
        decoder: Decoder
    ): Location {
        var worldName: String? = null
        var x = 0.0
        var y = 0.0
        var z = 0.0
        var yaw = 0f
        var pitch = 0f

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> worldName = decodeStringElement(descriptor, 0)
                    1 -> x = decodeDoubleElement(descriptor, 1)
                    2 -> y = decodeDoubleElement(descriptor, 2)
                    3 -> z = decodeDoubleElement(descriptor, 3)
                    4 -> yaw = decodeFloatElement(descriptor, 4)
                    5 -> pitch = decodeFloatElement(descriptor, 5)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw IllegalArgumentException(
                        "Unknown Location field index: $index"
                    )
                }
            }
        }

        val world = Bukkit.getWorld(
            requireNotNull(worldName) {
                "Location world is missing"
            }
        ) ?: throw SerializationException(
            "Unknown world: $worldName"
        )

        return Location(
            world,
            x,
            y,
            z,
            yaw,
            pitch
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: Location
    ) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(
                descriptor,
                0,
                requireNotNull(value.world).name
            )
            encodeDoubleElement(descriptor, 1, value.x)
            encodeDoubleElement(descriptor, 2, value.y)
            encodeDoubleElement(descriptor, 3, value.z)
            encodeFloatElement(descriptor, 4, value.yaw)
            encodeFloatElement(descriptor, 5, value.pitch)
        }
    }
}