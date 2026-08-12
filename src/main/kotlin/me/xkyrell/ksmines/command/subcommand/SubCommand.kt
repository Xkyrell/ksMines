package me.xkyrell.ksmines.command.subcommand

import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack

interface SubCommand {

    fun build(): LiteralCommandNode<CommandSourceStack>

}