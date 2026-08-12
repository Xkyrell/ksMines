package me.xkyrell.ksmines.dagger.module

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import me.xkyrell.ksmines.command.subcommand.*

@Module
interface CommandModule {

    @Binds
    @IntoSet
    fun bindAddBlockCommand(command: AddBlockSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindAddDropCommand(command: AddDropSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindCreateCommand(command: CreateSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindHelpCommand(command: HelpSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindListCommand(command: ListSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindRefillCommand(command: RefillSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindReloadCommand(command: ReloadSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindRemoveBlockCommand(command: RemoveBlockSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindRemoveDropCommand(command: RemoveDropSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindRemoveCommand(command: RemoveSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindRotateCommand(command: RotateSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindSessionCommand(command: SessionSubCommand): SubCommand

    @Binds
    @IntoSet
    fun bindTeleportCommand(command: TeleportSubCommand): SubCommand

}