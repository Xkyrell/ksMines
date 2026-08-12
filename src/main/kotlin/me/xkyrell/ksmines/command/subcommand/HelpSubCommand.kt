package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.CommonConfigLoader
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import javax.inject.Inject

class HelpSubCommand @Inject constructor(
    private val loader: CommonConfigLoader
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("help")
            .executes {
                val sender = it.source.sender
                val language = loader.getLanguageConfig()
                if (!sender.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
                    sender.sendMessage(language.noPermission.asComponent(
                        Placeholder.component("prefix", language.prefix)
                    ))

                    return@executes Command.SINGLE_SUCCESS
                }

                loader.getLanguageConfig().help.layout.forEach { line ->
                    sender.sendMessage(line)
                }

                return@executes Command.SINGLE_SUCCESS
            }
            .build()
    }
}