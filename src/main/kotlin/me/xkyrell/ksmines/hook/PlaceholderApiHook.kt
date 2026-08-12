package me.xkyrell.ksmines.hook

import io.papermc.paper.plugin.configuration.PluginMeta
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.service.MineResolver
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import javax.inject.Inject

class PlaceholderApiHook @Inject constructor(
    private val resolver: MineResolver,
    private val loader: CommonConfigLoader,
    private val plugin: Plugin
) {

    private var expansion: Expansion? = null

    fun register() {
        val manager = plugin.server.pluginManager
        if (!manager.isPluginEnabled("PlaceholderAPI")) {
            return
        }

        if (expansion == null) {
            expansion = Expansion(resolver, loader, plugin.pluginMeta).also {
                it.register()
            }
        }
    }

    fun unregister() {
        expansion?.unregister()
        expansion = null
    }

    private class Expansion(
        private val resolver: MineResolver,
        private val loader: CommonConfigLoader,
        private val meta: PluginMeta
    ) : PlaceholderExpansion() {

        override fun onPlaceholderRequest(
            player: Player?,
            params: String
        ): String? {
            val parts = params.split('_')
            if (parts.size < 3) {
                return null
            }

            val name = parts.drop(2).joinToString("_")
            val mine = resolver.resolve(name) ?: return "?"
            val language = loader.getLanguageConfig();
            val type = "${parts[0]}_${parts[1]}"
            return when (type) {
                "current_stage" -> mine.getCurrentStage().displayName.getSource()
                "next_stage" -> mine.getNextStage().displayName.getSource()
                "refill_time" -> mine.getRemainingRefillTime()
                    .formatAs(language.temporalStyle)

                "stage_time" -> mine.getRemainingStageTime()
                    .formatAs(language.temporalStyle)

                else -> null
            }
        }

        override fun getIdentifier(): String {
            return meta.name
        }

        override fun getAuthor(): String {
            return meta.authors.first()
        }

        override fun getVersion(): String {
            return "1.0"
        }
    }
}