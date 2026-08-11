package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.GeneralConfig
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineTemplate
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class MineSessionFactory(
    private val plugin: Plugin
) {

    fun createSession(
        owner: Player, targetIdentity: Key, template: MineTemplate, editedStage: Int
    ): MineSession {
        val general = GeneralConfig()
        return CreationMineSession(
            createSessionKey(targetIdentity), owner, targetIdentity,
            template, general.editorItem.compose(), editedStage
        )
    }

    fun createSession(mine: Mine, owner: Player, editedStage: Int): MineSession {
        val general = GeneralConfig()
        return EditMineSession(
            createSessionKey(mine.key()), mine,
            owner, general.editorItem.compose(), editedStage
        )
    }

    private fun createSessionKey(identity: Key) : NamespacedKey {
        val key = MinesConstants.SESSION_IDENTITY + identity.hashCode()
        return NamespacedKey(plugin, key)
    }
}