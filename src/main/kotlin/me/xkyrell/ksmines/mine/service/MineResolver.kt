package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.Mine
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern

interface MineResolver {

    fun resolve(@KeyPattern key: String) : Mine?

    fun resolve(key: Key) : Mine?

    fun resolveOrThrow(@KeyPattern name: String) : Mine

    fun resolveOrThrow(key: Key) : Mine

    fun getActiveMines() : Collection<Mine>

    fun getMines() : Collection<Mine>

}