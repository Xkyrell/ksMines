package me.xkyrell.ksmines.mine.block

import me.xkyrell.ksmines.util.Cuboid
import me.xkyrell.ksmines.util.*
import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.plugin.Plugin

internal class InstantBlockProcessor(
    private val plugin: Plugin
) : MineBlockProcessor {

    override fun populate(region: Cuboid, blocks: WeightSet<MineBlock>) {
        val world = region.minPoint.world
        for (position in region) {
            plugin.server.regionScheduler.execute(plugin, world, position.x, position.z) {
                world.fillMineChunk(region, blocks, position)
            }
        }
    }

    override fun depopulate(region: Cuboid) {
        val world = region.minPoint.world
        for (position in region) {
            plugin.server.regionScheduler.execute(plugin, world, position.x, position.z) {
                world.clearMineChunk(region, position)
            }
        }
    }
}