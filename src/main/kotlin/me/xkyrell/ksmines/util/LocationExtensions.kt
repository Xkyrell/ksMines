package me.xkyrell.ksmines.util

import org.bukkit.Location

fun Location.formatted(): String = "${world.name}, $blockX, $blockY, $blockZ"