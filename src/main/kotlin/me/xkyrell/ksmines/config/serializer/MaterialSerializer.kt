package me.xkyrell.ksmines.config.serializer

import org.bukkit.Material
import org.bukkit.Registry

class MaterialSerializer : KeyedSerializer<Material>(
    type = "material",
    registry = Registry.MATERIAL
)