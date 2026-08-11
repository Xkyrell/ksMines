package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineImpl
import me.xkyrell.ksmines.mine.MineTemplate
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class EditMineSession(
    private val key: NamespacedKey,
    private val target: Mine,
    owner: Player,
    editorItem: ItemStack,
    private val editedStage: Int,
) : AbstractMineSession(
    owner, target.getTemplate(), editorItem, editedStage,
    target.getTemplate().region, target.getTemplate().refillLocation
) {

    override fun createMine(template: MineTemplate): Mine {
        return MineImpl(target.key(), template)
    }

    override fun getKey(): NamespacedKey = key

    override fun getTargetIdentity(): Key = target.key()

    override fun getTarget(): Mine = target

}