package me.xkyrell.ksmines.util.random

import kotlin.random.Random

class WeightSet<W : Weighted> private constructor(
    private val weights: MutableSet<W> = LinkedHashSet()
) : MutableSet<W> by weights {

    private val totalWeight: Double
        get() = weights.sumOf { it.getProbability() }

    operator fun get(index: Int): W? {
        if (index < 0 || index >= weights.size) return null
        return weights.elementAtOrNull(index)
    }

    fun getRandomItem(random: Random = Random.Default): W {
        check(weights.isNotEmpty()) {
            "Cannot generate random item from an empty set of weights"
        }

        val total = totalWeight
        check(total > 0.0) {
            "Cannot select random item: total weight is zero or negative"
        }

        var value = random.nextDouble(total)
        for (weight in weights) {
            value -= weight.getProbability()
            if (value < 0.0) {
                return weight
            }
        }

        return weights.last()
    }

    companion object {

        fun <W : Weighted> create(): WeightSet<W> = WeightSet()

        fun <W : Weighted> create(collection: Collection<W>): WeightSet<W> =
            WeightSet(LinkedHashSet(collection))
    }
}