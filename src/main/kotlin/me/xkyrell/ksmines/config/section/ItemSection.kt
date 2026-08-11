package me.xkyrell.ksmines.config.section

import com.destroystokyo.paper.profile.ProfileProperty
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.datacomponent.item.ResolvableProfile
import io.papermc.paper.datacomponent.item.TooltipDisplay
import kotlinx.serialization.Serializable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Serializable
data class ItemSection(
    var displayName: LocaleSection? = null,
    var lore: List<LocaleSection>? = null,
    var material: Material? = null,
    var texture: String? = null,
    var hideAttributes: Boolean = false,
    var glowing: Boolean = false,
    var amount: Int? = null
) {

    @Suppress("UnstableApiUsage")
    fun compose() : ItemStack {
        val item = ItemStack(
            material ?: Material.STONE,
            amount ?: 1
        )

        texture?.let { texture ->
            if (material != Material.PLAYER_HEAD) {
                error("texture can only be used with PLAYER_HEAD")
            }

            item.setData(
                DataComponentTypes.PROFILE,
                ResolvableProfile.resolvableProfile()
                    .addProperty(ProfileProperty("textures", texture))
            )
        }

        displayName?.let {
            item.setData(DataComponentTypes.CUSTOM_NAME, it.asComponent())
        }

        lore?.let {
            item.setData(
                DataComponentTypes.LORE,
                ItemLore.lore(it)
            )
        }

        if (hideAttributes) {
            item.setData(
                DataComponentTypes.TOOLTIP_DISPLAY,
                TooltipDisplay.tooltipDisplay()
                    .hideTooltip(false)
                    .build()
            )
        }

        if (glowing) {
            item.setData(
                DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
                true
            )
        }

        return item
    }
}
