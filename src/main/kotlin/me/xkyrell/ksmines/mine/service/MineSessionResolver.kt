package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.session.MineSession
import org.bukkit.entity.Player

interface MineSessionResolver {

    fun resolveSession(owner: Player): MineSession?

    fun resolveSession(mine: Mine): MineSession?

    fun getSessions() : Collection<MineSession>

}