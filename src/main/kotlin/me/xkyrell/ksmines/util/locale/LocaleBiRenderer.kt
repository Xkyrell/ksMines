package me.xkyrell.ksmines.util.locale

import me.xkyrell.ksmines.config.section.LocaleSection
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleBiRenderer<A, B> {

    fun render(
        locale: LocaleSection,
        first: A,
        second: B
    ): Component

    fun sendMessage(
        audience: Audience,
        locale: LocaleSection,
        first: A,
        second: B
    ) {
        audience.sendMessage(render(locale, first, second))
    }

    fun sendMessageForAll(
        server: Server,
        locale: LocaleSection,
        first: A,
        second: B
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, first, second)
        }
    }
}