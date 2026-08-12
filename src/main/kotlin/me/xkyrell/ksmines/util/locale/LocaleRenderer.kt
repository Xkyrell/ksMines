package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleRenderer<T> {

    fun render(
        locale: LocaleComponent,
        value: T
    ): Component

    fun sendMessage(
        audience: Audience,
        locale: LocaleComponent,
        value: T
    ) {
        audience.sendMessage(render(locale, value))
    }

    fun sendMessageForAll(
        server: Server,
        locale: LocaleComponent,
        value: T
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, value)
        }
    }
}