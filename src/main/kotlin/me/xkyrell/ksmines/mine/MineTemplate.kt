package me.xkyrell.ksmines.mine

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.util.Cuboid
import me.xkyrell.temporal.Temporal
import org.bukkit.Location

@Serializable
data class MineTemplate(
    @Contextual val stageTime: Temporal,
    val teleportPlayersOnRefill: Boolean,
    val rotationStrategy: MineRotationStrategy,
    val rotationStages: List<MineStage>,
    val region: Cuboid?,
    @Contextual val refillLocation: Location?
) {

    fun getRegionOrThrow(): Cuboid {
        return requireNotNull(region) {
            "region is not configured"
        }
    }
}
