package me.xkyrell.ksmines.mine.block.responsible

import org.bukkit.event.block.BlockBreakEvent

interface DropResponsible {

    fun onDrop(event: BlockBreakEvent);

}