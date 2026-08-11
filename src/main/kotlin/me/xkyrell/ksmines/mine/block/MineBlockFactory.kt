package me.xkyrell.ksmines.mine.block

import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.Material

class MineBlockFactory {

    fun createBlock(
        delegate: MineBlock,
        material: Material = delegate.getMaterial(),
        probability: Double = delegate.getProbability(),
        customDrops: Set<MineBlockDrop>? =
            (delegate as? MineBlockImpl)?.getCustomDrops()
    ): MineBlock {
        return MineBlockImpl(
            material,
            probability,
            customDrops
        )
    }

    fun createBlock(
        material: Material,
        probability: Double,
        customDrops: Set<MineBlockDrop>? = null
    ): MineBlock {
        if (customDrops.isNullOrEmpty()) {
            return MineBlockImpl(material, probability)
        }

        return MineBlockImpl(
            material,
            probability,
            WeightSet.create(customDrops)
        )
    }
}