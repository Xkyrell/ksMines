package me.xkyrell.ksmines.mine.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.xkyrell.ksmines.mine.block.responsible.DropResponsible
import me.xkyrell.ksmines.util.random.WeightSet
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent

@Serializable
@SerialName("default")
internal class MineBlockImpl(
    private val material: Material,
    private val probability: Double,
    private val customDrops: Set<MineBlockDrop>? = null
) : MineBlock, DropResponsible {

    @Transient
    private val weightedDrops: WeightSet<MineBlockDrop>? = customDrops?.let {
        WeightSet.create(it)
    }

    init {
        require(probability in 0.0..1.0) {
            "probability must be between 0.0 and 1.0"
        }
    }

    override fun onDrop(event: BlockBreakEvent) {
        if (weightedDrops.isNullOrEmpty()) return

        event.isDropItems = false
        event.expToDrop = 0

        val drop = weightedDrops.getRandomItem()
        event.block.world.dropItemNaturally(
            event.block.location,
            drop.item.clone()
        )
    }

    override fun getMaterial(): Material = material

    override fun getProbability(): Double = probability

    fun getCustomDrops(): Set<MineBlockDrop>? = customDrops

}