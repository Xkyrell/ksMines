package me.xkyrell.ksmines

import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.service.MineResolver
import me.xkyrell.ksmines.util.*
import me.xkyrell.ksmines.util.locale.LocaleBiListRenderer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import javax.inject.Inject

class MineTask @Inject constructor (
    private val plugin: Plugin,
    private val loader: CommonConfigLoader,
    private val resolver: MineResolver
) {

    private var task: BukkitTask? = null

    fun start() {
        check(task == null) {
            "task is already started"
        }

        task = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            resolver.getActiveMines().forEach { mine ->
                    val observable = mine as? Observable ?: return@forEach
                    handleMine(mine, observable.onObserve())
                }
            }, 20L, 20L)
    }

    private fun handleMine(mine: Mine, result: Observable.Result) {
        val stage = mine.getCurrentStage()
        val prefix = loader.getLanguageConfig().prefix
        when (result) {
            Observable.Result.REFILLED -> {
                stage.refillNotification?.let {
                    MessageRenderers.NOTIFICATION.sendMessageForAll(
                        plugin.server, it, mine, prefix.asComponent()
                    )
                }
                stage.refillSound?.playForAll(plugin.server)
            }

            Observable.Result.ROTATED -> {
                stage.activationNotification?.let {
                    MessageRenderers.NOTIFICATION.sendMessageForAll(
                        plugin.server, it, mine, prefix.asComponent()
                    )
                }
                stage.activationSound?.playForAll(plugin.server)
            }
            else -> Unit
        }
    }

    fun shutdown() {
        task?.cancel()
        task = null
    }

    private object MessageRenderers {

        val NOTIFICATION = LocaleBiListRenderer<Mine, Component> { locales, mine, prefix ->
            val stage = mine.getCurrentStage()
            val nextStage = mine.getNextStage()
            val location = mine.getTemplate()
                .getRegionOrThrow()
                .getCenter()
                .formatted()

            locales.map { section -> section.asComponent(
                    Placeholder.component("prefix", prefix),
                    Placeholder.unparsed("location", location),
                    Placeholder.component("current_stage", stage.displayName),
                    Placeholder.component("next_stage", nextStage.displayName)
                )
            }
        }
    }
}