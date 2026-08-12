package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.argument.MineBlockMaterialArgument
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.entity.Player
import javax.inject.Inject

class RemoveBlockSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val sessionResolver: MineSessionResolver
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("removeblock")
            .requires { it.sender is Player }
            .then(Commands.argument("material", MineBlockMaterialArgument(sessionResolver))
                .executes(::handleBlock)
            )
            .build()
    }

    private fun handleBlock(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.sender as Player
        val language = loader.getLanguageConfig()
        if (!player.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            player.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val session = sessionResolver.resolveSession(player)
        if (session == null) {
            player.sendMessage(language.session.noActiveSession.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val material = ctx.getArgument("material", Material::class.java)
        if (session.getModifiedBlocks().none { it.getMaterial() == material }) {
            player.sendMessage(language.session.blockNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("material", material.key.value())
            ))

            return Command.SINGLE_SUCCESS
        }

        session.removeBlockIf { it.getMaterial() == material }
        player.sendMessage(language.session.blockRemoved.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("material", material.key.value())
        ))

        return Command.SINGLE_SUCCESS
    }
}