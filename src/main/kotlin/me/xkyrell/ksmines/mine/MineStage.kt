package me.xkyrell.ksmines.mine

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.config.section.LocaleSection
import me.xkyrell.ksmines.config.section.SoundSection
import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.temporal.Temporal

@Serializable
data class MineStage(
    val displayName: LocaleSection,
    val activationNotification: List<LocaleSection>? = null,
    val activationSound: SoundSection? = null,
    val refillNotification: List<LocaleSection>? = null,
    val refillSound: SoundSection? = null,
    @Contextual val refillTime: Temporal,
    val blocks: Set<MineBlock>
)
