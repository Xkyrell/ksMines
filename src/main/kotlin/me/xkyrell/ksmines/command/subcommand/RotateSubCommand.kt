package me.xkyrell.ksmines.command.subcommand

import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.MessageRenderers
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.service.MineResolver
import org.bukkit.entity.Player
import javax.inject.Inject

class RotateSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val mineResolver: MineResolver
) : AbstractMineActionSubCommand(
    loader, mineResolver, "rotate", MinesConstants.ROTATE_PERMISSION
) {

    override fun handleManualActionInternal(mine: Mine, sender: Player) {
        mine.rotate()

        val language = loader.getLanguageConfig()
        MessageRenderers.MINE_ACTOR.sendMessage(
            sender, language.mineRotated, mine, sender, language.prefix
        )
    }
}