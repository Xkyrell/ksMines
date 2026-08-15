package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class EditMineSession(
    private val key: NamespacedKey,
    private val target: Mine,
    private val owner: Player,
    private val editorItem: ItemStack,
    private val editedStage: Int,
) : PreparedMineSession(
    key, owner, target.key(), target.getTemplate(),
    editorItem, editedStage, target.getTemplate().region,
    target.getTemplate().refillLocation
) {

    override fun getTarget(): Mine = target

}