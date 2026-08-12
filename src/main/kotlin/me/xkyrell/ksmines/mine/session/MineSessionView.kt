package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.block.MineBlock
import net.kyori.adventure.key.Key
import org.bukkit.Keyed
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface MineSessionView : Keyed {

    fun getOwner(): Player

    fun getEditedStage(): Int

    fun getTargetIdentity(): Key

    fun getModifiedBlocks() : Set<MineBlock>

    fun getTarget(): Mine?

    fun isClosed(): Boolean

    fun isSessionItem(item: ItemStack): Boolean

}