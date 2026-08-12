package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleTriRenderer<A, B, C> {

    fun render(
        locale: LocaleComponent,
        first: A,
        second: B,
        third: C
    ): Component

    fun sendMessage(
        audience: Audience,
        locale: LocaleComponent,
        first: A,
        second: B,
        third: C
    ) {
        audience.sendMessage(render(locale, first, second, third))
    }

    fun sendMessageForAll(
        server: Server,
        locale: LocaleComponent,
        first: A,
        second: B,
        third: C
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, first, second, third)
        }
    }
}