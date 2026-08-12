package me.xkyrell.ksmines.command.provider

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.xkyrell.ksmines.config.MinesConfigLoader
import java.util.concurrent.CompletableFuture

class TemplateSuggestionProvider(
    private val loader: MinesConfigLoader
) : SuggestionProvider<CommandSourceStack> {

    override fun getSuggestions(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val remaining = builder.remainingLowerCase
        loader.getLoadedTemplates().keys
            .filter { it.lowercase().startsWith(remaining) }
            .forEach(builder::suggest)

        return builder.buildFuture()
    }
}