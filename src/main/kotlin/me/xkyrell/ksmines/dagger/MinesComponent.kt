package me.xkyrell.ksmines.dagger

import dagger.Component
import me.xkyrell.ksmines.MineTask
import me.xkyrell.ksmines.command.MainCommand
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.config.MinesConfigLoader
import me.xkyrell.ksmines.dagger.module.*
import me.xkyrell.ksmines.listener.MineListener
import me.xkyrell.ksmines.listener.MineSessionListener

@Component(modules = [
    DefaultModule::class, ConfigModule::class,
    ServiceModule::class, MineModule::class,
    CommandModule::class
])
interface MinesComponent {

    fun getCommonConfigLoader(): CommonConfigLoader

    fun getMinesConfigLoader(): MinesConfigLoader

    fun getMineListener(): MineListener

    fun getMineSessionListener(): MineSessionListener

    fun getTask(): MineTask

    fun getMainCommand(): MainCommand

}