package me.xkyrell.ksmines.dagger.module

import dagger.Module
import dagger.Provides
import org.bukkit.plugin.Plugin

@Module
class DefaultModule(private val plugin: Plugin) {

    @Provides
    fun providePlugin(): Plugin {
        return plugin
    }
}