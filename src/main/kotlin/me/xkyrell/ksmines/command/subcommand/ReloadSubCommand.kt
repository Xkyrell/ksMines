package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.MinesConfigLoader
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import javax.inject.Inject

class ReloadSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val minesConfigLoader: MinesConfigLoader
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

                commonConfigLoader.reload()
                minesConfigLoader.reload()

                sender.sendMessage(language.reload.asComponent(
                    Placeholder.component("prefix", language.prefix)
                ))

                return@executes Command.SINGLE_SUCCESS
            }
            .build()
    }
}