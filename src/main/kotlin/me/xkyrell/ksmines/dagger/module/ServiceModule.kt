package me.xkyrell.ksmines.dagger.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import me.xkyrell.ksmines.mine.service.*

@Module
interface ServiceModule {

    @Binds
    @Reusable
    fun bindMineService(impl: MineServiceImpl): MineService

    @Binds
    @Reusable
    fun bindMineSessionService(impl: MineSessionServiceImpl): MineSessionService

    companion object {

        @Provides
        @Reusable
        fun provideMineResolver(service: MineService): MineResolver {
            return service.getResolver()
        }

        @Provides
        @Reusable
        fun provideMineSessionResolver(service: MineSessionService): MineSessionResolver {
            return service.getResolver()
        }

    }
}