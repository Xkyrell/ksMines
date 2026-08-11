package me.xkyrell.ksmines.mine

import me.xkyrell.temporal.Temporal
import net.kyori.adventure.key.Keyed

interface Mine : Keyed {

    fun refill()

    fun rotate()

    fun getTemplate(): MineTemplate

    fun getCurrentStage(): MineStage

    fun getNextStage(): MineStage

    fun getRemainingStageTime(): Temporal

    fun getRemainingRefillTime(): Temporal

    fun isActive(): Boolean

    fun setActive(active: Boolean)

}