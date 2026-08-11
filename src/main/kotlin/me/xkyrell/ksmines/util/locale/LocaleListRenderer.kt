package me.xkyrell.ksmines.util.locale

import me.xkyrell.ksmines.config.section.LocaleSection
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleListRenderer<T> {

    fun render(
        locale: List<LocaleSection>,
        value: T
    ): List<Component>

    fun sendMessage(
        audience: Audience,
        locale: List<LocaleSection>,
        value: T
    ) {
        render(locale, value).forEach(audience::sendMessage)
    }

    fun sendMessageForAll(
        server: Server,
        locale: List<LocaleSection>,
        value: T
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, value)
        }
    }
}