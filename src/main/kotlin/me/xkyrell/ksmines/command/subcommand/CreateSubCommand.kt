package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.provider.TemplateSuggestionProvider
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.LanguageConfig
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.mine.MineTemplate
import me.xkyrell.ksmines.mine.service.MineResolver
import me.xkyrell.ksmines.mine.service.MineSessionService
import me.xkyrell.ksmines.mine.session.MineSessionFactory
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import javax.inject.Inject

class CreateSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val minesConfigLoader: MinesConfigLoader,
    private val mineResolver: MineResolver,
    private val sessionService: MineSessionService,
    private val sessionFactory: MineSessionFactory
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("create")
            .requires { it.sender is Player }
            .then(Commands.argument("key", ArgumentTypes.key())
                .then(Commands.argument("template", StringArgumentType.word())
                    .suggests(TemplateSuggestionProvider(minesConfigLoader))
                    .executes(::handleMineCreation)
                )
            )
            .build()
    }

    private fun handleMineCreation(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.sender as Player
        val language = commonConfigLoader.getLanguageConfig()
        if (!player.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            player.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val key = ctx.getArgument("key", Key::class.java)
        if (sessionService.getResolver().resolveSession(player) != null) {
            player.sendMessage(language.session.alreadyInSession.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        if (mineResolver.resolve(key) != null) {
            player.sendMessage(language.mineAlreadyExists.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key.asString())
            ))

            return Command.SINGLE_SUCCESS
        }

        val template = ctx.getArgument("template", String::class.java)
        val trustedTemplate = minesConfigLoader.getLoadedTemplates()[template]
        if (trustedTemplate == null) {
            player.sendMessage(language.templateNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("template", template)
            ))

            return Command.SINGLE_SUCCESS
        }

        createMineSession(player, key.asString(), trustedTemplate, language);
        return Command.SINGLE_SUCCESS
    }

    private fun createMineSession(
        player: Player, key: String, template: MineTemplate, language: LanguageConfig
    ) {
        val targetIdentity: Key = Key.key(player.world.name, key)
        val session = sessionFactory.createSession(
            player, targetIdentity, template, 0
        )

        sessionService.register(session)
        player.sendMessage(language.session.started.asComponent(
                Placeholder.component("prefix", language.prefix)
        ))
    }
}