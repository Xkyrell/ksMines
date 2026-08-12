package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import me.xkyrell.ksmines.command.provider.MineSuggestionProvider
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.service.MineResolver
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

abstract class AbstractMineActionSubCommand(
    private val commonConfigLoader: CommonConfigLoader,
    private val mineResolver: MineResolver,
    private val literal: String,
    private val permission: String
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal(literal)
            .requires { it.sender is Player }
            .then(Commands.argument("key", ArgumentTypes.key())
                .suggests(MineSuggestionProvider(mineResolver))
                .executes(::handleManualAction)
            )
            .build()
    }

    private fun handleManualAction(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val language = commonConfigLoader.getLanguageConfig()
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val key = ctx.getArgument("key", Key::class.java)
        val mine = mineResolver.resolve(key)
        if (mine == null) {
            sender.sendMessage(language.mineNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key.asString())
            ))

            return Command.SINGLE_SUCCESS
        }

        if (!mine.isActive()) {
            sender.sendMessage(language.mineInactive.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key.asString())
            ))

            return Command.SINGLE_SUCCESS
        }

        handleManualActionInternal(mine, sender as Player)
        return Command.SINGLE_SUCCESS
    }

    protected abstract fun handleManualActionInternal(mine: Mine, sender: Player)

}