package me.xkyrell.ksmines.config.section

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

@Serializable
class LocaleSection(
    private val source: String
) : ComponentLike {

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