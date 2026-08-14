package me.xkyrell.ksmines.config.section

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.util.locale.LocaleComponent
import me.xkyrell.temporal.Temporal
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import org.bukkit.entity.Player

@Serializable
data class TitleSection(
    @Contextual val title: LocaleComponent,
    @Contextual val subtitle: LocaleComponent,
    @Contextual val fadeIn: Temporal = DEFAULT_FADE_IN,
    @Contextual val stay: Temporal = DEFAULT_STAY,
    @Contextual val fadeOut: Temporal = DEFAULT_FADE_OUT
) {

    private val composedTitle by lazy {
        val times = Title.Times.times(
            fadeIn.toDuration(), stay.toDuration(), fadeOut.toDuration()
        )
        Title.title(title.asComponent(), subtitle.asComponent(), times)
    }

    fun sendTitle(player: Player) {
        player.showTitle(composedTitle)
    }

    private companion object {

        private val DEFAULT_FADE_IN = Temporal.of(10L * Ticks.SINGLE_TICK_DURATION_MS)
        private val DEFAULT_STAY = Temporal.of(70L * Ticks.SINGLE_TICK_DURATION_MS)
        private val DEFAULT_FADE_OUT = Temporal.of(20L * Ticks.SINGLE_TICK_DURATION_MS)

    }
}
