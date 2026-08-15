package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.mine.MineFactory
import me.xkyrell.ksmines.mine.service.MineService
import me.xkyrell.ksmines.mine.service.MineSessionService
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReloadSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val minesConfigLoader: MinesConfigLoader,
    private val sessionService: MineSessionService,
    private val mineService: MineService,
    private val mineFactory: MineFactory,
    private val plugin: Plugin
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("reload")
            .executes {
                val sender = it.source.sender
                val language = commonConfigLoader.getLanguageConfig()
                if (!sender.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
                    sender.sendMessage(language.noPermission.asComponent(
                        Placeholder.component("prefix", language.prefix)
                    ))

                    return@executes Command.SINGLE_SUCCESS
                }

                sessionService.unregisterAll()
                mineService.unregisterAll()

                commonConfigLoader.reload()
                minesConfigLoader.reload()

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

                sender.sendMessage(language.reload.asComponent(
                    Placeholder.component("prefix", language.prefix)
                ))

                return@executes Command.SINGLE_SUCCESS
            }
            .build()
    }
}