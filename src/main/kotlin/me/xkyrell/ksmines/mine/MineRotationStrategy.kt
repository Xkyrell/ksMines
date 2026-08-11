package me.xkyrell.ksmines.mine

import kotlinx.serialization.Serializable

@Serializable
enum class MineRotationStrategy {

    RANDOM {
        override fun next(items: List<MineStage>, index: Int): MineStage {
            return items.random()
        }
    },

    SEQUENTIAL {
        override fun next(items: List<MineStage>, index: Int): MineStage {
            return items[index % items.size]
        }
    };

    abstract fun next(items: List<MineStage>, index: Int): MineStage

}