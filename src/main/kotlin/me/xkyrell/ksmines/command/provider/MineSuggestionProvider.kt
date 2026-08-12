package me.xkyrell.ksmines.command.provider

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.xkyrell.ksmines.mine.service.MineResolver
import java.util.concurrent.CompletableFuture

class MineSuggestionProvider(
    private val resolver: MineResolver
) : SuggestionProvider<CommandSourceStack> {

    override fun getSuggestions(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val remaining = builder.remainingLowerCase
        resolver.getMines()
            .map { it.key().value() }
            .filter { it.lowercase().startsWith(remaining) }
            .forEach(builder::suggest)

        return builder.buildFuture()
    }
}
