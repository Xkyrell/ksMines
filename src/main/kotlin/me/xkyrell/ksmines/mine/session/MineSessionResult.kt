package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.MineTemplate
import net.kyori.adventure.key.Key

data class MineSessionResult(
    val targetIdentity: Key,
    val modifiedTemplate: MineTemplate,
    val noChanges: Boolean,
    val editedStage: Int
)
