package me.xkyrell.ksmines.util.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.TagPattern
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object TagResolvers {

    fun components(
        @TagPattern key: String,
        separator: JoinConfiguration = JoinConfiguration.newlines(),
        replacements: Collection<ComponentLike?>
    ): TagResolver {
        return TagResolver.resolver(key, Tag.inserting(Component.join(separator, replacements)))
    }
}