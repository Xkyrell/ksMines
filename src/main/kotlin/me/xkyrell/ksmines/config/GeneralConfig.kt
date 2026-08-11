package me.xkyrell.ksmines.config

import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.config.section.ItemSection
import me.xkyrell.ksmines.config.section.LocaleSection
import org.bukkit.Material

@Serializable
data class GeneralConfig(
    val allowBlockInteractionInSession: Boolean = true,
    val editorItem: ItemSection = ItemSection(
        material = Material.BLAZE_ROD,
        displayName = LocaleSection("<gradient:#FFD700:#B87333>Палка для разметки региона"),
        lore = listOf(
            LocaleSection("Левый клик, первая позиция"),
            LocaleSection("Правый клик, вторая позиция")
        ),
        hideAttributes = true
    )
)