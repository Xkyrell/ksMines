package me.xkyrell.ksmines.util

import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World

fun World.fillMineChunk(
    region: Cuboid,
    blocks: WeightSet<MineBlock>,
    position: ChunkPosition
) {
    forEachChunkInternal(region, position) { chunk, x, y, z ->
        val material = blocks.getRandomItem().getMaterial()
        chunk.getBlock(x, y, z).setType(material, false)
    }
}

fun World.clearMineChunk(
    region: Cuboid,
    position: ChunkPosition
) {
    forEachChunkInternal(region, position) { chunk, x, y, z ->
        chunk.getBlock(x, y, z).setType(Material.AIR, false)
    }
}

private fun World.forEachChunkInternal(
    region: Cuboid,
    position: ChunkPosition,
    action: (Chunk, Int, Int, Int) -> Unit
) {
    val chunk = getChunkAt(position.x, position.z)
    val chunkMinX = position.x shl 4
    val chunkMaxX = chunkMinX + 15
    val chunkMinZ = position.z shl 4
    val chunkMaxZ = chunkMinZ + 15

    val minX = maxOf(chunkMinX, region.minPoint.blockX)
    val maxX = minOf(chunkMaxX, region.maxPoint.blockX)
    val minZ = maxOf(chunkMinZ, region.minPoint.blockZ)
    val maxZ = minOf(chunkMaxZ, region.maxPoint.blockZ)
    for (x in minX..maxX) {
        val localX = x and 15
        for (z in minZ..maxZ) {
            val localZ = z and 15
            for (y in region.minPoint.blockY..region.maxPoint.blockY) {
                action(chunk, localX, y, localZ)
            }
        }
    }
}