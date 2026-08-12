package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.provider.MineSuggestionProvider
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.mine.service.MineService
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import javax.inject.Inject

class RemoveSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val mineService: MineService,
    private val minesConfigLoader: MinesConfigLoader
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("remove")
            .requires { it.sender is Player }
            .then(Commands.argument("key", StringArgumentType.word())
                .suggests(MineSuggestionProvider(mineService.getResolver()))
                .executes {
                    handleRemove(it, false)
                }
                .then(Commands.argument("cleanUp", BoolArgumentType.bool())
                    .executes {
                        val cleanUp = it.getArgument("cleanUp", Boolean::class.java)
                        handleRemove(it, cleanUp)
                    }
                ))
            .build()
    }

    private fun handleRemove(ctx: CommandContext<CommandSourceStack>, cleanUp: Boolean): Int {
        val sender = ctx.source.sender
        val language = commonConfigLoader.getLanguageConfig()
        if (!sender.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            sender.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val key = ctx.getArgument("key", String::class.java)
        val mine = mineService.getResolver().resolve(key)
        if (mine == null) {
            sender.sendMessage(language.mineNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key)
            ))

            return Command.SINGLE_SUCCESS
        }

        mineService.unregister(mine.key(), cleanUp)
        minesConfigLoader.removeTemplate(mine.key().value())
        sender.sendMessage(language.mineRemoved.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("mine", mine.key().value())
        ))

        return Command.SINGLE_SUCCESS
    }
}