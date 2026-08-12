package me.xkyrell.ksmines.dagger.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import me.xkyrell.ksmines.config.*
import org.bukkit.plugin.Plugin

@Module
class ConfigModule {

    @Provides
    @Reusable
    fun provideKitConfigLoader(plugin: Plugin): MinesConfigLoader {
        return MinesConfigLoader(plugin.dataFolder.toPath())
    }

    @Provides
    @Reusable
    fun provideConfigLoader(plugin: Plugin): CommonConfigLoader {
        return CommonConfigLoader(plugin.dataFolder.toPath())
    }
}