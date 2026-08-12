package me.xkyrell.ksmines.mine

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.util.locale.LocaleComponent
import me.xkyrell.ksmines.config.section.SoundSection
import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.temporal.Temporal

@Serializable
data class MineStage(
    @Contextual val displayName: LocaleComponent,
    val activationNotification: List<@Contextual LocaleComponent>? = null,
    val activationSound: SoundSection? = null,
    val refillNotification: List<@Contextual LocaleComponent>? = null,
    val refillSound: SoundSection? = null,
    @Contextual val refillTime: Temporal,
    val blocks: Set<MineBlock>
)
