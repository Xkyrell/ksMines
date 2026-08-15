package me.xkyrell.ksmines.mine.block

import me.xkyrell.ksmines.util.ChunkPosition
import me.xkyrell.ksmines.util.Cuboid
import me.xkyrell.ksmines.util.*
import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.World
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

internal class QueueBlockProcessor(
    private val plugin: Plugin
) : MineBlockProcessor {

    override fun populate(region: Cuboid, blocks: WeightSet<MineBlock>) {
        submitRegion(region) { world, position ->
            world.fillMineChunk(region, blocks, position)
        }
    }

    override fun depopulate(region: Cuboid) {
        submitRegion(region) { world, position ->
            world.clearMineChunk(region, position)
        }
    }

    private fun submitRegion(
        region: Cuboid,
        operation: (World, ChunkPosition) -> Unit
    ) {
        val positions = ArrayDeque<ChunkPosition>()
        for (position in region) {
            positions.addLast(position)
        }

        scheduleBatch(region.world, positions, operation)
    }

    private fun scheduleBatch(
        world: World,
        positions: ArrayDeque<ChunkPosition>,
        operation: (World, ChunkPosition) -> Unit
    ) {
        if (positions.isEmpty()) return

        plugin.server.asyncScheduler.runDelayed(plugin, {
            var processedChunks = 0
            while (processedChunks < CHUNKS_PER_BATCH && positions.isNotEmpty()) {
                val position = positions.removeFirst()
                plugin.server.regionScheduler.execute(plugin, world, position.x, position.z
                ) {
                    operation(world, position)
                }
                processedChunks++
            }

            scheduleBatch(world, positions, operation)
        }, 200L, TimeUnit.MILLISECONDS)
    }

    companion object {

        private const val CHUNKS_PER_BATCH = 2

    }
}