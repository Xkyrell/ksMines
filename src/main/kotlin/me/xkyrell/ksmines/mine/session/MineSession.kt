package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.ksmines.util.Cuboid
import org.bukkit.Location

interface MineSession : MineSessionView {

    fun setRegion(region: Cuboid)

    fun modifyMinPoint(location: Location)

    fun modifyMaxPoint(location: Location)

    fun setRefillLocation(location: Location?)

    fun addBlock(block: MineBlock)

    fun removeBlock(block: MineBlock)

    fun removeBlockIf(predicate: (MineBlock) -> Boolean)

    fun modifyBlockIfPresent(modifier: (MineBlock) -> MineBlock?)

    fun applyChanges(): MineSessionResult

    fun close()

}