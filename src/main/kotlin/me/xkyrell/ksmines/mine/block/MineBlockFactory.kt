package me.xkyrell.ksmines.mine.block

import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import javax.inject.Inject

class MineBlockFactory @Inject constructor() {

    fun createBlock(
        delegate: MineBlock,
        material: Material = delegate.getMaterial(),
        probability: Double = delegate.getProbability(),
        drops: MineBlockDrop? = null
    ): MineBlock {
        val existingDrops = getDrops(delegate)

        val customDrops = drops
            ?.let { (existingDrops + it).toSet() }
            ?.takeIf { it.isNotEmpty() }

        return MineBlockImpl(
            material = material,
            probability = probability,
            customDrops = customDrops
        )
    }

    fun createBlockWithoutDrop(
        delegate: MineBlock,
        item: ItemStack
    ): MineBlock? {
        val drops = getDrops(delegate)
        val remainingDrops = drops
            .filterNot { it.item.isSimilar(item) }
            .toSet()

        if (remainingDrops.size == drops.size) {
            return null
        }

        return createBlock(
            delegate.getMaterial(),
            delegate.getProbability(),
            remainingDrops
        )
    }

    private fun getDrops(block: MineBlock): Set<MineBlockDrop> {
        return (block as? MineBlockImpl)?.getCustomDrops().orEmpty()
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