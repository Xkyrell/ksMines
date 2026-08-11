package me.xkyrell.ksmines.mine.block

import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.util.random.Weighted
import org.bukkit.Material

@Serializable
sealed interface MineBlock : Weighted {

    fun getMaterial(): Material

}