package me.xkyrell.ksmines.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.YamlNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import me.xkyrell.ksmines.config.serializer.LocaleComponentSerializer
import me.xkyrell.ksmines.config.serializer.MaterialSerializer
import me.xkyrell.ksmines.config.serializer.TemporalSerializer
import me.xkyrell.ksmines.util.locale.LocaleComponent
import me.xkyrell.temporal.Temporal
import org.bukkit.Material
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

class CommonConfigLoader @Inject constructor(
    dataFolder: Path
) : AbstractConfigLoader(dataFolder) {

    override val yaml = Yaml(
        serializersModule = SerializersModule {
            contextual(Material::class, MaterialSerializer())
            contextual(Temporal::class, TemporalSerializer())
            contextual(LocaleComponent::class, LocaleComponentSerializer())
        },
        configuration = YamlConfiguration(
            yamlNamingStrategy = YamlNamingStrategy.KebabCase
        )
    )

    private lateinit var general: GeneralConfig
    private lateinit var language: LanguageConfig

    override fun load() {
        Files.createDirectories(dataFolder)
        reload()
    }

    override fun reload() {
        general = loadOrCreate(
            path = dataFolder.resolve("config.yml"),
            serializer = GeneralConfig.serializer(),
            fallback = ::GeneralConfig
        )

        language = loadOrCreate(
            path = dataFolder.resolve("language.yml"),
            serializer = LanguageConfig.serializer(),
            fallback = ::LanguageConfig
        )
    }

    override fun save() {
        save(
            path = dataFolder.resolve("config.yml"),
            serializer = GeneralConfig.serializer(),
            value = general
        )

        save(
            path = dataFolder.resolve("language.yml"),
            serializer = LanguageConfig.serializer(),
            value = language
        )
    }

    fun getGeneralConfig(): GeneralConfig = general

    fun getLanguageConfig(): LanguageConfig = language

}