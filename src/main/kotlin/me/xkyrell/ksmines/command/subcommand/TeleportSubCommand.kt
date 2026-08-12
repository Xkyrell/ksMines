package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.provider.MineSuggestionProvider
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineResolver
import me.xkyrell.ksmines.util.Cuboid
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Location
import org.bukkit.entity.Player
import javax.inject.Inject

class TeleportSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val mineResolver: MineResolver
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("teleport")
            .requires { it.sender is Player }
            .then(
                Commands.argument("key", StringArgumentType.word())
                .suggests(MineSuggestionProvider(mineResolver))
                .executes(::handleTeleport)
            )
            .build()
    }

    private fun handleTeleport(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source as Player
        val language = commonConfigLoader.getLanguageConfig()
        if (!player.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            player.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val key = ctx.getArgument("key", String::class.java)
        val mine = mineResolver.resolve(key)
        if (mine == null) {
            player.sendMessage(language.mineNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key)
            ))

            return Command.SINGLE_SUCCESS
        }

        val region = mine.getTemplate().region
        if (region == null) {
            player.sendMessage(language.mineRegionMissing.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key)
            ))

            return Command.SINGLE_SUCCESS
        }

        player.teleport(getTeleportLocation(region))
        player.sendMessage(language.mineTeleported.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("mine", key)
        ))

        return Command.SINGLE_SUCCESS
    }

    private fun getTeleportLocation(region: Cuboid): Location {
        val center = region.getCenter()
        return center.add(0.0, region.maxPoint.blockY + 1.5 - center.y, 0.0)
    }
}