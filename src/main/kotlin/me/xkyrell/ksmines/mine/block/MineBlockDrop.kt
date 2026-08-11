package me.xkyrell.ksmines.mine.block

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.util.random.Weighted
import org.bukkit.inventory.ItemStack

@Serializable
data class MineBlockDrop(
    @Contextual val item: ItemStack,
    val probability: Double = 1.0
) : Weighted {

    init {
        require(probability in 0.0..1.0) {
            "probability must be between 0.0 and 1.0"
        }
    }

    override fun getProbability(): Double = probability;

}
