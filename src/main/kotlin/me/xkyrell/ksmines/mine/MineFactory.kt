package me.xkyrell.ksmines.mine

import net.kyori.adventure.key.Key
import org.bukkit.plugin.Plugin
import javax.inject.Inject

class MineFactory @Inject constructor (val plugin: Plugin) {

    fun createMine(key: Key, template: MineTemplate) : Mine {
        val populator = template.processorStrategy.compose(plugin)
        return MineImpl(key, template, populator)
    }
}