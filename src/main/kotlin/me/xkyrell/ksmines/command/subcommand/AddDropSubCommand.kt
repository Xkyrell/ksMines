package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.argument.MineBlockMaterialArgument
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.block.MineBlockDrop
import me.xkyrell.ksmines.mine.block.MineBlockFactory
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.entity.Player
import javax.inject.Inject

class AddDropSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val sessionResolver: MineSessionResolver,
    private val blockFactory: MineBlockFactory
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("adddrop")
            .requires { it.sender is Player }
            .then(Commands.argument("material", MineBlockMaterialArgument(sessionResolver))
                    .then(Commands.argument("probability", DoubleArgumentType.doubleArg(0.0, 1.0))
                        .executes(::handleAddDrop)
                    )
            )
            .build()
    }

    private fun handleAddDrop(ctx: CommandContext<CommandSourceStack>): Int {
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
        val probability = DoubleArgumentType.getDouble(ctx, "probability")
        if (session.getModifiedBlocks().none { it.getMaterial() == material }) {
            player.sendMessage(language.session.blockNotFound.asComponent(
                Placeholder.component("prefix", language.prefix),
                Placeholder.unparsed("material", material.key.value())
            ))

            return Command.SINGLE_SUCCESS
        }

        val item = player.inventory.itemInMainHand
        if (item.type.isAir || session.isSessionItem(item)) {
            player.sendMessage(language.session.noItemInHand.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val drop = MineBlockDrop(item = item.clone(), probability = probability)
        session.modifyBlockIfPresent { block ->
            if (block.getMaterial() != material) {
                return@modifyBlockIfPresent null
            }

            blockFactory.createBlock(
                delegate = block,
                drops = drop
            )
        }

        player.sendMessage(language.session.dropAdded.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("material", material.key.value())
        ))

        return Command.SINGLE_SUCCESS
    }
}