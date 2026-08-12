package me.xkyrell.ksmines.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import java.util.concurrent.CompletableFuture

class BlockMaterialArgument : CustomArgumentType.Converted<Material, String> {

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
        context: CommandContext<S>, builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val remaining = builder.remainingLowerCase
        Registry.MATERIAL.forEach { material ->
            if (!material.isBlock) return@forEach
            val key = material.key.value()
            if (key.startsWith(remaining)) {
                builder.suggest(key)
            }
        }
        return builder.buildFuture()
    }
}