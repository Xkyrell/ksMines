package me.xkyrell.ksmines.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import me.xkyrell.ksmines.mine.service.MineSessionResolver
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

class MineBlockMaterialArgument(
    private val sessionResolver: MineSessionResolver
) : CustomArgumentType.Converted<Material, String> {

    override fun convert(nativeType: String): Material {
        val material = Registry.MATERIAL.get(
            NamespacedKey.minecraft(nativeType.lowercase())
        )

        if (material == null || !material.isBlock) {
            return Material.AIR
        }

        return material

    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val player = (context.source as CommandSourceStack).sender as Player
        val session = sessionResolver.resolveSession(player)
            ?: return builder.buildFuture()

        val remaining = builder.remainingLowerCase
        session.getModifiedBlocks()
            .map { it.getMaterial().key.value() }
            .distinct()
            .filter { it.startsWith(remaining) }
            .forEach(builder::suggest)

        return builder.buildFuture()
    }
}