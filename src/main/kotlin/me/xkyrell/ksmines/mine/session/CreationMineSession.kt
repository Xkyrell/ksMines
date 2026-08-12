package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineImpl
import me.xkyrell.ksmines.mine.MineTemplate
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class CreationMineSession(
    private val key: NamespacedKey,
    private val owner: Player,
    private val targetIdentity: Key,
    private val template: MineTemplate,
    private val editorItem: ItemStack,
    private val editedStage: Int,
) : AbstractMineSession(
    key, owner, targetIdentity, template,
    editorItem, editedStage, null, null
) {

    override fun createMine(template: MineTemplate): Mine {
        return MineImpl(targetIdentity, template)
    }

    override fun getTarget(): Mine? = null

}