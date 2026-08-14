package me.xkyrell.ksmines.mine.block

import org.bukkit.plugin.Plugin

enum class MineBlockProcessorStrategy {

    INSTANT {
        override fun compose(plugin: Plugin): MineBlockProcessor {
            return null
        }
    },
    QUEUE {
        override fun compose(plugin: Plugin): MineBlockProcessor {
            return null
        }
    };

    abstract fun compose(plugin: Plugin) : MineBlockProcessor

}