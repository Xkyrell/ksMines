package me.xkyrell.ksmines.listener

import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.hook.PlaceholderApiHook
import me.xkyrell.ksmines.mine.MineImpl
import me.xkyrell.ksmines.mine.block.responsible.DropResponsible
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
import javax.inject.Inject

class MineListener @Inject constructor(
    private val sessionService: MineSessionService,
    private val mineService: MineService,
    private val loader: MinesConfigLoader,
    private val hook: PlaceholderApiHook,
    private val plugin: Plugin
) : Listener {

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

        loader.getLoadedTemplates()
            .mapNotNull { (name, template) ->
                val region = template.region ?: return@mapNotNull null
                val namespace = region.minPoint.world.name
                MineImpl(Key.key(namespace, name), template)
            }
            .forEach(mineService::register)

        hook.register()
    }

    @EventHandler
    fun onDisablePlugin(event: PluginDisableEvent) {
        if (plugin == event.plugin) {
            sessionService.unregisterAll()
            mineService.unregisterAll(true)
            hook.unregister()
        }
    }
}