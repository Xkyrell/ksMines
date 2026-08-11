package me.xkyrell.ksmines.config.serializer

import org.bukkit.Registry
import org.bukkit.Sound

class SoundSerializer : KeyedSerializer<Sound>(
    type = "sound",
    registry = Registry.SOUNDS
)