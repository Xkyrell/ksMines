package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineTemplate
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import javax.inject.Inject

class MineSessionFactory @Inject constructor(
    private val plugin: Plugin,
    private val loader: CommonConfigLoader
) {

    fun createSession(
        owner: Player, targetIdentity: Key, template: MineTemplate, editedStage: Int
    ): MineSession {
        val editorItem = loader.getGeneralConfig().editorItem
        return PreparedMineSession(createSessionKey(targetIdentity),
            owner, targetIdentity, template, editorItem.compose(),
            editedStage, null, null
        )
    }

    fun createSession(mine: Mine, owner: Player, editedStage: Int): MineSession {
        val editorItem = loader.getGeneralConfig().editorItem
        return EditMineSession(
            createSessionKey(mine.key()), mine,
            owner, editorItem.compose(), editedStage
        )
    }

    private fun createSessionKey(identity: Key) : NamespacedKey {
        val key = MinesConstants.SESSION_IDENTITY + identity.hashCode()
        return NamespacedKey(plugin, key)
    }
}