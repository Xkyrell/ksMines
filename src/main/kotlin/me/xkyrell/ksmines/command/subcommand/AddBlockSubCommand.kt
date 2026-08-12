package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.argument.BlockMaterialArgument
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.LanguageConfig
import me.xkyrell.ksmines.mine.block.MineBlockDrop
import me.xkyrell.ksmines.mine.block.MineBlockFactory
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import me.xkyrell.ksmines.mine.session.MineSession
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import javax.inject.Inject

class AddBlockSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val sessionResolver: MineSessionResolver,
    private val blockFactory: MineBlockFactory,
) : SubCommand {

    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("addblock")
            .requires { it.sender is Player }
            .then(Commands.argument("material", BlockMaterialArgument())
                .then(Commands.argument("probability", DoubleArgumentType.doubleArg(0.0, 1.0))
                    .executes {
                        handleAddBlock(it) { _, _ -> emptySet() }
                    }
                    .then(Commands.literal("--drop-from-hand")
                        .then(Commands.argument("drop-probability", DoubleArgumentType.doubleArg(0.0, 1.0))
                            .executes(::handleBlockAndDropFromHand)
                        )
                    )
                    .then(Commands.literal("--drop-from-inventory")
                        .then(Commands.argument("drop-probability", DoubleArgumentType.doubleArg(0.0, 1.0))
                            .executes(::handleBlockAndDropFromInventory)
                        )
                    )
                )
            )
            .build()
    }

    private fun handleBlockAndDropFromHand(ctx: CommandContext<CommandSourceStack>): Int {
        val dropProbability = DoubleArgumentType.getDouble(ctx, "drop-probability")
        return handleAddBlock(ctx) { player, session ->
            dropsFromHand(player, session, dropProbability, loader.getLanguageConfig())
        }
    }

    private fun handleBlockAndDropFromInventory(ctx: CommandContext<CommandSourceStack>): Int {
        val dropProbability = DoubleArgumentType.getDouble(ctx, "drop-probability")
        return handleAddBlock(ctx) { player, session ->
            dropsFromInventory(player, session, dropProbability, loader.getLanguageConfig())
        }
    }

    private fun handleAddBlock(
        ctx: CommandContext<CommandSourceStack>, drops: (Player, MineSession) -> Set<MineBlockDrop>?
    ): Int {
        val player = ctx.source as Player
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
        if (material.isAir) {
            player.sendMessage(language.session.invalidBlock.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return Command.SINGLE_SUCCESS
        }

        val probability = ctx.getArgument("probability", Double::class.java)
        val customDrops = drops(player, session) ?: return Command.SINGLE_SUCCESS

        val block = blockFactory.createBlock(material, probability, customDrops)
        session.removeBlockIf { it.getMaterial() == material }
        session.addBlock(block)
        player.sendMessage(language.session.blockAdded.asComponent(
            Placeholder.component("prefix", language.prefix),
            Placeholder.unparsed("material", material.key.value())
        ))

        return Command.SINGLE_SUCCESS
    }

    private fun dropsFromHand(
        player: Player, session: MineSession,
        probability: Double, language: LanguageConfig
    ): Set<MineBlockDrop>? {
        val item = player.inventory.itemInMainHand
        if (item.type.isAir || session.isSessionItem(item)) {
            player.sendMessage(language.session.noItemInHand.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return null
        }

        return setOf(MineBlockDrop(item.clone(), probability))
    }

    private fun dropsFromInventory(
        player: Player, session: MineSession,
        probability: Double, language: LanguageConfig
    ): Set<MineBlockDrop>? {
        val items: List<ItemStack> = player.inventory.contents
            .filterNotNull()
            .filter { !it.type.isAir && !session.isSessionItem(it) }

        if (items.isEmpty()) {
            player.sendMessage(language.session.noItemsInInventory.asComponent(
                Placeholder.component("prefix", language.prefix)
            ))

            return null
        }

        return items.map { MineBlockDrop(it.clone(), probability) }.toSet()
    }
}