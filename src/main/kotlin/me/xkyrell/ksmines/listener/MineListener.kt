package me.xkyrell.ksmines.listener

import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.hook.PlaceholderApiHook
import me.xkyrell.ksmines.mine.MineFactory
import me.xkyrell.ksmines.mine.block.responsible.DropResponsible
import me.xkyrell.ksmines.mine.event.MineRegionOperationEvent
import me.xkyrell.ksmines.mine.service.MineService
import me.xkyrell.ksmines.mine.service.MineSessionService
import net.kyori.adventure.key.Key
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MineListener @Inject constructor(
    private val mineFactory: MineFactory,
    private val sessionService: MineSessionService,
    private val mineService: MineService,
    private val commonConfigLoader: CommonConfigLoader,
    private val minesConfigLoader: MinesConfigLoader,
    private val hook: PlaceholderApiHook,
    private val plugin: Plugin
) : Listener {

    @EventHandler
    fun onRegionOperation(event: MineRegionOperationEvent) {
        val refillLocation = event.mine.getTemplate().refillLocation ?: return
        val title = commonConfigLoader.getLanguageConfig().refillTeleport
        event.nearbyPlayers.forEach { player ->
            player.scheduler.run(plugin, {
                player.teleportAsync(refillLocation)
            }, null)

            title.sendTitle(player)
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        val block = event.block
        val mine = mineService.getResolver().getActiveMines()
            .firstOrNull {
                it.getTemplate().region?.contains(block.location) ?: false
            } ?: return

        val mineBlock = mine.getCurrentStage().blocks
            .firstOrNull { it.getMaterial() == block.type }
            ?: return

        if (mineBlock is DropResponsible) {
            mineBlock.onDrop(event)
        }
    }

    @EventHandler
    fun onEnablePlugin(event: PluginEnableEvent) {
        if (plugin != event.plugin) return

        minesConfigLoader.getLoadedTemplates()
            .mapNotNull { (name, template) ->
                val region = template.region ?: return@mapNotNull null
                val namespace = region.minPoint.world.name
                mineFactory.createMine(Key.key(namespace, name), template)
            }
            .forEach { mine ->
                mineService.register(mine)
                plugin.server.asyncScheduler.runDelayed(plugin, {
                    mine.refillSilently()
                }, 1L, TimeUnit.SECONDS)
            }

        hook.register()
    }

    @EventHandler
    fun onDisablePlugin(event: PluginDisableEvent) {
        if (plugin == event.plugin) {
            sessionService.unregisterAll()
            mineService.unregisterAll()
            hook.unregister()
        }
    }
}