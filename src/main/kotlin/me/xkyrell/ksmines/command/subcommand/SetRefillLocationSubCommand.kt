package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import me.xkyrell.ksmines.util.formatted
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Location
import org.bukkit.entity.Player
import javax.inject.Inject

class SetRefillLocationSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val sessionResolver: MineSessionResolver
) : SubCommand {

    @Suppress("UnstableApiUsage")
    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("setrefilllocation")
            .requires { it.sender is Player }
            .executes {
                handleSetLocation(it) {
                    val player = it.source.sender as Player
                    player.location.clone().add(0.0, 0.5, 0.0)
                }
            }
            .then(
                Commands.argument("location", ArgumentTypes.finePosition())
                    .executes {
                        val player = it.source.sender as Player
                        val position = it.getArgument(
                            "location", FinePositionResolver::class.java
                        )

                        handleSetLocation(it) {
                            position.resolve(it.source).toLocation(player.world)
                        }
                    }
            )
            .build()
    }

    private fun handleSetLocation(
        ctx: CommandContext<CommandSourceStack>,
        locationProvider: () -> Location
    ): Int {
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

        val location = locationProvider()
        session.setRefillLocation(location)
        player.sendMessage(language.session.refillLocationSet.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("location", location.formatted())
        ))

        return Command.SINGLE_SUCCESS
    }
}