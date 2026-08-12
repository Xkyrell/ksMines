package me.xkyrell.ksmines.config.section

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.Server
import org.bukkit.Sound
import org.bukkit.entity.Player

@Serializable
data class SoundSection(
    @Contextual val sound: Sound,
    val volume: Float = 1.0f,
    val pitch: Float = 1.0f
) {

    fun play(player: Player) {
        player.playSound(player.location, sound, volume, pitch)
    }

    fun playForAll(server: Server) {
        server.onlinePlayers.forEach { play(it) }
    }
}
