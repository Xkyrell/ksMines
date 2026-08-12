package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class LocaleComponent(private val source: String) : ComponentLike {

    override fun asComponent(): Component {
        return MiniMessage.miniMessage().deserialize(source)
    }

    fun asComponent(vararg resolvers: TagResolver): Component {
        return MiniMessage.miniMessage().deserialize(
            source, TagResolver.resolver(*resolvers)
        )
    }

    fun getSource() : String = source;

}