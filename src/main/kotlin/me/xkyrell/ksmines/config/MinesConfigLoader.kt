package me.xkyrell.ksmines.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.modules.SerializersModule
import me.xkyrell.ksmines.config.serializer.*
import me.xkyrell.ksmines.mine.MineTemplate
import me.xkyrell.ksmines.mine.MineTemplates
import me.xkyrell.temporal.Temporal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import java.nio.file.Files
import java.nio.file.Path

class MinesConfigLoader(
    dataFolder: Path
) : AbstractConfigLoader(dataFolder) {

    override val yaml: Yaml = Yaml(
        serializersModule = SerializersModule {
            contextual(Material::class, MaterialSerializer())
            contextual(Sound::class, SoundSerializer())
            contextual(ItemStack::class, ItemStackSerializer())
            contextual(Location::class, LocationSerializer())
            contextual(Temporal::class, TemporalSerializer())
        }
    )

    private val minesDirectory: Path = dataFolder.resolve("mines")

    private val templates: MutableMap<String, MineTemplate> = HashMap()

    override fun load() {
        try {
            Files.createDirectories(minesDirectory)
            loadAll()
            if (templates.isEmpty()) {
                createDefaults()
            }
        }
        catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to load mine templates",
                ex
            )
        }
    }

    override fun reload() {
        try {
            templates.clear()
            loadAll()
            if (templates.isEmpty()) {
                createDefaults()
            }
        }
        catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to reload mine templates",
                ex
            )
        }
    }

    override fun save() {
        templates.forEach { (key, template) -> saveTemplate(key, template) }
    }

    private fun loadAll() {
        Files.newDirectoryStream(
            minesDirectory,
            "*.yml"
        ).use { stream ->
            for (file in stream) {
                val key = getNameFromFile(file)
                try {
                    val template = load(
                        file,
                        MineTemplate.serializer()
                    )

                    templates[key] = template
                }
                catch (ex: Exception) {
                    throw IllegalStateException(
                        "Failed to load mine template: ${file.fileName}",
                        ex
                    )
                }
            }
        }
    }

    private fun getNameFromFile(file: Path): String {
        val name = file.fileName
            .toString()
            .removeSuffix(".yml")

        require(name.isNotBlank()) {
            "Mine template filename cannot be empty"
        }

        return name
    }

    private fun createDefaults() {
        saveTemplate("normal", MineTemplates.DEFAULTS)
    }

    fun saveTemplate(
        name: String,
        template: MineTemplate
    ) {
        templates[name] = template

        save(
            minesDirectory.resolve("${name}.yml"),
            MineTemplate.serializer(),
            template
        )
    }

    fun removeTemplate(
        name: String
    ) {
        templates.remove(name)
        try {
            Files.deleteIfExists(minesDirectory.resolve("${name}.yml"))
        }
        catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to remove mine template: $name",
                ex
            )
        }
    }

    fun getLoadedTemplates(): Map<String, MineTemplate> = templates.toMap()

}