package me.xkyrell.ksmines.mine.block

import org.bukkit.plugin.Plugin

enum class MineBlockProcessorStrategy {

    INSTANT {
        override fun compose(plugin: Plugin): MineBlockProcessor {
            return InstantBlockProcessor(plugin)
        }
    },
    QUEUE {
        override fun compose(plugin: Plugin): MineBlockProcessor {
            return QueueBlockProcessor(plugin)
        }
    };

    abstract fun compose(plugin: Plugin) : MineBlockProcessor

}