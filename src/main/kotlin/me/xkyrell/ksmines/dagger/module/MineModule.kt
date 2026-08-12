package me.xkyrell.ksmines.dagger.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import me.xkyrell.ksmines.config.CommonConfigLoader
import me.xkyrell.ksmines.mine.block.MineBlockFactory
import me.xkyrell.ksmines.mine.session.MineSessionFactory
import org.bukkit.plugin.Plugin

@Module
class MineModule {

    @Provides
    @Reusable
    fun provideMineBlockFactory(): MineBlockFactory =
        MineBlockFactory()

    @Provides
    @Reusable
    fun provideMineSessionFactory(
        plugin: Plugin,
        commonConfigLoader: CommonConfigLoader
    ): MineSessionFactory =
        MineSessionFactory(plugin, commonConfigLoader)
}