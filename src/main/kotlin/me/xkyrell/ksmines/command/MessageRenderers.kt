package me.xkyrell.ksmines.command

import me.xkyrell.ksmines.config.LanguageConfig
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.util.formatted
import me.xkyrell.ksmines.util.locale.LocaleBiListRenderer
import me.xkyrell.ksmines.util.locale.LocaleBiRenderer
import me.xkyrell.ksmines.util.locale.LocaleTriRenderer
import me.xkyrell.ksmines.util.locale.TagResolvers
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

object MessageRenderers {

    val MINE_LIST = LocaleBiListRenderer<Collection<Mine>, LanguageConfig> { locale, mines, language ->
        val prefix = Placeholder.component("prefix", language.prefix)
        val entries = mines.map { mine ->
            language.mineList.entry.asComponent(
                Placeholder.unparsed("mine", mine.key().value()),
                Placeholder.component("mine_is_active",
                    if (mine.isActive()) {
                        language.mineList.active
                    }
                    else {
                        language.mineList.inactive
                    }
                ),
                Placeholder.unparsed("location",
                    mine.getTemplate().region?.getCenter()?.formatted() ?: "?"
                )
            )
        }.ifEmpty {
            listOf(language.mineList.empty)
        }

        locale.map { it.asComponent(prefix, TagResolvers.components(
                "mine-entry", replacements = entries
                )
            )
        }
    }

    val MINE_ACTOR = LocaleTriRenderer<Mine, Player, ComponentLike> { locale, mine, sender, prefix ->
        locale.asComponent(
            Placeholder.component("prefix", prefix),
            Placeholder.unparsed("mine", mine.key().value()),
            Placeholder.unparsed("player", sender.name)
        )
    }
}