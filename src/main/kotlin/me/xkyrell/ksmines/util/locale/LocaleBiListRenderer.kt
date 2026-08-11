package me.xkyrell.ksmines.util.locale

import me.xkyrell.ksmines.config.section.LocaleSection
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleBiListRenderer<A, B> {

    fun render(
        locales: List<LocaleSection>,
        first: A,
        second: B
    ): List<Component>

    fun sendMessage(
        audience: Audience,
        locale: List<LocaleSection>,
        first: A,
        second: B
    ) {
        render(locale, first, second).forEach(audience::sendMessage)
    }

    fun sendMessageForAll(
        server: Server,
        locales: List<LocaleSection>,
        first: A,
        second: B
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locales, first, second)
        }
    }
}