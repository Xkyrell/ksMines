package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleBiRenderer<A, B> {

    fun render(
        locale: LocaleComponent,
        first: A,
        second: B
    ): Component

    fun sendMessage(
        audience: Audience,
        locale: LocaleComponent,
        first: A,
        second: B
    ) {
        audience.sendMessage(render(locale, first, second))
    }

    fun sendMessageForAll(
        server: Server,
        locale: LocaleComponent,
        first: A,
        second: B
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, first, second)
        }
    }
}