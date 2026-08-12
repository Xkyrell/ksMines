package me.xkyrell.ksmines

import me.xkyrell.ksmines.dagger.DaggerMinesComponent
import me.xkyrell.ksmines.dagger.MinesComponent
import me.xkyrell.ksmines.dagger.module.ConfigModule
import me.xkyrell.ksmines.dagger.module.DefaultModule
import me.xkyrell.ksmines.dagger.module.MineModule
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class MinesPlugin : JavaPlugin() {

    private lateinit var component: MinesComponent
    private lateinit var task: MineTask

    override fun onEnable() {
        component = DaggerMinesComponent
            .builder()
            .defaultModule(DefaultModule(this))
            .configModule(ConfigModule())
            .mineModule(MineModule())
            .build()

        component.getCommonConfigLoader().load()
        component.getMinesConfigLoader().load()

        task = component.getTask()
        task.start()

        component.getMainCommand().register(this)
        server.pluginManager.registerEvents(
            component.getMineListener(),
            this
        )

        server.pluginManager.registerEvents(
            component.getMineSessionListener(),
            this
        )
    }

    override fun onDisable() {
        task.shutdown()
        HandlerList.unregisterAll(this)
    }
}