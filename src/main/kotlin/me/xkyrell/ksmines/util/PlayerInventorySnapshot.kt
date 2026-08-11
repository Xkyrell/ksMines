package me.xkyrell.ksmines.util

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerInventorySnapshot private constructor(
    private val contents: Array<ItemStack?>,
    private val armor: Array<ItemStack?>,
    private val offHand: ItemStack?
) {

    fun restore(player: Player) {
        val inventory = player.inventory

        inventory.contents = contents
            .map { it?.clone() }
            .toTypedArray()

        inventory.armorContents = armor
            .map { it?.clone() }
            .toTypedArray()

        inventory.setItemInOffHand(offHand?.clone())
    }

    companion object {

        fun capture(player: Player): PlayerInventorySnapshot {
            val inventory = player.inventory
            return PlayerInventorySnapshot(
                contents = inventory.contents
                    .map { it?.clone() }
                    .toTypedArray(),

                armor = inventory.armorContents
                    .map { it?.clone() }
                    .toTypedArray(),

                offHand = inventory.itemInOffHand.clone()
            )
        }
    }
}