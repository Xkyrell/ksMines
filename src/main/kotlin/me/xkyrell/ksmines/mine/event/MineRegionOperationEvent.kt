package me.xkyrell.ksmines.mine.event

import me.xkyrell.ksmines.mine.Mine
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class MineRegionOperationEvent(
    val mine: Mine,
    val nearbyPlayers: Set<Player>
) : Event() {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {

        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS

    }
}