package me.xkyrell.ksmines.mine

import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.ksmines.util.*
import me.xkyrell.ksmines.util.random.WeightSet
import me.xkyrell.temporal.Temporal
import me.xkyrell.temporal.TemporalUnit
import net.kyori.adventure.key.Key

internal class MineImpl(
    private val key: Key,
    private val template: MineTemplate
) : Mine, Observable {

    private var currentStage: MineStage
    private var nextStage: MineStage
    private var currentBlocks: WeightSet<MineBlock>
    private var remainingStageTime: Temporal
    private var remainingRefillTime: Temporal
    private var active = false

    init {
        require(template.rotationStages.isNotEmpty()) {
            "template must contain at least one rotation stage"
        }

        currentStage = template.rotationStages.first()
        nextStage = template.rotationStrategy.next(template.rotationStages, 0)
        currentBlocks = WeightSet.create(currentStage.blocks)
        remainingRefillTime = currentStage.refillTime.clone()
        remainingStageTime = template.stageTime.clone()
    }

    override fun onObserve(): Observable.Result {
        val second = TemporalUnit.SECONDS.toMillis()
        if (remainingStageTime.isZero) {
            rotate()
            refill()
            return Observable.Result.ROTATED
        }

        if (remainingRefillTime.isZero) {
            refill()
            return Observable.Result.REFILLED
        }

        if (template.rotationStages.size > 1) {
            remainingStageTime.operation { it - second }
        }

        remainingRefillTime.operation { it - second }
        return Observable.Result.IDLE
    }

    override fun refill() {
        check(active) {
            "Cannot refill inactive mine"
        }

        requireNotNull(template.region) {
            "region is not configured"
        }

        for (block in template.region) {
            block.setType(currentBlocks.getRandomItem().getMaterial(), false)
        }

        remainingRefillTime = currentStage.refillTime.clone()
        if (template.teleportPlayersOnRefill && template.refillLocation != null) {
            for (player in template.refillLocation.world.players) {
                if (template.region.contains(player.location)) {
                    player.teleport(template.refillLocation)
                }
            }
        }
    }

    override fun rotate() {
        check(active) {
            "Cannot rotate inactive mine"
        }

        currentStage = nextStage
        val stages = template.rotationStages
        nextStage = template.rotationStrategy.next(
            stages, stages.indexOf(currentStage)
        )
        currentBlocks = WeightSet.create(
            currentStage.blocks
        )

        remainingStageTime = template.stageTime.clone()
    }

    override fun key(): Key = key

    override fun getTemplate(): MineTemplate = template

    override fun getCurrentStage(): MineStage = currentStage

    override fun getNextStage(): MineStage = nextStage

    override fun getRemainingStageTime(): Temporal = remainingStageTime

    override fun getRemainingRefillTime(): Temporal = remainingRefillTime

    override fun isActive(): Boolean = active

    override fun setActive(active: Boolean) {
        if (template.region != null) {
            this.active = active
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MineImpl

        if (key != other.key) return false
        if (template != other.template) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + template.hashCode()
        return result
    }
}