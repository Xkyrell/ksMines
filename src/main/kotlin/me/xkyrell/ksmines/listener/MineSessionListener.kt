package me.xkyrell.ksmines.listener

import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import me.xkyrell.ksmines.mine.session.MineSession
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class MineSessionListener(
    private val resolver: MineSessionResolver,
    private val loader: CommonConfigLoader
) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (handleBlockInteraction(event.player) {
            it.modifyMinPoint(event.block.location)
        }) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (handleBlockInteraction(event.player) {
            it.modifyMaxPoint(event.block.location)
        }) {
            event.isCancelled = true
        }
    }

    private fun handleBlockInteraction(
        player: Player,
        action: (MineSession) -> Unit
    ): Boolean {
        val session = resolver.resolveSession(player) ?: return false
        if (!loader.getGeneralConfig().allowBlockInteractionInSession) {
            val language = loader.getLanguageConfig()
            player.sendMessage(language.session.blockInteractionDenied.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))
            return true
        }

        if (!session.isSessionItem(player.inventory.itemInMainHand)) {
            return false
        }

        action(session)
        return true
    }
}