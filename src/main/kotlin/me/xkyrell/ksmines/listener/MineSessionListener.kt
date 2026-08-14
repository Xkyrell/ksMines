package me.xkyrell.ksmines.listener

import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.LanguageConfig
import me.xkyrell.ksmines.mine.service.MineSessionService
import me.xkyrell.ksmines.mine.session.MineSession
import me.xkyrell.ksmines.util.formatted
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import javax.inject.Inject

class MineSessionListener @Inject constructor(
    private val sessionService: MineSessionService,
    private val loader: CommonConfigLoader
) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val player = event.player
        val language = loader.getLanguageConfig()
        val clickedLocation = event.clickedBlock?.location ?: return
        val interacted = handleBlockInteraction(player, language) { session ->
            val isMinPoint = action == Action.LEFT_CLICK_BLOCK
            val messageComponent = if (isMinPoint) {
                language.session.regionMinPointSet
            }
            else {
                language.session.regionMaxPointSet
            }

            player.sendMessage(
                messageComponent.asComponent(
                    Placeholder.component("prefix", language.prefix),
                    Placeholder.unparsed("location", clickedLocation.formatted())
                )
            )

            if (isMinPoint) {
                session.modifyMinPoint(clickedLocation)
            }
            else {
                session.modifyMaxPoint(clickedLocation)
            }
        }

        if (interacted) event.isCancelled = true
    }

    private fun handleBlockInteraction(
        player: Player, language: LanguageConfig,
        action: (MineSession) -> Unit
    ): Boolean {
        val session = sessionService.getResolver().resolveSession(player) ?: return false
        if (!loader.getGeneralConfig().allowBlockInteractionInSession) {
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

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        sessionService.unregister(event.player)
    }
}