package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.MessageRenderers
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineResolver
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import javax.inject.Inject

class ListSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val mineResolver: MineResolver
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("list")
            .executes {
                val sender = it.source.sender
                val language = loader.getLanguageConfig()
                if (!sender.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
                    sender.sendMessage(language.noPermission.asComponent(
                        Placeholder.component("prefix", language.prefix)
                    ))

                    return@executes Command.SINGLE_SUCCESS
                }

                val mines = mineResolver.getMines()
                MessageRenderers.MINE_LIST.sendMessage(
                    sender, language.mineList.layout, mines, language
                )
                return@executes Command.SINGLE_SUCCESS
            }
            .build()
    }
}