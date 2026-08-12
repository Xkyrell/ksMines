package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleListRenderer<T> {

    fun render(
        locale: List<LocaleComponent>,
        value: T
    ): List<Component>

    fun sendMessage(
        audience: Audience,
        locale: List<LocaleComponent>,
        value: T
    ) {
        render(locale, value).forEach(audience::sendMessage)
    }

    fun sendMessageForAll(
        server: Server,
        locale: List<LocaleComponent>,
        value: T
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, value)
        }
    }
}