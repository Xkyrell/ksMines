package me.xkyrell.ksmines.command.subcommand

import me.xkyrell.ksmines.MinesConstants
import me.xkyrell.ksmines.command.MessageRenderers
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.service.MineResolver
import org.bukkit.entity.Player
import javax.inject.Inject

class RefillSubCommand @Inject constructor(
    private val loader: CommonConfigLoader,
    private val mineResolver: MineResolver
) : AbstractMineActionSubCommand(
    loader, mineResolver, "refill", MinesConstants.REFILL_PERMISSION
) {

    override fun handleManualActionInternal(mine: Mine, sender: Player) {
        mine.refill()

        val language = loader.getLanguageConfig()
        MessageRenderers.MINE_ACTOR.sendMessage(
            sender, language.mineRefilled, mine, sender, language.prefix
        )
    }
}