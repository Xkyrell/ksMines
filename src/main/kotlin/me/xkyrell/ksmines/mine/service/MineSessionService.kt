package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.session.MineSession
import org.bukkit.entity.Player

interface MineSessionService {

    fun register(session: MineSession)

    fun unregister(owner: Player) : Boolean

    fun unregisterAll()

    fun getResolver() : MineSessionResolver

}