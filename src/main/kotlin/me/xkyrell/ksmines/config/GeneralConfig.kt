package me.xkyrell.ksmines.config

import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.config.section.ItemSection
import me.xkyrell.ksmines.util.locale.LocaleComponent
import org.bukkit.Material

@Serializable
data class GeneralConfig(
    val allowBlockInteractionInSession: Boolean = true,
    val editorItem: ItemSection = ItemSection(
        material = Material.BLAZE_ROD,
        displayName = LocaleComponent("<gradient:#FFD700:#B87333>Палка для разметки региона"),
        lore = listOf(
            LocaleComponent("Левый клик, первая позиция"),
            LocaleComponent("Правый клик, вторая позиция")
        ),
        hideAttributes = true
    )
)