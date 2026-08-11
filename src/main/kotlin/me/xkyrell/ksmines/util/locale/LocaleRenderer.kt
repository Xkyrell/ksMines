package me.xkyrell.ksmines.util.locale

import me.xkyrell.ksmines.config.section.LocaleSection
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Server

fun interface LocaleRenderer<T> {

    fun render(
        locale: LocaleSection,
        value: T
    ): Component

    fun sendMessage(
        audience: Audience,
        locale: LocaleSection,
        value: T
    ) {
        audience.sendMessage(render(locale, value))
    }

    fun sendMessageForAll(
        server: Server,
        locale: LocaleSection,
        value: T
    ) {
        server.onlinePlayers.forEach {
            sendMessage(it, locale, value)
        }
    }
}