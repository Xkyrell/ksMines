package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine

data class MineSessionResult(
    val mine: Mine,
    val noChanges: Boolean,
    val editedStage: Int
)
