package me.xkyrell.ksmines.util

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.Location
import org.bukkit.World

@Serializable
class Cuboid(
    @Contextual val minPoint: Location,
    @Contextual val maxPoint: Location
) : Iterable<ChunkPosition> {

    init {
        requireNotNull(minPoint.world) {
            "minPoint world cannot be null"
        }

        requireNotNull(maxPoint.world) {
            "maxPoint world cannot be null"
        }

        require(minPoint.world == maxPoint.world) {
            "minPoint and maxPoint must be in the same world"
        }

        require(minPoint.blockX <= maxPoint.blockX) {
            "minPoint must not exceed maxPoint on X"
        }

        require(minPoint.blockY <= maxPoint.blockY) {
            "minPoint must not exceed maxPoint on Y"
        }

        require(minPoint.blockZ <= maxPoint.blockZ) {
            "minPoint must not exceed maxPoint on Z"
        }
    }

    val world: World get() = minPoint.world!!

    fun contains(location: Location): Boolean =
        location.world == world &&
                contains(
                    location.blockX,
                    location.blockY,
                    location.blockZ
                )

    fun contains(x: Int, y: Int, z: Int): Boolean =
        x in minPoint.blockX..maxPoint.blockX &&
                y in minPoint.blockY..maxPoint.blockY &&
                z in minPoint.blockZ..maxPoint.blockZ

    fun getCenter(): Location =
        Location(
            world,
            (minPoint.x + maxPoint.x) / 2.0,
            (minPoint.y + maxPoint.y) / 2.0,
            (minPoint.z + maxPoint.z) / 2.0
        )

    fun getSizeX(): Int =
        maxPoint.blockX - minPoint.blockX + 1

    fun getSizeY(): Int =
        maxPoint.blockY - minPoint.blockY + 1

    fun getSizeZ(): Int =
        maxPoint.blockZ - minPoint.blockZ + 1

    fun getSize(): Int =
        getSizeX() * getSizeY() * getSizeZ()

    override fun iterator(): Iterator<ChunkPosition> {
        return CuboidIterator(minPoint, maxPoint)
    }

    companion object {

        fun of(
            first: Location,
            second: Location
        ): Cuboid {
            require(first.world == second.world) {
                "Locations must be in the same world"
            }

            return Cuboid(
                minPoint = Location(
                    requireNotNull(first.world),
                    minOf(first.blockX, second.blockX).toDouble(),
                    minOf(first.blockY, second.blockY).toDouble(),
                    minOf(first.blockZ, second.blockZ).toDouble()
                ),
                maxPoint = Location(
                    requireNotNull(first.world),
                    maxOf(first.blockX, second.blockX).toDouble(),
                    maxOf(first.blockY, second.blockY).toDouble(),
                    maxOf(first.blockZ, second.blockZ).toDouble()
                )
            )
        }
    }

    private class CuboidIterator(
        min: Location,
        max: Location
    ) : Iterator<ChunkPosition> {

        private val baseX = min.blockX shr 4
        private val baseZ = min.blockZ shr 4
        private val chunkSizeX = (max.blockX shr 4) - baseX + 1
        private val chunkSizeZ = (max.blockZ shr 4) - baseZ + 1
        private val totalSize = chunkSizeX * chunkSizeZ
        private var index = 0

        override fun hasNext(): Boolean = index < totalSize

        override fun next(): ChunkPosition {
            if (!hasNext()) throw NoSuchElementException()

            val x = index % chunkSizeX
            val z = index / chunkSizeX

            index++
            return ChunkPosition(baseX + x, baseZ + z)
        }
    }
}