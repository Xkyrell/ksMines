package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.provider.MineSuggestionProvider
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.LanguageConfig
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineFactory
import me.xkyrell.ksmines.mine.service.MineService
import me.xkyrell.ksmines.mine.service.MineSessionService
import me.xkyrell.ksmines.mine.session.MineSessionFactory
import me.xkyrell.ksmines.mine.session.MineSessionResult
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import javax.inject.Inject

class SessionSubCommand @Inject constructor(
    private val commonConfigLoader: CommonConfigLoader,
    private val minesConfigLoader: MinesConfigLoader,
    private val mineFactory: MineFactory,
    private val mineService: MineService,
    private val sessionService: MineSessionService,
    private val sessionFactory: MineSessionFactory
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("session")
            .requires { it.sender is Player }
            .then(Commands.literal("start")
                .then(Commands.argument("key", ArgumentTypes.key())
                    .suggests(MineSuggestionProvider(mineService.getResolver()))
                    .then(Commands.argument("stage", IntegerArgumentType.integer(1))
                        .executes(::handleStartSession)
                    )
                )
            )
            .then(Commands.literal("terminate")
                .executes {
                    handleTeminateSession(it, false)
                }
                .then(Commands.literal("--refill")
                    .executes {
                        handleTeminateSession(it, true)
                    }
                )
            )
            .build()
    }

    private fun handleStartSession(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.sender as Player
        val language = commonConfigLoader.getLanguageConfig()
        if (!player.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            player.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val key = ctx.getArgument("key", Key::class.java)
        val mine = mineService.getResolver().resolve(key)
        if (mine == null) {
            player.sendMessage(language.mineNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", key.asString())
            ))

            return Command.SINGLE_SUCCESS
        }

        if (sessionService.getResolver().resolveSession(player) != null) {
            player.sendMessage(language.session.alreadyInSession.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val stage = IntegerArgumentType.getInteger(ctx, "stage")
        val stageIndex = stage - 1
        if (stageIndex !in mine.getTemplate().rotationStages.indices) {
            player.sendMessage(language.session.invalidStage.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("stage", stage.toString())
            ))

            return Command.SINGLE_SUCCESS
        }

        val session = sessionFactory.createSession(mine, player, stageIndex)
        sessionService.register(session)
        player.sendMessage(language.session.started.asComponent(
            Placeholder.component("prefix", language.prefix)
        ))

        return Command.SINGLE_SUCCESS
    }

    private fun handleTeminateSession(ctx: CommandContext<CommandSourceStack>, needRefill: Boolean): Int {
        val player = ctx.source.sender as Player
        val language = commonConfigLoader.getLanguageConfig()
        if (!player.hasPermission(MinesConstants.ADMIN_PERMISSION)) {
            player.sendMessage(language.noPermission.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val session = sessionService.terminate(player)
        if (session == null) {
            player.sendMessage(language.session.noActiveSession.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))
            return Command.SINGLE_SUCCESS
        }

        val result = session.applyChanges()
        if (result.noChanges) {
            player.sendMessage(language.session.noChangesSession.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val stage = result.editedStage + 1
        player.sendMessage(language.session.appliedChangesSession.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("stage", stage.toString())
        ))

        applySessionResult(result, needRefill, player, language)
        return Command.SINGLE_SUCCESS
    }

    private fun applySessionResult(
        result: MineSessionResult, needRefill: Boolean,
        player: Player, language: LanguageConfig
    ) {
        val mine: Mine = mineFactory.createMine(result.targetIdentity, result.modifiedTemplate)
        minesConfigLoader.saveTemplate(mine.key().value(), mine.getTemplate())
        if (mineService.getResolver().resolve(mine.key()) == null) {
            player.sendMessage(language.mineCreated.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("mine", mine.key().asString())
            ))
        }
        else {
            mineService.unregister(mine.key())
        }

        mineService.register(mine)
        if (needRefill) mine.refill()
    }
}