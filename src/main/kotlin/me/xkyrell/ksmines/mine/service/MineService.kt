package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.Mine
import net.kyori.adventure.key.Key

interface MineService {

    fun register(mine: Mine)

    fun unregister(key: Key, cleanUp: Boolean = false)

    fun unregisterAll(cleanUp: Boolean = false)

    fun getResolver() : MineResolver
}