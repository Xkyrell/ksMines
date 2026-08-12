package me.xkyrell.ksmines.command

import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import me.xkyrell.ksmines.command.subcommand.HelpSubCommand
import me.xkyrell.ksmines.command.subcommand.SubCommand
import org.bukkit.plugin.Plugin
import javax.inject.Inject

class MainCommand @Inject constructor(
    subCommands: @JvmSuppressWildcards Set<SubCommand>
) {

    private val subCommands = subCommands
        .sortedByDescending { it is HelpSubCommand }

    fun register(plugin: Plugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val registrar: Commands = event.registrar()
            registrar.register(build(),
                "A command provided by ksPlugins.",
                listOf("ksmine", "mines", "mine")
            )
        }
    }

    private fun build(): LiteralCommandNode<CommandSourceStack> {
        val helpSubCommand = subCommands.first().build()
        val root = Commands.literal("ksmines")
            .executes(helpSubCommand.command)
            .then(helpSubCommand)

        subCommands.forEach { root.then(it.build()) }
        return root.build()
    }
}